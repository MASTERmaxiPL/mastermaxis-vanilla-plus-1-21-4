package net.mastermaxi.mastermaxisvanillaplus.screen;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import net.mastermaxi.mastermaxisvanillaplus.item.QuiverItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

public class QuiverScreenHandler extends ScreenHandler {

    private final ItemStack quiverStack;
    private final SimpleInventory quiverInventory;
    private final int entityId;

    public QuiverScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ItemStack.EMPTY, -1);
    }

    public QuiverScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack stack) {
        this(syncId, playerInventory, stack, -1);
    }

    public QuiverScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack stack, int entityId) {
        super(ModScreenHandlerTypes.QUIVER, syncId);
        this.entityId = entityId;

        ItemStack target = stack;
        if (target.isEmpty()) {
            AccessoriesCapability cap = AccessoriesCapability.get(playerInventory.player);
            if (cap != null) {
                AccessoriesContainer back = cap.getContainers().get("back");
                if (back != null) {
                    target = back.getAccessories().getStack(0);
                }
            }
        }
        this.quiverStack = target;

        this.quiverInventory = new SimpleInventory(QuiverItem.SLOTS);
        if (!this.quiverStack.isEmpty()) {
            DefaultedList<ItemStack> arrows = QuiverItem.getStoredArrows(this.quiverStack);
            for (int i = 0; i < arrows.size(); i++) {
                this.quiverInventory.setStack(i, arrows.get(i));
            }
        }

        this.quiverInventory.addListener(inv -> {
            if (!this.quiverStack.isEmpty()) {
                DefaultedList<ItemStack> arrows = DefaultedList.ofSize(QuiverItem.SLOTS, ItemStack.EMPTY);
                for (int i = 0; i < QuiverItem.SLOTS; i++) {
                    arrows.set(i, this.quiverInventory.getStack(i));
                }
                QuiverItem.setStoredArrows(this.quiverStack, arrows);

                AccessoriesCapability cap = AccessoriesCapability.get(playerInventory.player);
                if (cap != null) {
                    AccessoriesContainer back = cap.getContainers().get("back");
                    if (back != null) back.markChanged();
                }

                if (this.entityId >= 0) {
                    net.minecraft.entity.Entity e = playerInventory.player.getWorld().getEntityById(this.entityId);
                    if (e instanceof net.mastermaxi.mastermaxisvanillaplus.entity.custom.BackpackEntity bEntity) {
                        bEntity.setBackpackStack(this.quiverStack);
                    }
                }
            }
        });

        // Add 4 arrow slots (shifted 1px left to x={49, 69, 89, 109}, 1px down to y=4)
        int[] slotX = {49, 69, 89, 109};
        for (int i = 0; i < 4; i++) {
            this.addSlot(new Slot(this.quiverInventory, i, slotX[i], 4) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return isArrowOrBolt(stack);
                }
            });
        }

        // Add Player Inventory
        int playerInvY = 29;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 9; c++) {
                this.addSlot(new Slot(playerInventory, c + r * 9 + 9, 8 + c * 18, playerInvY + r * 18));
            }
        }

        for (int c = 0; c < 9; c++) {
            this.addSlot(new Slot(playerInventory, c, 8 + c * 18, playerInvY + 58));
        }
    }

    private static boolean isArrowOrBolt(ItemStack stack) {
        return stack.isIn(ItemTags.ARROWS) || stack.getItem() instanceof net.mastermaxi.mastermaxisvanillaplus.item.BoltItem;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        if (this.entityId >= 0) {
            net.minecraft.entity.Entity e = player.getWorld().getEntityById(this.entityId);
            return e instanceof net.mastermaxi.mastermaxisvanillaplus.entity.custom.BackpackEntity && e.isAlive() && e.squaredDistanceTo(player) <= 64.0;
        }
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (slotIndex < 4) {
                // Moving from Quiver to Player Inventory
                if (!this.insertItem(originalStack, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Moving from Player Inventory to Quiver
                if (isArrowOrBolt(originalStack)) {
                    if (!this.insertItem(originalStack, 0, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, originalStack);
        }

        return newStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        if (player.getWorld().isClient) return;

        if (!this.quiverStack.isEmpty()) {
            DefaultedList<ItemStack> arrows = DefaultedList.ofSize(QuiverItem.SLOTS, ItemStack.EMPTY);
            for (int i = 0; i < QuiverItem.SLOTS; i++) {
                arrows.set(i, this.quiverInventory.getStack(i));
            }
            QuiverItem.setStoredArrows(this.quiverStack, arrows);

            AccessoriesCapability cap = AccessoriesCapability.get(player);
            if (cap != null) {
                AccessoriesContainer back = cap.getContainers().get("back");
                if (back != null) back.markChanged();
            }

            if (this.entityId >= 0) {
                net.minecraft.entity.Entity e = player.getWorld().getEntityById(this.entityId);
                if (e instanceof net.mastermaxi.mastermaxisvanillaplus.entity.custom.BackpackEntity bEntity) {
                    bEntity.setBackpackStack(this.quiverStack);
                }
            }
        }
    }
}
