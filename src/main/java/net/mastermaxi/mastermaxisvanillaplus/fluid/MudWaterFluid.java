package net.mastermaxi.mastermaxisvanillaplus.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Optional;

public abstract class MudWaterFluid extends FlowableFluid {
    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == getStill() || fluid == getFlowing();
    }

    @Override
    protected boolean isInfinite(ServerWorld world) {
        return world.getGameRules().getBoolean(GameRules.WATER_SOURCE_CONVERSION);
    }

    @Override
    public Fluid getFlowing() {
        return FluidInit.MUD_WATER_FLOWING;
    }

    @Override
    public Fluid getStill() {
        return FluidInit.MUD_WATER_STILL;
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(state, world, pos, blockEntity);
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !state.isIn(FluidTags.WATER);
    }

    @Override
    protected int getMaxFlowDistance(WorldView world) {
        return 5;
    }

    @Override
    protected int getLevelDecreasePerBlock(WorldView world) {
        return 1;
    }

    @Override
    public int getTickRate(WorldView world) {
        return 5;
    }

    @Override
    protected float getBlastResistance() {
        return 100.0F;
    }

    @Override
    public Item getBucketItem() {
        return FluidInit.MUD_WATER_BUCKET;
    }

    @Override
    public Optional<SoundEvent> getBucketFillSound() {
        return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return FluidInit.MUD_WATER_BLOCK.getDefaultState()
                .with(Properties.LEVEL_15, getBlockStateLevel(state));
    }

    @Override
    public int getLevel(FluidState fluidState) {
        return 0;
    }



    public static class Flowing extends MudWaterFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder){
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }



    public static class Still extends MudWaterFluid {
        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}
