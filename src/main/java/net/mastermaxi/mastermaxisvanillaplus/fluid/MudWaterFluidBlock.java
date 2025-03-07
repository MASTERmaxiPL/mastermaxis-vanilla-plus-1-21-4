package net.mastermaxi.mastermaxisvanillaplus.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.entity.Entity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class MudWaterFluidBlock extends FluidBlock {
    public MudWaterFluidBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (world.isClient() && entity.getVelocity().horizontalLengthSquared() > 0.003) {
            if (world.random.nextInt(3) == 0) {
                world.addParticle(
                        new BlockStateParticleEffect(ParticleTypes.BLOCK, net.minecraft.block.Blocks.MUD.getDefaultState()),
                        entity.getX() + (world.random.nextDouble() - 0.5) * entity.getWidth(),
                        entity.getY() + 0.1,
                        entity.getZ() + (world.random.nextDouble() - 0.5) * entity.getWidth(),
                        (world.random.nextDouble() - 0.5) * 0.1,
                        0.08 + world.random.nextDouble() * 0.1,
                        (world.random.nextDouble() - 0.5) * 0.1
                );
            }
        }
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        convertSurroundingBlocks(world, pos);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
        convertSurroundingBlocks(world, pos);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(
            BlockState state,
            WorldView world,
            ScheduledTickView tickView,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            Random random
    ) {
        if (world instanceof World w) {
            convertSurroundingBlocks(w, pos);
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    public static void convertSurroundingBlocks(World world, BlockPos pos) {
        if (world.isClient()) {
            return;
        }
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            BlockState neighborState = world.getBlockState(neighborPos);
            if (isConvertible(neighborState)) {
                world.setBlockState(neighborPos, net.mastermaxi.mastermaxisvanillaplus.block.Blocks.MUD_BLOCK.getDefaultState(), Block.NOTIFY_ALL);
                world.playSound(null, neighborPos, SoundEvents.BLOCK_MUD_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
            }
        }
    }

    private static boolean isConvertible(BlockState state) {
        return state.isIn(BlockTags.SAND) || (state.isIn(BlockTags.DIRT) && !state.isOf(net.mastermaxi.mastermaxisvanillaplus.block.Blocks.MUD_BLOCK));
    }
}
