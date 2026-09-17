package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.item.BoltItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

/**
 * Mixin to ensure all crossbows (vanilla and modded) only accept bolts as ammunition,
 * completely replacing arrows for crossbow shooting.
 */
@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Inject(method = "getHeldProjectiles", at = @At("HEAD"), cancellable = true)
    private void injectHeldProjectilesToBolts(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        cir.setReturnValue(stack -> stack.getItem() instanceof BoltItem);
    }

    @Inject(method = "getProjectiles", at = @At("HEAD"), cancellable = true)
    private void injectProjectilesToBolts(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        cir.setReturnValue(stack -> stack.getItem() instanceof BoltItem);
    }
}
