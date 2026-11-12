package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

/**
 * Mixin to ensure vanilla BowItem cannot shoot heavy arrows.
 * Heavy arrows require a Heavy Bow or Heavy Hardened Bow.
 */
@SuppressWarnings("unused")
@Mixin(BowItem.class)
public class BowItemMixin {

    @Inject(method = "getProjectiles", at = @At("HEAD"), cancellable = true)
    private void restrictArrowsForVanillaBow(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        Object self = this;
        if (self.getClass().getName().equals("net.minecraft.item.BowItem")) {
            cir.setReturnValue(net.mastermaxi.mastermaxisvanillaplus.item.PrismarineArrowRestItem.VANILLA_BOW_PROJECTILES);
        }
    }
}
