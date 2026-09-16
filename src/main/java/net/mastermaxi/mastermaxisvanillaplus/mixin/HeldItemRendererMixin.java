package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.mastermaxi.mastermaxisvanillaplus.item.ViewfinderHelper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to ensure all custom bows and crossbows use proper vanilla animations
 * in first-person view instead of floating over the player's head.
 */
@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Inject(
        method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hideItemWhenUsingViewfinder(
        AbstractClientPlayerEntity player,
        float tickDelta,
        float pitch,
        Hand hand,
        float swingProgress,
        ItemStack item,
        float equipProgress,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        int light,
        CallbackInfo ci
    ) {
        if (ViewfinderHelper.isUsingViewfinder(player)) {
            ci.cancel();
        }
    }

    @Redirect(
        method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z")
    )
    private boolean redirectIsOfInRenderFirstPersonItem(ItemStack stack, Item item) {
        if (item == Items.CROSSBOW) {
            return stack.getItem() instanceof CrossbowItem;
        }
        if (item == Items.BOW) {
            return stack.getItem() instanceof BowItem;
        }
        return stack.isOf(item);
    }

    @Redirect(
        method = {
            "getHandRenderType(Lnet/minecraft/client/network/ClientPlayerEntity;)Lnet/minecraft/client/render/item/HeldItemRenderer$HandRenderType;",
            "getUsingItemHandRenderType(Lnet/minecraft/client/network/ClientPlayerEntity;)Lnet/minecraft/client/render/item/HeldItemRenderer$HandRenderType;",
            "isChargedCrossbow(Lnet/minecraft/item/ItemStack;)Z"
        },
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z")
    )
    private static boolean redirectIsOfInStaticMethods(ItemStack stack, Item item) {
        if (item == Items.CROSSBOW) {
            return stack.getItem() instanceof CrossbowItem;
        }
        if (item == Items.BOW) {
            return stack.getItem() instanceof BowItem;
        }
        return stack.isOf(item);
    }
}
