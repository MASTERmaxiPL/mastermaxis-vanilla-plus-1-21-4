package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.item.QuiverHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(RangedWeaponItem.class)
public abstract class RangedWeaponItemMixin {

    @Inject(method = "load", at = @At("TAIL"))
    private static void onArrowLoaded(ItemStack weapon, ItemStack projectile, LivingEntity shooter, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (shooter instanceof PlayerEntity player && !cir.getReturnValue().isEmpty()) {
            QuiverHelper.onArrowShot(player);
        }
    }
}
