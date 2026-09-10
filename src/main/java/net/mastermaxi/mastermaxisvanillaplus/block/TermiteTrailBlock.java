package net.mastermaxi.mastermaxisvanillaplus.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class TermiteTrailBlock extends Block {
    private static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    public TermiteTrailBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos downPos = pos.down();
        return world.getBlockState(downPos).isSideSolidFullSquare(world, downPos, Direction.UP);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, net.minecraft.world.tick.ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (!state.canPlaceAt(world, pos)) {
            return net.minecraft.block.Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);

        // Disappear slowly if no infected mounds or infected wood are nearby
        boolean hasSource = false;
        for (BlockPos checkPos : BlockPos.iterateOutwards(pos, 8, 4, 8)) {
            BlockState check = world.getBlockState(checkPos);
            if (check.isOf(Blocks.INFECTED_MOUND) || check.isOf(Blocks.INFECTED_WOOD)) {
                hasSource = true;
                break;
            }
        }
        if (!hasSource) {
            world.removeBlock(pos, false);
        }
    }
}
