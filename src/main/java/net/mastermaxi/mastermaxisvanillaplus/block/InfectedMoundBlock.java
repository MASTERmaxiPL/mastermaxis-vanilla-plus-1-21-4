package net.mastermaxi.mastermaxisvanillaplus.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class InfectedMoundBlock extends Block {
    public InfectedMoundBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && world instanceof ServerWorld serverWorld && player != null && !player.isCreative()) {
            player.damage(serverWorld, world.getDamageSources().generic(), 1.0F);
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if (!world.isClient) {
            // Schedule a single initial check shortly after placement
            world.scheduleBlockTick(pos, this, 60 + world.random.nextInt(40));
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        // Execute initial placement attack (no recurring scheduled ticks)
        tryInfectNearby(world, pos, random);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        tryInfectNearby(world, pos, random);
    }

    public static void tryInfectNearby(ServerWorld world, BlockPos pos, Random random) {
        // 1. Leader check: only ONE block per connected colony acts as the colony leader
        if (!isColonyLeader(world, pos)) {
            return;
        }

        // 2. Count size of the connected colony (handles irregular shapes via BFS)
        int colonySize = getColonySize(world, pos);

        // 3. Probability check based on colony size
        float attackChance = colonySize <= 3 ? 0.35F : (colonySize <= 8 ? 0.55F : 0.75F);
        if (random.nextFloat() > attackChance) {
            return;
        }

        // 4. Max concurrent infections check (prevents wiping out whole forests instantly)
        int maxSimultaneousInfections = colonySize <= 3 ? 1 : (colonySize <= 8 ? 2 : 3);
        int currentInfections = countActiveInfections(world, pos, 8);
        if (currentInfections >= maxSimultaneousInfections) {
            return; // Colony is already busy devouring other wood
        }

        // 5. Find nearest uninfected wood within 8 blocks
        for (BlockPos checkPos : BlockPos.iterateOutwards(pos, 8, 4, 8)) {
            BlockState checkState = world.getBlockState(checkPos);
            if (isInfectable(checkState)) {
                infect(world, checkPos.toImmutable(), checkState, random, colonySize);
                createTrail(world, pos, checkPos);
                return;
            }
        }
    }

    /**
     * Determines whether this block is the designated leader of the connected mound colony.
     * Prevents multiple blocks in the same mound from acting redundantly.
     */
    public static boolean isColonyLeader(World world, BlockPos pos) {
        // If there's an infected mound directly below, that lower block takes precedence
        if (world.getBlockState(pos.down()).isOf(Blocks.INFECTED_MOUND)) {
            return false;
        }
        // Break ties among ground blocks deterministically (favor minimum X and Z)
        return !world.getBlockState(pos.north()).isOf(Blocks.INFECTED_MOUND)
                && !world.getBlockState(pos.west()).isOf(Blocks.INFECTED_MOUND);
    }

    /**
     * Measures total connected mound blocks in any irregular shape up to a cap of 24 blocks.
     */
    public static int getColonySize(World world, BlockPos origin) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        queue.add(origin);
        visited.add(origin);

        int count = 0;
        while (!queue.isEmpty() && count < 24) {
            BlockPos current = queue.poll();
            count++;

            for (Direction dir : Direction.values()) {
                BlockPos neighbor = current.offset(dir);
                if (!visited.contains(neighbor) && world.getBlockState(neighbor).isOf(Blocks.INFECTED_MOUND)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return count;
    }

    /**
     * Counts active infected wood blocks in vicinity to enforce colony capacity limits.
     */
    public static int countActiveInfections(World world, BlockPos center, int radius) {
        int count = 0;
        for (BlockPos p : BlockPos.iterateOutwards(center, radius, 4, radius)) {
            if (world.getBlockState(p).isOf(Blocks.INFECTED_WOOD)) {
                count++;
                if (count >= 5) {
                    break;
                }
            }
        }
        return count;
    }

    public static void createTrail(ServerWorld world, BlockPos start, BlockPos end) {
        int dx = end.getX() - start.getX();
        int dz = end.getZ() - start.getZ();
        int steps = Math.max(Math.abs(dx), Math.abs(dz));
        if (steps > 1) {
            for (int i = 1; i < steps; i++) {
                int x = start.getX() + (dx * i) / steps;
                int z = start.getZ() + (dz * i) / steps;
                for (int dy = 3; dy >= -3; dy--) {
                    BlockPos surfacePos = new BlockPos(x, start.getY() + dy, z);
                    BlockPos abovePos = surfacePos.up();
                    if (world.getBlockState(abovePos).isAir() && world.getBlockState(surfacePos).isSideSolidFullSquare(world, surfacePos, Direction.UP)) {
                        world.setBlockState(abovePos, Blocks.TERMITE_TRAIL.getDefaultState(), Block.NOTIFY_ALL);
                        break;
                    }
                }
            }
        }
    }

    public static boolean isInfectable(BlockState state) {
        if (state.isOf(Blocks.INFECTED_WOOD)) {
            return false;
        }
        return state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.PLANKS);
    }

    public static void infect(ServerWorld world, BlockPos targetPos, BlockState targetState, Random random) {
        infect(world, targetPos, targetState, random, 1);
    }

    public static void infect(ServerWorld world, BlockPos targetPos, BlockState targetState, Random random, int colonySize) {
        int anim = random.nextInt(4);
        world.setBlockState(targetPos, Blocks.INFECTED_WOOD.getDefaultState().with(InfectedWoodBlock.ANIMATION, anim), Block.NOTIFY_ALL);
        if (world.getBlockEntity(targetPos) instanceof InfectedWoodBlockEntity be) {
            be.setOriginalState(targetState);
            // Speed of devouring scales with colony size: 480 ticks down to ~160 ticks (~8s to 24s)
            int life = Math.max(160, 480 - (colonySize * 20) + random.nextInt(60));
            be.setLifeTicks(life);
        }
    }
}
