package net.mastermaxi.mastermaxisvanillaplus.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

public class GrassyRedSoilBlock extends Block implements Fertilizable {
    public static final MapCodec<GrassyRedSoilBlock> CODEC = createCodec(GrassyRedSoilBlock::new);

    @Override
    public MapCodec<GrassyRedSoilBlock> getCodec() {
        return CODEC;
    }

    public GrassyRedSoilBlock(Settings settings) {
        super(settings);
    }

    private static boolean canSurvive(BlockState state, WorldView world, BlockPos pos) {
        BlockPos upPos = pos.up();
        BlockState upState = world.getBlockState(upPos);
        if (upState.isOf(net.minecraft.block.Blocks.SNOW) && upState.get(SnowBlock.LAYERS) == 1) {
            return true;
        }
        if (upState.getFluidState().getLevel() == 8) {
            return false;
        }
        int opacity = ChunkLightProvider.getRealisticOpacity(state, upState, Direction.UP, upState.getOpacity());
        return opacity < 15;
    }

    private static boolean canSpread(BlockState state, WorldView world, BlockPos pos) {
        BlockPos upPos = pos.up();
        return canSurvive(state, world, pos) && !world.getFluidState(upPos).isIn(FluidTags.WATER);
    }

    private static boolean isAllowedPlant(BlockState state) {
        return state.isOf(net.minecraft.block.Blocks.SHORT_GRASS)
                || state.isOf(net.minecraft.block.Blocks.TALL_GRASS)
                || state.isOf(net.minecraft.block.Blocks.FERN)
                || state.isOf(net.minecraft.block.Blocks.LARGE_FERN)
                || state.isOf(Blocks.REDTHORN)
                || state.isOf(net.minecraft.block.Blocks.ACACIA_SAPLING)
                || state.isOf(net.minecraft.block.Blocks.DEAD_BUSH)
                || state.isOf(net.minecraft.block.Blocks.CACTUS)
                || state.isOf(net.minecraft.block.Blocks.MELON_STEM)
                || state.isOf(net.minecraft.block.Blocks.ATTACHED_MELON_STEM)
                || state.isOf(net.minecraft.block.Blocks.MELON);
    }

    private static boolean isPlant(BlockState state) {
        return state.getBlock() instanceof PlantBlock
                || state.getBlock() instanceof CropBlock
                || state.isIn(BlockTags.CROPS)
                || state.isIn(BlockTags.SAPLINGS)
                || state.isIn(BlockTags.FLOWERS);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // 1. Check if grass survives or turns back to red_soil
        if (!canSurvive(state, world, pos)) {
            world.setBlockState(pos, Blocks.RED_SOIL.getDefaultState());
            return;
        }

        // 2. Check plant above: if unauthorized plant, wither it into dead bush!
        BlockPos upPos = pos.up();
        BlockState upState = world.getBlockState(upPos);
        if (isPlant(upState) && !isAllowedPlant(upState)) {
            if (upState.getBlock() instanceof TallPlantBlock) {
                BlockPos abovePos = upPos.up();
                if (world.getBlockState(abovePos).isOf(upState.getBlock())) {
                    world.removeBlock(abovePos, false);
                }
            }
            world.setBlockState(upPos, net.minecraft.block.Blocks.DEAD_BUSH.getDefaultState());
            world.playSound(null, upPos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.7F, 0.8F);
            world.spawnParticles(ParticleTypes.SMOKE, upPos.getX() + 0.5, upPos.getY() + 0.3, upPos.getZ() + 0.5, 5, 0.1, 0.1, 0.1, 0.02);
        }

        // 3. Spreading to nearby red_soil
        if (world.getLightLevel(pos.up()) >= 9) {
            BlockState defaultState = this.getDefaultState();
            for (int i = 0; i < 4; i++) {
                BlockPos targetPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                if (world.getBlockState(targetPos).isOf(Blocks.RED_SOIL) && canSpread(defaultState, world, targetPos)) {
                    world.setBlockState(targetPos, defaultState);
                }
            }
        }
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.up()).isAir();
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos upPos = pos.up();
        outer:
        for (int i = 0; i < 128; i++) {
            BlockPos targetPos = upPos;
            for (int j = 0; j < i / 16; j++) {
                targetPos = targetPos.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!world.getBlockState(targetPos.down()).isOf(this) || world.getBlockState(targetPos).isFullCube(world, targetPos)) {
                    continue outer;
                }
            }
            BlockState targetState = world.getBlockState(targetPos);
            if (targetState.isAir()) {
                BlockState plantState;
                int r = random.nextInt(8);
                if (r == 0) {
                    plantState = Blocks.REDTHORN.getDefaultState();
                } else if (r == 1) {
                    plantState = net.minecraft.block.Blocks.DEAD_BUSH.getDefaultState();
                } else {
                    plantState = net.minecraft.block.Blocks.SHORT_GRASS.getDefaultState();
                }
                if (plantState.canPlaceAt(world, targetPos)) {
                    world.setBlockState(targetPos, plantState, 3);
                }
            }
        }
    }
}
