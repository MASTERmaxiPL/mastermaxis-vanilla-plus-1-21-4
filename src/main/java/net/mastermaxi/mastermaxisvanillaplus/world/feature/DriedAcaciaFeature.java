package net.mastermaxi.mastermaxisvanillaplus.world.feature;

import com.mojang.serialization.Codec;
import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class DriedAcaciaFeature extends Feature<DefaultFeatureConfig> {
    public DriedAcaciaFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();

        // Search downwards to find solid ground
        while (origin.getY() > world.getBottomY()
                && (world.isAir(origin) || world.getBlockState(origin).isReplaceable() || !world.getFluidState(origin).isEmpty())) {
            origin = origin.down();
        }

        if (origin.getY() <= world.getBottomY() + 4) {
            return false;
        }

        BlockState ground = world.getBlockState(origin);
        if (!ground.isOf(Blocks.LATERITE) && !ground.isOf(Blocks.MUDSTONE_BLOCK) && !ground.isIn(BlockTags.DIRT)) {
            return false;
        }

        BlockPos trunkBase = origin.up();
        if (!world.isAir(trunkBase) || !world.getFluidState(trunkBase).isEmpty()) {
            return false;
        }

        BlockState logY = net.minecraft.block.Blocks.ACACIA_LOG.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y);
        BlockState logX = net.minecraft.block.Blocks.ACACIA_LOG.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.X);
        BlockState logZ = net.minecraft.block.Blocks.ACACIA_LOG.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Z);

        // ~20% chance: Weathered snapped trunk / broken stump
        boolean isSnappedStump = random.nextFloat() < 0.20F;
        if (isSnappedStump) {
            int stumpHeight = 2 + random.nextInt(2);
            for (int dy = 0; dy < stumpHeight; dy++) {
                BlockPos pos = trunkBase.up(dy);
                if (world.isAir(pos) || world.getBlockState(pos).isReplaceable()) {
                    world.setBlockState(pos, logY, Block.NOTIFY_LISTENERS);
                }
            }
            // 50% chance for a single small broken remnant branch
            if (random.nextBoolean()) {
                Direction dir = Direction.Type.HORIZONTAL.random(random);
                BlockPos branch = trunkBase.up(stumpHeight - 1).offset(dir);
                if (world.isAir(branch)) {
                    BlockState branchLog = (dir.getAxis() == Direction.Axis.X) ? logX : logZ;
                    world.setBlockState(branch, branchLog, Block.NOTIFY_LISTENERS);
                }
            }
            return true;
        }

        // Full dried acacia tree skeleton without leaves
        int trunkHeight = 3 + random.nextInt(3); // 3 to 5 blocks
        for (int dy = 0; dy < trunkHeight; dy++) {
            BlockPos pos = trunkBase.up(dy);
            if (world.isAir(pos) || world.getBlockState(pos).isReplaceable()) {
                world.setBlockState(pos, logY, Block.NOTIFY_LISTENERS);
            }
        }

        // Generate 2 to 3 dead spreading branches
        Direction dir1 = Direction.Type.HORIZONTAL.random(random);
        Direction dir2 = random.nextBoolean() ? dir1.getOpposite() : dir1.rotateYClockwise();

        generateBranch(world, random, trunkBase.up(trunkHeight - 1), dir1, logX, logY, logZ);
        generateBranch(world, random, trunkBase.up(trunkHeight - 1), dir2, logX, logY, logZ);

        if (random.nextFloat() < 0.30F) {
            Direction dir3 = dir1.rotateYCounterclockwise();
            generateBranch(world, random, trunkBase.up(trunkHeight - 1), dir3, logX, logY, logZ);
        }

        return true;
    }

    private void generateBranch(
            StructureWorldAccess world,
            Random random,
            BlockPos startPos,
            Direction direction,
            BlockState logX,
            BlockState logY,
            BlockState logZ
    ) {
        int branchLength = 2 + random.nextInt(3); // 2 to 4 blocks
        BlockPos currentPos = startPos;

        for (int step = 0; step < branchLength; step++) {
            currentPos = currentPos.offset(direction);
            // Rise up gradually to simulate umbrella canopy curvature
            if (step % 2 == 1 && step < branchLength - 1) {
                currentPos = currentPos.up();
            }

            if (world.isAir(currentPos) || world.getBlockState(currentPos).isReplaceable()) {
                BlockState log = (direction.getAxis() == Direction.Axis.X) ? logX : logZ;
                world.setBlockState(currentPos, log, Block.NOTIFY_LISTENERS);
            }
        }

        // Dead tips / twigs extending out from branch end
        BlockPos tipUp = currentPos.up();
        if (world.isAir(tipUp)) {
            world.setBlockState(tipUp, logY, Block.NOTIFY_LISTENERS);
        }

        if (random.nextBoolean()) {
            Direction sideDir = random.nextBoolean() ? direction.rotateYClockwise() : direction.rotateYCounterclockwise();
            BlockPos tipSide = currentPos.offset(sideDir);
            if (world.isAir(tipSide)) {
                BlockState sideLog = (sideDir.getAxis() == Direction.Axis.X) ? logX : logZ;
                world.setBlockState(tipSide, sideLog, Block.NOTIFY_LISTENERS);
            }
        }
    }
}
