package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.item.ViewfinderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(method = "doAttack()Z", at = @At("HEAD"), cancellable = true)
    private void cancelAttackWhenAimingCrossbow(CallbackInfoReturnable<Boolean> cir) {
        if (ViewfinderHelper.isHoldingChargedViewfinderCrossbow(this.player)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "handleBlockBreaking(Z)V", at = @At("HEAD"), cancellable = true)
    private void cancelBlockBreakingWhenAimingCrossbow(boolean breaking, CallbackInfo ci) {
        if (ViewfinderHelper.isHoldingChargedViewfinderCrossbow(this.player)) {
            ci.cancel();
        }
    }
}
