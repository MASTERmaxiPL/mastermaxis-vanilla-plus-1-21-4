package net.mastermaxi.mastermaxisvanillaplus.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class MoundBlock extends Block {
    public MoundBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        if (player.getMainHandStack().isOf(Items.SHEARS)) {
            // Base speed 4.8F / 0.8F (hardness) / 30 = 0.2F delta per tick (5 ticks = 0.25s)
            float normalSpeed = player.getBlockBreakingSpeed(state);
            float totalSpeed = 4.8F * normalSpeed;
            float hardness = state.getHardness(world, pos);
            if (hardness <= 0.0F) {
                return 0.0F;
            }
            return totalSpeed / hardness / 30.0F;
        }
        return super.calcBlockBreakingDelta(state, player, world, pos);
    }
}
