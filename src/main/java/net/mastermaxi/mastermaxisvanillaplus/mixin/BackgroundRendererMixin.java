package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.fluid.FluidInit;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Unique
    private static final Vector4f MUD_FOG_COLOR = new Vector4f(0.24F, 0.15F, 0.08F, 1.0F);

    @Inject(method = "getFogColor", at = @At("HEAD"), cancellable = true)
    private static void getMudWaterFogColor(Camera camera, float tickDelta, ClientWorld world, int clampedViewDistance, float skyDarkness, CallbackInfoReturnable<Vector4f> cir) {
        if (camera.getSubmersionType() == CameraSubmersionType.WATER) {
            BlockPos cameraPos = BlockPos.ofFloored(camera.getPos());
            FluidState fluidState = world.getFluidState(cameraPos);
            if (fluidState.isOf(FluidInit.MUD_WATER_STILL) || fluidState.isOf(FluidInit.MUD_WATER_FLOWING)) {
                cir.setReturnValue(new Vector4f(MUD_FOG_COLOR));
            }
        }
    }

    @Inject(method = "applyFog", at = @At("RETURN"), cancellable = true)
    private static void applyMudWaterFog(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable<Fog> cir) {
        if (camera.getSubmersionType() == CameraSubmersionType.WATER) {
            Entity entity = camera.getFocusedEntity();
            if (entity != null && entity.getWorld() != null) {
                BlockPos cameraPos = BlockPos.ofFloored(camera.getPos());
                FluidState fluidState = entity.getWorld().getFluidState(cameraPos);
                if (fluidState.isOf(FluidInit.MUD_WATER_STILL) || fluidState.isOf(FluidInit.MUD_WATER_FLOWING)) {
                    cir.setReturnValue(new Fog(-1.0F, 10.0F, FogShape.SPHERE, 0.24F, 0.15F, 0.08F, 1.0F));
                }
            }
        }
    }
}
