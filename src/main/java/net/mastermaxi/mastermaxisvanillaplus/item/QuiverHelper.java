package net.mastermaxi.mastermaxisvanillaplus.item;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.function.Predicate;

public class QuiverHelper {

    private record ArrowTracking(ItemStack arrow, int index, boolean fromBackpack) {}
    private static final java.util.Map<java.util.UUID, ArrowTracking> PENDING_SHOTS = new java.util.concurrent.ConcurrentHashMap<>();

    public static ItemStack getEquippedQuiver(PlayerEntity player) {
        AccessoriesCapability cap = AccessoriesCapability.get(player);
        if (cap == null) return ItemStack.EMPTY;

        AccessoriesContainer back = cap.getContainers().get("back");
        if (back == null) return ItemStack.EMPTY;

        ItemStack stack = back.getAccessories().getStack(0);
        if (stack.getItem() instanceof QuiverItem) {
            return stack;
        } else if (stack.getItem() instanceof BackpackItem backpack && backpack.getTier().hasQuiver) {
            return stack;
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getArrowForShooting(PlayerEntity player, Predicate<ItemStack> predicate) {
        ItemStack quiver = getEquippedQuiver(player);
        if (quiver.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (quiver.getItem() instanceof QuiverItem) {
            DefaultedList<ItemStack> arrows = QuiverItem.getStoredArrows(quiver);
            for (int i = 0; i < arrows.size(); i++) {
                ItemStack arrow = arrows.get(i);
                if (!arrow.isEmpty() && predicate.test(arrow)) {
                    PENDING_SHOTS.put(player.getUuid(), new ArrowTracking(arrow, i, false));
                    return arrow;
                }
            }
        } else if (quiver.getItem() instanceof BackpackItem backpack && backpack.getTier().hasQuiver) {
            DefaultedList<ItemStack> arrows = backpack.getStoredQuiverArrows(quiver);
            for (int i = 0; i < arrows.size(); i++) {
                ItemStack arrow = arrows.get(i);
                if (!arrow.isEmpty() && predicate.test(arrow)) {
                    PENDING_SHOTS.put(player.getUuid(), new ArrowTracking(arrow, i, true));
                    return arrow;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    public static void onArrowShot(PlayerEntity player) {
        ArrowTracking tracking = PENDING_SHOTS.remove(player.getUuid());
        if (tracking == null || tracking.arrow().isEmpty() || tracking.index() < 0) return;
        if (player.getAbilities().creativeMode) return;

        ItemStack quiver = getEquippedQuiver(player);
        if (quiver.isEmpty()) {
            return;
        }

        AccessoriesCapability cap = AccessoriesCapability.get(player);
        AccessoriesContainer back = cap != null ? cap.getContainers().get("back") : null;

        if (tracking.fromBackpack() && quiver.getItem() instanceof BackpackItem backpack) {
            DefaultedList<ItemStack> arrows = backpack.getStoredQuiverArrows(quiver);
            if (tracking.index() < arrows.size()) {
                ItemStack current = arrows.get(tracking.index());
                if (!current.isEmpty()) {
                    current.decrement(1);
                    if (current.isEmpty()) {
                        arrows.set(tracking.index(), ItemStack.EMPTY);
                    }
                }
                backpack.setStoredQuiverArrows(quiver, arrows);
                if (back != null) back.markChanged();
            }
        } else if (!tracking.fromBackpack() && quiver.getItem() instanceof QuiverItem) {
            DefaultedList<ItemStack> arrows = QuiverItem.getStoredArrows(quiver);
            if (tracking.index() < arrows.size()) {
                ItemStack current = arrows.get(tracking.index());
                if (!current.isEmpty()) {
                    current.decrement(1);
                    if (current.isEmpty()) {
                        arrows.set(tracking.index(), ItemStack.EMPTY);
                    }
                }
                QuiverItem.setStoredArrows(quiver, arrows);
                if (back != null) back.markChanged();
            }
        }
    }

    public static int getEquippedQuiverTotalArrows(PlayerEntity player) {
        ItemStack quiver = getEquippedQuiver(player);
        if (quiver.isEmpty()) return 0;

        if (quiver.getItem() instanceof QuiverItem) {
            return QuiverItem.getTotalArrows(quiver);
        } else if (quiver.getItem() instanceof BackpackItem backpack && backpack.getTier().hasQuiver) {
            DefaultedList<ItemStack> arrows = backpack.getStoredQuiverArrows(quiver);
            int total = 0;
            for (ItemStack a : arrows) {
                if (!a.isEmpty()) total += a.getCount();
            }
            return total;
        }
        return 0;
    }

    public static ItemStack getEquippedQuiverFirstArrow(PlayerEntity player) {
        ItemStack quiver = getEquippedQuiver(player);
        if (quiver.isEmpty()) return ItemStack.EMPTY;

        if (quiver.getItem() instanceof QuiverItem) {
            return QuiverItem.peekFirstArrow(quiver);
        } else if (quiver.getItem() instanceof BackpackItem backpack && backpack.getTier().hasQuiver) {
            DefaultedList<ItemStack> arrows = backpack.getStoredQuiverArrows(quiver);
            for (ItemStack a : arrows) {
                if (!a.isEmpty()) return a;
            }
        }
        return ItemStack.EMPTY;
    }
}
