package net.mastermaxi.mastermaxisvanillaplus.screen;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.BackpackEntity;
import net.mastermaxi.mastermaxisvanillaplus.item.BackpackItem;
import net.mastermaxi.mastermaxisvanillaplus.network.BackpackPayload;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class BackpackScreenHandler extends ScreenHandler {

    private final BackpackItem.BackpackTier tier;
    private final int entityId;
    private final ItemStack backpackStack;
    private final Inventory storageInventory;
    private final Inventory upgradeInventory;
    private final Inventory quiverInventory;

    public static final TagKey<net.minecraft.item.Item> BACKPACK_UPGRADES_TAG = TagKey.of(RegistryKeys.ITEM, Identifier.of("mastermaxisvanillaplus", "backpack_upgrades"));

    public static boolean isAllowedUpgrade(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.isOf(Items.CRAFTING_TABLE) || stack.isIn(BACKPACK_UPGRADES_TAG);
    }

    // Client-side constructor called by ExtendedScreenHandlerType
    public BackpackScreenHandler(int syncId, PlayerInventory playerInventory, BackpackPayload payload) {
        this(syncId, playerInventory, ItemStack.EMPTY, BackpackItem.BackpackTier.values()[payload.tierOrdinal()], payload.entityId());
    }

    // Server-side / Client constructor
    public BackpackScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack stack, BackpackItem.BackpackTier tier, int entityId) {
        super(ModScreenHandlerTypes.BACKPACK, syncId);
        this.tier = tier;
        this.entityId = entityId;

        ItemStack targetStack = stack;
        if (targetStack.isEmpty() && entityId >= 0 && playerInventory.player.getWorld() != null) {
            Entity e = playerInventory.player.getWorld().getEntityById(entityId);
            if (e instanceof BackpackEntity backpackEntity) {
                targetStack = backpackEntity.getBackpackStack();
            }
        }
        if (targetStack.isEmpty()) {
            AccessoriesCapability cap = AccessoriesCapability.get(playerInventory.player);
            if (cap != null) {
                AccessoriesContainer back = cap.getContainers().get("back");
                if (back != null) {
                    targetStack = back.getAccessories().getStack(0);
                }
            }
        }
        this.backpackStack = targetStack;

        // Initialize Inventories
        if (this.tier == BackpackItem.BackpackTier.ENDER) {
            this.storageInventory = playerInventory.player.getEnderChestInventory();
        } else {
            this.storageInventory = new SimpleInventory(this.tier.inventorySlots);
            if (this.backpackStack.getItem() instanceof BackpackItem item) {
                DefaultedList<ItemStack> items = item.getStoredItems(this.backpackStack);
                for (int i = 0; i < items.size(); i++) {
                    this.storageInventory.setStack(i, items.get(i));
                }
            }
        }

        this.upgradeInventory = new SimpleInventory(this.tier.upgradeSlots);
        if (this.backpackStack.getItem() instanceof BackpackItem item) {
            DefaultedList<ItemStack> upgrades = item.getStoredUpgrades(this.backpackStack);
            for (int i = 0; i < upgrades.size(); i++) {
                this.upgradeInventory.setStack(i, upgrades.get(i));
            }
        }

        if (this.tier.hasQuiver) {
            this.quiverInventory = new SimpleInventory(3);
            if (this.backpackStack.getItem() instanceof BackpackItem item) {
                DefaultedList<ItemStack> arrows = item.getStoredQuiverArrows(this.backpackStack);
                for (int i = 0; i < arrows.size(); i++) {
                    this.quiverInventory.setStack(i, arrows.get(i));
                }
            }
        } else {
            this.quiverInventory = null;
        }

        // Add Storage Slots
        int rows = this.tier.inventorySlots / 9;
        int storageStartY = this.tier == BackpackItem.BackpackTier.ENDER ? 39 : 18;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < 9; c++) {
                int index = c + r * 9;
                this.addSlot(new Slot(this.storageInventory, index, 12 + c * 18, storageStartY + r * 18) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return !(stack.getItem() instanceof BackpackItem)
                                && !(stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof ShulkerBoxBlock)
                                && !(stack.getItem() instanceof BundleItem);
                    }
                });
            }
        }

        // Add Quiver Slots if applicable (shifted by 2px to x={64, 84, 104}, y=68)
        if (this.tier.hasQuiver) {
            int[] quiverX = {64, 84, 104};
            for (int i = 0; i < 3; i++) {
                this.addSlot(new Slot(this.quiverInventory, i, quiverX[i], 68) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return stack.isIn(ItemTags.ARROWS) || stack.getItem() instanceof net.mastermaxi.mastermaxisvanillaplus.item.BoltItem;
                    }
                });
            }
        }

        // Add Upgrade Slots (right column, shifted 1px left to 187, 1px up)
        int upgradeY;
        if (this.tier.hasQuiver) {
            upgradeY = 36;
        } else if (this.tier == BackpackItem.BackpackTier.ENDER) {
            upgradeY = 21;
        } else {
            upgradeY = 18;
        }

        for (int i = 0; i < this.tier.upgradeSlots; i++) {
            this.addSlot(new Slot(this.upgradeInventory, i, 187, upgradeY + i * 18) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return isAllowedUpgrade(stack);
                }
            });
        }

        // Add Player Inventory (x shifted to 12)
        int playerInvY;
        switch (this.tier) {
            case LEATHER -> playerInvY = 50;
            case IRON -> playerInvY = 68;
            case IRON_WITH_QUIVER -> playerInvY = 92;
            case NETHERITE -> playerInvY = 86;
            case ENDER -> playerInvY = 89;
            default -> playerInvY = 68;
        }

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 9; c++) {
                this.addSlot(new Slot(playerInventory, c + r * 9 + 9, 12 + c * 18, playerInvY + r * 18));
            }
        }

        for (int c = 0; c < 9; c++) {
            this.addSlot(new Slot(playerInventory, c, 12 + c * 18, playerInvY + 58));
        }
    }

    public BackpackItem.BackpackTier getTier() {
        return this.tier;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        if (this.entityId >= 0) {
            Entity e = player.getWorld().getEntityById(this.entityId);
            return e instanceof BackpackEntity && e.isAlive() && e.squaredDistanceTo(player) <= 64.0;
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

            int totalStorageSlots = this.tier.inventorySlots;
            int totalQuiverSlots = this.tier.hasQuiver ? 3 : 0;
            int totalUpgradeSlots = this.tier.upgradeSlots;
            int totalBackpackSlots = totalStorageSlots + totalQuiverSlots + totalUpgradeSlots;
            int totalSlots = totalBackpackSlots + 36;

            if (slotIndex < totalBackpackSlots) {
                // Moving from Backpack to Player inventory
                if (!this.insertItem(originalStack, totalBackpackSlots, totalSlots, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Moving from Player inventory to Backpack
                boolean inserted = false;
                if (this.tier.hasQuiver && (originalStack.isIn(ItemTags.ARROWS) || originalStack.getItem() instanceof net.mastermaxi.mastermaxisvanillaplus.item.BoltItem)) {
                    inserted = this.insertItem(originalStack, totalStorageSlots, totalStorageSlots + totalQuiverSlots, false);
                }
                if (!inserted) {
                    inserted = this.insertItem(originalStack, 0, totalStorageSlots, false);
                }
                if (!inserted && isAllowedUpgrade(originalStack)) {
                    inserted = this.insertItem(originalStack, totalStorageSlots + totalQuiverSlots, totalBackpackSlots, false);
                }

                if (!inserted) {
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

        if (this.tier != BackpackItem.BackpackTier.ENDER) {
            DefaultedList<ItemStack> items = DefaultedList.ofSize(this.tier.inventorySlots, ItemStack.EMPTY);
            for (int i = 0; i < this.tier.inventorySlots; i++) {
                items.set(i, this.storageInventory.getStack(i));
            }

            DefaultedList<ItemStack> upgrades = DefaultedList.ofSize(this.tier.upgradeSlots, ItemStack.EMPTY);
            for (int i = 0; i < this.tier.upgradeSlots; i++) {
                upgrades.set(i, this.upgradeInventory.getStack(i));
            }

            DefaultedList<ItemStack> arrows = null;
            if (this.tier.hasQuiver && this.quiverInventory != null) {
                arrows = DefaultedList.ofSize(3, ItemStack.EMPTY);
                for (int i = 0; i < 3; i++) {
                    arrows.set(i, this.quiverInventory.getStack(i));
                }
            }

            if (this.entityId >= 0) {
                Entity e = player.getWorld().getEntityById(this.entityId);
                if (e instanceof BackpackEntity backpackEntity) {
                    ItemStack current = backpackEntity.getBackpackStack();
                    if (current.getItem() instanceof BackpackItem item) {
                        item.setStoredItems(current, items);
                        item.setStoredUpgrades(current, upgrades);
                        if (arrows != null) item.setStoredQuiverArrows(current, arrows);
                        backpackEntity.setBackpackStack(current);
                    }
                }
            } else if (!this.backpackStack.isEmpty() && this.backpackStack.getItem() instanceof BackpackItem item) {
                item.setStoredItems(this.backpackStack, items);
                item.setStoredUpgrades(this.backpackStack, upgrades);
                if (arrows != null) item.setStoredQuiverArrows(this.backpackStack, arrows);

                AccessoriesCapability cap = AccessoriesCapability.get(player);
                if (cap != null) {
                    AccessoriesContainer back = cap.getContainers().get("back");
                    if (back != null) back.markChanged();
                }
            }
        } else {
            // For Ender Backpack, save upgrades
            DefaultedList<ItemStack> upgrades = DefaultedList.ofSize(this.tier.upgradeSlots, ItemStack.EMPTY);
            for (int i = 0; i < this.tier.upgradeSlots; i++) {
                upgrades.set(i, this.upgradeInventory.getStack(i));
            }

            if (this.entityId >= 0) {
                Entity e = player.getWorld().getEntityById(this.entityId);
                if (e instanceof BackpackEntity backpackEntity) {
                    ItemStack current = backpackEntity.getBackpackStack();
                    if (current.getItem() instanceof BackpackItem item) {
                        item.setStoredUpgrades(current, upgrades);
                        backpackEntity.setBackpackStack(current);
                    }
                }
            } else if (!this.backpackStack.isEmpty() && this.backpackStack.getItem() instanceof BackpackItem item) {
                item.setStoredUpgrades(this.backpackStack, upgrades);
                AccessoriesCapability cap = AccessoriesCapability.get(player);
                if (cap != null) {
                    AccessoriesContainer back = cap.getContainers().get("back");
                    if (back != null) back.markChanged();
                }
            }
        }
    }
}
