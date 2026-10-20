package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.item.BackpackItem;
import net.mastermaxi.mastermaxisvanillaplus.item.QuiverItem;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EquippableComponent.class)
public abstract class EquippableComponentMixin {

    @Shadow public abstract EquipmentSlot slot();

    @Inject(method = "equip", at = @At("HEAD"), cancellable = true)
    private void blockChestEquipForBackItems(ItemStack stack, PlayerEntity player, CallbackInfoReturnable<ActionResult> cir) {
        if (this.slot() == EquipmentSlot.CHEST) {
            if (stack.isOf(Items.ELYTRA) || stack.getItem() instanceof BackpackItem || stack.getItem() instanceof QuiverItem) {
                cir.setReturnValue(ActionResult.PASS);
            }
        }
    }
}
