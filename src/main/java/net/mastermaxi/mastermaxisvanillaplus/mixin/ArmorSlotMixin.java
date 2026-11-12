package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.item.BackpackItem;
import net.mastermaxi.mastermaxisvanillaplus.item.QuiverItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.screen.slot.ArmorSlot")
public abstract class ArmorSlotMixin {

    @Shadow @Final private EquipmentSlot equipmentSlot;

    @Inject(method = "canInsert", at = @At("HEAD"), cancellable = true)
    private void preventBackSlotItemsInChestplateSlot(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (this.equipmentSlot == EquipmentSlot.CHEST) {
            if (stack.isOf(Items.ELYTRA) || stack.getItem() instanceof BackpackItem || stack.getItem() instanceof QuiverItem) {
                cir.setReturnValue(false);
            }
        }
    }
}
