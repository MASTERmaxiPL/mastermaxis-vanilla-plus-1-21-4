package net.mastermaxi.mastermaxisvanillaplus.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class InfectedWoodBlock extends BlockWithEntity {
    public static final MapCodec<InfectedWoodBlock> CODEC = createCodec(InfectedWoodBlock::new);
    public static final IntProperty ANIMATION = IntProperty.of("animation", 0, 3);

    public InfectedWoodBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(ANIMATION, 0));
    }

    @Override
    protected MapCodec<InfectedWoodBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ANIMATION);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InfectedWoodBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, BlockEntities.INFECTED_WOOD, InfectedWoodBlockEntity::tick);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player != null && !player.isCreative()) {
            if (world.getBlockEntity(pos) instanceof InfectedWoodBlockEntity be && be.getOriginalState() != null) {
                dropStack(world, pos, new ItemStack(be.getOriginalState().getBlock()));
            }
        }
        return super.onBreak(world, pos, state, player);
    }
}
