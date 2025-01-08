package net.mastermaxi.mastermaxisvanillaplus.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class MudBlock extends Block {
    private final FlowableFluid customLiquid; // Reference to your custom fluid.

    public MudBlock(Settings settings, FlowableFluid customLiquid) {
        super(settings.ticksRandomly());
        this.customLiquid = customLiquid;
    }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state,
            WorldView world,
            ScheduledTickView tickView,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            Random random
    ) {
        super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
        tryConvertWater(world, pos);
        return state;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, net.minecraft.entity.LivingEntity placer, net.minecraft.item.ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        tryConvertWater(world, pos); // Check when the block is placed.
    }

    private void tryConvertWater(WorldView worldView, BlockPos pos) {
        if (worldView instanceof World world) {
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.offset(direction);
                if (world.getBlockState(neighborPos).getBlock() == Blocks.WATER) { // Find water nearby
                    world.setBlockState(neighborPos, this.customLiquid.getDefaultState().getBlockState()); // Replace water with custom liquid
                }
            }
        }
    }
}
