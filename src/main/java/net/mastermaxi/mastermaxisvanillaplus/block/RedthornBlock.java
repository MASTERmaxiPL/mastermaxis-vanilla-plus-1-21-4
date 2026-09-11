package net.mastermaxi.mastermaxisvanillaplus.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class RedthornBlock extends PlantBlock {
    public static final MapCodec<RedthornBlock> CODEC = createCodec(RedthornBlock::new);
    private static final VoxelShape SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    @Override
    public MapCodec<RedthornBlock> getCodec() {
        return CODEC;
    }

    public RedthornBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(Blocks.GRASSY_RED_SOIL)
                || floor.isOf(Blocks.RED_SOIL)
                || floor.isOf(Blocks.LATERITE)
                || floor.isOf(net.minecraft.block.Blocks.COARSE_DIRT);
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getFluidState(pos).isEmpty() && super.canPlaceAt(state, world, pos);
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity && entity.getType() != EntityType.BEE) {
            entity.slowMovement(state, new Vec3d(0.8, 0.75, 0.8));
            if (world instanceof ServerWorld serverWorld) {
                Vec3d movement = entity.isControlledByPlayer() ? entity.getMovement() : entity.getLastRenderPos().subtract(entity.getPos());
                if (movement.horizontalLengthSquared() > 0.0) {
                    double dx = Math.abs(movement.getX());
                    double dz = Math.abs(movement.getZ());
                    if (dx >= 0.003 || dz >= 0.003) {
                        entity.damage(serverWorld, world.getDamageSources().sweetBerryBush(), 1.0F);
                    }
                }
            }
        }
    }

    /**
     * Adds random XZ position offset when rendering (like grass/flowers).
     * The offset is deterministic per block position (based on block hash),
     * so it won't shift around. Max 0.25 blocks in each direction.
     */
    @Override
    protected float getMaxHorizontalModelOffset() {
        return 0.25f;
    }
}
