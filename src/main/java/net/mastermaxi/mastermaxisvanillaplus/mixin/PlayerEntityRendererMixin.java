package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin to ensure custom crossbows get ArmPose.CROSSBOW_HOLD when charged.
 */
@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @Redirect(
        method = "getArmPose(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z")
    )
    private static boolean redirectIsOfInGetArmPose(ItemStack stack, Item item) {
        if (item == Items.CROSSBOW) {
            return stack.getItem() instanceof CrossbowItem;
        }
        return stack.isOf(item);
    }
}
