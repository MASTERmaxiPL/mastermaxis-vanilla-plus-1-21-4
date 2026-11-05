package net.mastermaxi.mastermaxisvanillaplus.screen;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.BackpackEntity;
import net.mastermaxi.mastermaxisvanillaplus.item.BackpackItem;
import net.mastermaxi.mastermaxisvanillaplus.item.BackpackUpgradeHelper;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.mastermaxi.mastermaxisvanillaplus.network.BackpackPayload;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BackpackScreenHandler extends ScreenHandler {

    private final BackpackItem.BackpackTier tier;
    private final int entityId;
    private final ItemStack backpackStack;
    private final Inventory storageInventory;
    private final SimpleInventory upgradeInventory;
    private final SimpleInventory quiverInventory;
    private final PlayerEntity player;
    private final CraftingInventory craftingInventory;
    private final CraftingResultInventory craftingResultInventory;
    private final CraftingResultSlot craftingResultSlot;
    private final List<Slot> craftingInputSlots = new ArrayList<>();
    private boolean craftingActive;

    public static final TagKey<net.minecraft.item.Item> BACKPACK_UPGRADES_TAG = TagKey.of(RegistryKeys.ITEM, Identifier.of("mastermaxisvanillaplus", "backpack_upgrades"));

    public static boolean isAllowedUpgrade(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.isOf(Items.CRAFTING_TABLE)
                || stack.isOf(Items.LANTERN)
                || stack.isOf(Items.SOUL_LANTERN)
                || stack.isOf(Items.JUKEBOX)
                || stack.isOf(ItemInit.MAGNET)
                || stack.isIn(BACKPACK_UPGRADES_TAG);
    }

    public static boolean isRestrictedStorageItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        return stack.getItem() instanceof BackpackItem
                || (stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof ShulkerBoxBlock)
                || stack.getItem() instanceof BundleItem;
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

        this.player = playerInventory.player;
        this.craftingInventory = new CraftingInventory(this, 3, 3);
        this.craftingResultInventory = new CraftingResultInventory();
        this.craftingActive = hasCraftingUpgrade();

        this.upgradeInventory.addListener(inv -> {
            boolean hadCrafting = this.craftingActive;
            boolean hasCrafting = hasCraftingUpgrade();
            if (hadCrafting && !hasCrafting) {
                this.dropInventory(this.player, this.craftingInventory);
                this.craftingResultInventory.setStack(0, ItemStack.EMPTY);
            }
            this.craftingActive = hasCrafting;

            if (this.backpackStack.getItem() instanceof BackpackItem item) {
                DefaultedList<ItemStack> upgrades = DefaultedList.ofSize(this.tier.upgradeSlots, ItemStack.EMPTY);
                for (int i = 0; i < this.tier.upgradeSlots; i++) {
                    upgrades.set(i, this.upgradeInventory.getStack(i));
                }
                item.setStoredUpgrades(this.backpackStack, upgrades);
                if (this.entityId < 0) {
                    AccessoriesCapability cap = AccessoriesCapability.get(playerInventory.player);
                    if (cap != null) {
                        AccessoriesContainer back = cap.getContainers().get("back");
                        if (back != null) {
                            ItemStack current = back.getAccessories().getStack(0);
                            item.setStoredUpgrades(current, upgrades);
                            back.markChanged();
                        }
                    }
                } else {
                    Entity e = playerInventory.player.getWorld().getEntityById(this.entityId);
                    if (e instanceof BackpackEntity backpackEntity) {
                        backpackEntity.setBackpackStack(this.backpackStack);
                    }
                }
            }
        });

        if (this.tier.hasQuiver) {
            this.quiverInventory = new SimpleInventory(3);
            if (this.backpackStack.getItem() instanceof BackpackItem item) {
                DefaultedList<ItemStack> arrows = item.getStoredQuiverArrows(this.backpackStack);
                for (int i = 0; i < arrows.size(); i++) {
                    this.quiverInventory.setStack(i, arrows.get(i));
                }
            }
            this.quiverInventory.addListener(inv -> {
                if (this.backpackStack.getItem() instanceof BackpackItem item) {
                    DefaultedList<ItemStack> arrows = DefaultedList.ofSize(3, ItemStack.EMPTY);
                    for (int i = 0; i < 3; i++) {
                        arrows.set(i, this.quiverInventory.getStack(i));
                    }
                    item.setStoredQuiverArrows(this.backpackStack, arrows);
                    if (this.entityId < 0) {
                        AccessoriesCapability cap = AccessoriesCapability.get(playerInventory.player);
                        if (cap != null) {
                            AccessoriesContainer back = cap.getContainers().get("back");
                            if (back != null) {
                                ItemStack current = back.getAccessories().getStack(0);
                                item.setStoredQuiverArrows(current, arrows);
                                back.markChanged();
                            }
                        }
                    }
                }
            });
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
                        if (isRestrictedStorageItem(stack)) {
                            return false;
                        }
                        if (tier.hasQuiver && (stack.isIn(ItemTags.ARROWS) || stack.getItem() instanceof net.mastermaxi.mastermaxisvanillaplus.item.BoltItem)) {
                            return false;
                        }
                        return super.canInsert(stack);
                    }

                    @Override
                    public boolean canTakeItems(PlayerEntity playerEntity) {
                        if (tier == BackpackItem.BackpackTier.ENDER && isRestrictedStorageItem(getStack())) {
                            return false;
                        }
                        return super.canTakeItems(playerEntity);
                    }

                    @Override
                    public boolean canTakePartial(PlayerEntity playerEntity) {
                        if (tier == BackpackItem.BackpackTier.ENDER && isRestrictedStorageItem(getStack())) {
                            return false;
                        }
                        return super.canTakePartial(playerEntity);
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

        // Add Crafting Expander Slots
        int expanderX = 183;
        int expanderY = getCraftingExpanderY();

        this.craftingResultSlot = new CraftingResultSlot(this.player, this.craftingInventory, this.craftingResultInventory, 0, expanderX + 69, expanderY + 24) {
            @Override
            public boolean isEnabled() {
                return hasCraftingUpgrade();
            }
        };
        this.addSlot(this.craftingResultSlot);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                int index = c + r * 3;
                Slot inputSlot = new Slot(this.craftingInventory, index, expanderX + 4 + c * 18, expanderY + 6 + r * 18) {
                    @Override
                    public boolean isEnabled() {
                        return hasCraftingUpgrade();
                    }
                };
                this.craftingInputSlots.add(inputSlot);
                this.addSlot(inputSlot);
            }
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
        } else {
            AccessoriesCapability cap = AccessoriesCapability.get(player);
            if (cap != null) {
                AccessoriesContainer back = cap.getContainers().get("back");
                if (back != null) {
                    ItemStack current = back.getAccessories().getStack(0);
                    return current.getItem() instanceof BackpackItem;
                }
            }
            return false;
        }
    }


    public ItemStack insertIntoStorage(ItemStack stack) {
        if (stack.isEmpty()) return stack;
        if (isRestrictedStorageItem(stack)) return stack;
        int totalStorageSlots = this.tier.inventorySlots;
        // 1. Try merge with existing matching stacks
        for (int i = 0; i < totalStorageSlots; i++) {
            Slot slot = this.slots.get(i);
            ItemStack slotStack = slot.getStack();
            if (!slotStack.isEmpty() && ItemStack.areItemsAndComponentsEqual(slotStack, stack) && slotStack.getCount() < slotStack.getMaxCount()) {
                int toAdd = Math.min(stack.getCount(), slotStack.getMaxCount() - slotStack.getCount());
                slotStack.increment(toAdd);
                stack.decrement(toAdd);
                slot.markDirty();
                if (stack.isEmpty()) {
                    this.syncState();
                    return ItemStack.EMPTY;
                }
            }
        }
        // 2. Try empty storage slots
        for (int i = 0; i < totalStorageSlots; i++) {
            Slot slot = this.slots.get(i);
            if (!slot.hasStack() && slot.canInsert(stack)) {
                slot.setStack(stack.copy());
                stack.setCount(0);
                slot.markDirty();
                this.syncState();
                return ItemStack.EMPTY;
            }
        }
        this.syncState();
        return stack;
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        int totalStorageSlots = this.tier.inventorySlots;
        int totalQuiverSlots = this.tier.hasQuiver ? 3 : 0;
        int upgradeStart = totalStorageSlots + totalQuiverSlots;
        int upgradeEnd = upgradeStart + this.tier.upgradeSlots;

        if (slotIndex >= upgradeStart && slotIndex < upgradeEnd) {
            Slot slot = this.slots.get(slotIndex);
            if (slot.hasStack() && slot.getStack().isOf(Items.JUKEBOX)) {
                ItemStack cursorStack = this.getCursorStack();
                if (!cursorStack.isEmpty()) {
                    Optional<RegistryEntry<JukeboxSong>> songOpt = JukeboxSong.getSongEntryFromStack(player.getRegistryManager(), cursorStack);
                    if (songOpt.isPresent()) {
                        if (!player.getWorld().isClient && player instanceof ServerPlayerEntity serverPlayer) {
                            ItemStack discToPlay = cursorStack.split(1);
                            this.setCursorStack(cursorStack);
                            BackpackUpgradeHelper.startJukebox(serverPlayer, discToPlay, songOpt.get(), this.backpackStack, this.tier);
                            this.syncState();
                        } else if (player.getWorld().isClient) {
                            cursorStack.decrement(1);
                            this.setCursorStack(cursorStack);
                        }
                        return;
                    }
                } else {
                    if (player instanceof ServerPlayerEntity serverPlayer && BackpackUpgradeHelper.isPlayingJukebox(serverPlayer)) {
                        if (actionType == SlotActionType.QUICK_MOVE) {
                            BackpackUpgradeHelper.stopJukebox(serverPlayer, true);
                            this.syncState();
                            return;
                        } else if (actionType == SlotActionType.PICKUP) {
                            ItemStack ejected = BackpackUpgradeHelper.stopJukebox(serverPlayer, false);
                            if (!ejected.isEmpty()) {
                                this.setCursorStack(ejected);
                                this.syncState();
                                return;
                            }
                        }
                    }
                }
            }
        }

        super.onSlotClick(slotIndex, button, actionType, player);
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
            int totalBackpackContainerSlots = totalStorageSlots + totalQuiverSlots + totalUpgradeSlots;
            int resultSlotId = this.craftingResultSlot.id;
            int craftingInputStart = this.craftingInputSlots.getFirst().id;
            int playerInvStart = craftingInputStart + 9;
            int totalSlots = playerInvStart + 36;

            if (slotIndex == resultSlotId) {
                // Moving from Crafting Result slot to Player inventory / Backpack
                if (!this.insertItem(originalStack, playerInvStart, totalSlots, true)) {
                    if (!this.insertItem(originalStack, 0, totalStorageSlots, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                slot.onQuickTransfer(originalStack, newStack);
            } else if (slotIndex >= craftingInputStart && slotIndex < playerInvStart) {
                // Moving from Crafting Input slots to Player inventory / Backpack
                if (!this.insertItem(originalStack, playerInvStart, totalSlots, false)) {
                    if (!this.insertItem(originalStack, 0, totalStorageSlots, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (slotIndex < totalBackpackContainerSlots) {
                // Moving from Backpack Storage/Quiver/Upgrade to Player inventory
                if (!slot.canTakeItems(player)) {
                    return ItemStack.EMPTY;
                }
                if (!this.insertItem(originalStack, playerInvStart, totalSlots, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Moving from Player inventory to Backpack
                boolean inserted;
                if (this.tier.hasQuiver && (originalStack.isIn(ItemTags.ARROWS) || originalStack.getItem() instanceof net.mastermaxi.mastermaxisvanillaplus.item.BoltItem)) {
                    inserted = this.insertItem(originalStack, totalStorageSlots, totalStorageSlots + totalQuiverSlots, false);
                } else if (isAllowedUpgrade(originalStack)) {
                    inserted = this.insertItem(originalStack, totalStorageSlots + totalQuiverSlots, totalBackpackContainerSlots, false);
                    if (!inserted && !isRestrictedStorageItem(originalStack)) {
                        inserted = this.insertItem(originalStack, 0, totalStorageSlots, false);
                    }
                } else if (!isRestrictedStorageItem(originalStack)) {
                    inserted = this.insertItem(originalStack, 0, totalStorageSlots, false);
                } else {
                    inserted = false;
                }

                if (!inserted) {
                    if (slotIndex < playerInvStart + 27) {
                        if (!this.insertItem(originalStack, playerInvStart + 27, totalSlots, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(originalStack, playerInvStart, playerInvStart + 27, false)) {
                        return ItemStack.EMPTY;
                    }
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
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (inventory == this.craftingInventory) {
            updateCraftingResult();
        }
    }

    private void updateCraftingResult() {
        if (this.player.getWorld() instanceof ServerWorld serverWorld) {
            CraftingRecipeInput input = this.craftingInventory.createRecipeInput();
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) this.player;
            ItemStack result = ItemStack.EMPTY;
            Optional<RecipeEntry<CraftingRecipe>> match = serverWorld.getServer()
                    .getRecipeManager()
                    .getFirstMatch(RecipeType.CRAFTING, input, serverWorld);
            if (match.isPresent()) {
                RecipeEntry<CraftingRecipe> entry = match.get();
                CraftingRecipe recipe = entry.value();
                if (this.craftingResultInventory.shouldCraftRecipe(serverPlayer, entry)) {
                    ItemStack crafted = recipe.craft(input, serverWorld.getRegistryManager());
                    if (crafted.isItemEnabled(serverWorld.getEnabledFeatures())) {
                        result = crafted;
                    }
                }
            }
            this.craftingResultInventory.setStack(0, result);
            this.setPreviousTrackedSlot(this.craftingResultSlot.id, result);
            serverPlayer.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(this.syncId, this.nextRevision(), this.craftingResultSlot.id, result));
        }
    }

    public boolean hasCraftingUpgrade() {
        for (int i = 0; i < this.upgradeInventory.size(); i++) {
            if (this.upgradeInventory.getStack(i).isOf(Items.CRAFTING_TABLE)) {
                return true;
            }
        }
        return false;
    }

    public int getCraftingExpanderY() {
        return switch (this.tier) {
            case LEATHER -> 35;
            case IRON, IRON_WITH_QUIVER -> 53;
            case NETHERITE -> 71;
            case ENDER -> 74;
        };
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        this.dropInventory(player, this.craftingInventory);

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

                BackpackUpgradeHelper.markBackpackChanged(player);
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
                BackpackUpgradeHelper.markBackpackChanged(player);
            }
        }
    }
}
