package net.mastermaxi.mastermaxisvanillaplus.screen;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import net.mastermaxi.mastermaxisvanillaplus.item.BackpackItem;
import net.mastermaxi.mastermaxisvanillaplus.item.QuiverItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class BackSlot extends Slot {
    public static final TagKey<net.minecraft.item.Item> BACK_TAG = TagKey.of(RegistryKeys.ITEM, Identifier.of("accessories", "back"));
    private final PlayerEntity player;

    public BackSlot(PlayerEntity player, int x, int y) {
        super(new SimpleInventory(1), 0, x, y);
        this.player = player;
    }

    private AccessoriesContainer getBackContainer() {
        AccessoriesCapability cap = AccessoriesCapability.get(player);
        return cap != null ? cap.getContainers().get("back") : null;
    }

    @Override
    public ItemStack getStack() {
        AccessoriesContainer back = getBackContainer();
        return back != null ? back.getAccessories().getStack(0) : ItemStack.EMPTY;
    }

    @Override
    public void setStack(ItemStack stack) {
        setStackNoCallbacks(stack);
        markDirty();
    }

    @Override
    public void setStackNoCallbacks(ItemStack stack) {
        AccessoriesContainer back = getBackContainer();
        if (back != null) {
            ItemStack oldStack = back.getAccessories().getStack(0);

            // Swap dropping logic: if old item was backpack/quiver and new item is backpack/quiver
            if (!player.getWorld().isClient && !oldStack.isEmpty() && !stack.isEmpty() && oldStack != stack) {
                if (oldStack.getItem() instanceof BackpackItem oldBackpack && (stack.getItem() instanceof BackpackItem || stack.getItem() instanceof QuiverItem)) {
                    oldBackpack.dropStoredItems(player.getWorld(), player, oldStack);
                } else if (oldStack.getItem() instanceof QuiverItem oldQuiver && (stack.getItem() instanceof BackpackItem || stack.getItem() instanceof QuiverItem)) {
                    oldQuiver.dropStoredArrows(player.getWorld(), player, oldStack);
                }
            }

            back.getAccessories().setStack(0, stack);
            back.markChanged();
        }
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isIn(BACK_TAG);
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return 1;
    }

    @Override
    public ItemStack takeStack(int amount) {
        AccessoriesContainer back = getBackContainer();
        if (back != null) {
            ItemStack taken = back.getAccessories().removeStack(0, amount);
            if (!taken.isEmpty()) {
                markDirty();
            }
            return taken;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public void markDirty() {
        AccessoriesContainer back = getBackContainer();
        if (back != null) {
            back.markChanged();
        }
    }
}
