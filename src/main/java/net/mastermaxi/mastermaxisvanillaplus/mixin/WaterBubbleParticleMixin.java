package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.fluid.MudWaterFluid;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.WaterBubbleParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WaterBubbleParticle.class)
public abstract class WaterBubbleParticleMixin extends SpriteBillboardParticle {
    protected WaterBubbleParticleMixin(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void tintMudBubble(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, CallbackInfo ci) {
        if (mastermaxisvanillaplus$isMudWater(world, x, y, z)) {
            this.setColor(0.36F, 0.23F, 0.12F);
        }
    }

    @Unique
    private static boolean mastermaxisvanillaplus$isMudWater(ClientWorld world, double x, double y, double z) {
        BlockPos pos = BlockPos.ofFloored(x, y, z);
        return world.getFluidState(pos).getFluid() instanceof MudWaterFluid
                || world.getFluidState(pos.down()).getFluid() instanceof MudWaterFluid
                || world.getFluidState(pos.down(2)).getFluid() instanceof MudWaterFluid;
    }
}
