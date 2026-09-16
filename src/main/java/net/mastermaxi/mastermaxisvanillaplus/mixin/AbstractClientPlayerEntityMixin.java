package net.mastermaxi.mastermaxisvanillaplus.mixin;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.item.ViewfinderHelper;
import net.mastermaxi.mastermaxisvanillaplus.item.ViewfinderItem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {
    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "getFovMultiplier(ZF)F", at = @At("HEAD"), cancellable = true)
    private void injectViewfinderFov(boolean firstPerson, float tickDelta, CallbackInfoReturnable<Float> cir) {
        if (firstPerson && ViewfinderHelper.isUsingViewfinder(this)) {
            cir.setReturnValue(ViewfinderItem.FOV_MULTIPLIER);
        }
    }
}
