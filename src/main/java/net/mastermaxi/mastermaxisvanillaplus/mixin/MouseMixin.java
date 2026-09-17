package net.mastermaxi.mastermaxisvanillaplus.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.item.ViewfinderHelper;
import net.mastermaxi.mastermaxisvanillaplus.item.ViewfinderItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @WrapOperation(
            method = "updateMouse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"
            )
    )
    private void slowDownViewfinderCamera(ClientPlayerEntity player, double dx, double dy, Operation<Void> original) {
        if (this.client.options.getPerspective().isFirstPerson() && ViewfinderHelper.isUsingViewfinder(player)) {
            dx *= ViewfinderItem.FOV_MULTIPLIER;
            dy *= ViewfinderItem.FOV_MULTIPLIER;
        }
        original.call(player, dx, dy);
    }
}
