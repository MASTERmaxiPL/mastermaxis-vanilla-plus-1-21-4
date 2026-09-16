package net.mastermaxi.mastermaxisvanillaplus.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;

public class ViewfinderHelper {

    public static boolean isHoldingViewfinderCrossbow(PlayerEntity player) {
        if (player == null) {
            return false;
        }
        return isViewfinderCrossbow(player.getMainHandStack()) || isViewfinderCrossbow(player.getOffHandStack());
    }

    public static boolean isHoldingChargedViewfinderCrossbow(PlayerEntity player) {
        if (player == null) {
            return false;
        }
        ItemStack main = player.getMainHandStack();
        if (isViewfinderCrossbow(main) && CrossbowItem.isCharged(main)) {
            return true;
        }
        ItemStack off = player.getOffHandStack();
        return isViewfinderCrossbow(off) && CrossbowItem.isCharged(off);
    }

    public static boolean isViewfinderCrossbow(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ViewfinderScopeItem && stack.getItem() instanceof CrossbowItem;
    }

    public static boolean isUsingViewfinder(PlayerEntity player) {
        if (player == null) {
            return false;
        }
        // 1. Standalone ViewfinderItem (used via RMB like spyglass)
        if (player.isUsingItem() && player.getActiveItem().getItem() instanceof ViewfinderItem) {
            return true;
        }
        // 2. Crossbow with viewfinder:
        // When charged: Left Mouse Button (attackKey) aims/scopes!
        if (isHoldingChargedViewfinderCrossbow(player)) {
            if (isAttackKeyPressed()) {
                return true;
            }
        }
        // Sneaking also allows scoping as a secondary option
        return player.isSneaking() && isHoldingViewfinderCrossbow(player);
    }

    private static boolean isAttackKeyPressed() {
        try {
            return isAttackKeyPressedClient();
        } catch (Throwable ignored) {
            return false;
        }
    }

    @Environment(EnvType.CLIENT)
    private static boolean isAttackKeyPressedClient() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client != null && client.options != null && client.options.attackKey.isPressed();
    }
}
