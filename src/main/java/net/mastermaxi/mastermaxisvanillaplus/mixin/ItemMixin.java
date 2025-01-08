package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.SnowballItem;
import net.minecraft.util.annotation.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug()
@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    private void modifyMaxCount(CallbackInfoReturnable<Integer> ci) {
        System.out.println("ItemMixin: Snowball stack size modified.");
        if ((Object) this instanceof SnowballItem) {
            ci.setReturnValue(64); // Zmieniamy maksymalny rozmiar stosu na 64
            System.out.println("ItemMixin: Snowball stack size modified to 64.");
        }
    }
}