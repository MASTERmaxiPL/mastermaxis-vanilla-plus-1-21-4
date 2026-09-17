package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

/**
 * Mixin to ensure vanilla BowItem cannot shoot heavy arrows.
 * Heavy arrows require a Heavy Bow or Heavy Hardened Bow.
 */
@Mixin(BowItem.class)
public class BowItemMixin {

    @Inject(method = "getProjectiles", at = @At("HEAD"), cancellable = true)
    private void restrictHeavyArrowForVanillaBow(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        Object self = this;
        if (self.getClass().getName().equals("net.minecraft.item.BowItem")) {
            cir.setReturnValue(stack -> stack.isIn(ItemTags.ARROWS) && !stack.isOf(ItemInit.HEAVY_ARROW));
        }
    }
}
