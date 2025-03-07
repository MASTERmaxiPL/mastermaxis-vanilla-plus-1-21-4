package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
    @Inject(method = "isSameFluid", at = @At("HEAD"), cancellable = true)
    private static void treatWaterAndMudWaterAsSame(FluidState a, FluidState b, CallbackInfoReturnable<Boolean> cir) {
        if (!a.getFluid().matchesType(b.getFluid()) && a.isIn(FluidTags.WATER) && b.isIn(FluidTags.WATER)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
        method = "getFluidHeight(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/fluid/Fluid;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)F",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getWaterMudWaterHeight(BlockRenderView world, Fluid fluid, BlockPos pos, BlockState state, FluidState fluidState, CallbackInfoReturnable<Float> cir) {
        if (!fluid.matchesType(fluidState.getFluid()) && fluid.getDefaultState().isIn(FluidTags.WATER) && fluidState.isIn(FluidTags.WATER)) {
            BlockState upState = world.getBlockState(pos.up());
            if (upState.getFluidState().isIn(FluidTags.WATER)) {
                cir.setReturnValue(1.0F);
            } else {
                cir.setReturnValue(fluidState.getHeight(world, pos));
            }
        }
    }
}
