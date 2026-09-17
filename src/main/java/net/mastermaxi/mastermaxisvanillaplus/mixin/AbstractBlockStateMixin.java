package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow
    public abstract boolean isOf(Block block);

    @Shadow
    protected abstract BlockState asBlockState();

    @Inject(method = "onStateReplaced", at = @At("HEAD"))
    private void onFletchingTableReplaced(World world, BlockPos pos, BlockState state, boolean moved, CallbackInfo ci) {
        if (this.isOf(Blocks.FLETCHING_TABLE)) {
            ItemScatterer.onStateReplaced(this.asBlockState(), state, world, pos);
        }
    }
}
