package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin {
    @Inject(method = "isSideInvisible", at = @At("HEAD"), cancellable = true)
    private void hideWaterMudWaterBoundary(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (!state.getFluidState().getFluid().matchesType(stateFrom.getFluidState().getFluid())
                && state.getFluidState().isIn(FluidTags.WATER) && stateFrom.getFluidState().isIn(FluidTags.WATER)) {
            cir.setReturnValue(true);
        }
    }
}
