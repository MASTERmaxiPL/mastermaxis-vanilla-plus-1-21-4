package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.mastermaxi.mastermaxisvanillaplus.item.PyrotechnicTubeItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

import net.mastermaxi.mastermaxisvanillaplus.item.PrismarineArrowRestItem;
import net.minecraft.item.BowItem;

@SuppressWarnings({"unused", "ConstantConditions"})
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "getProjectileType", at = @At("HEAD"), cancellable = true)
    private void injectCrossbowProjectileType(ItemStack weapon, CallbackInfoReturnable<ItemStack> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (weapon.getItem() instanceof BowItem) {
            Predicate<ItemStack> predicate = PrismarineArrowRestItem.getBowProjectilePredicate(weapon);

            ItemStack quiverArrow = net.mastermaxi.mastermaxisvanillaplus.item.QuiverHelper.getArrowForShooting(player, predicate);
            if (!quiverArrow.isEmpty()) {
                cir.setReturnValue(quiverArrow);
                return;
            }

            ItemStack held = RangedWeaponItem.getHeldProjectile(player, predicate);
            if (!held.isEmpty()) {
                cir.setReturnValue(held);
                return;
            }

            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (predicate.test(stack)) {
                    cir.setReturnValue(stack);
                    return;
                }
            }

            if (player.getAbilities().creativeMode) {
                cir.setReturnValue(new ItemStack(Items.ARROW));
                return;
            }

            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }

        if (weapon.getItem() instanceof RangedWeaponItem rangedWeapon) {
            ItemStack quiverArrow = net.mastermaxi.mastermaxisvanillaplus.item.QuiverHelper.getArrowForShooting(player, rangedWeapon.getProjectiles());
            if (!quiverArrow.isEmpty()) {
                cir.setReturnValue(quiverArrow);
                return;
            }
        }

        if (weapon.getItem() instanceof CrossbowItem) {
            boolean hasTube = PyrotechnicTubeItem.hasAttachment(weapon);
            Predicate<ItemStack> predicate = PyrotechnicTubeItem.getCrossbowProjectilePredicate(hasTube);

            ItemStack held = RangedWeaponItem.getHeldProjectile(player, predicate);
            if (!held.isEmpty()) {
                cir.setReturnValue(held);
                return;
            }

            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (predicate.test(stack)) {
                    cir.setReturnValue(stack);
                    return;
                }
            }

            if (player.getAbilities().creativeMode) {
                cir.setReturnValue(new ItemStack(ItemInit.BOLT));
                return;
            }

            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}
