package net.mastermaxi.mastermaxisvanillaplus.screen;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.enchantment.ModEnchantments;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.mastermaxi.mastermaxisvanillaplus.item.SchematicItem;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class FletchingTableScreenHandler extends ScreenHandler {
    public static final int WEAPON_SLOT_INDEX = 0;
    public static final int ATTACHMENT_SLOT_INDEX = 1;
    public static final int INGREDIENT_A_SLOT_INDEX = 2;
    public static final int INGREDIENT_B_SLOT_INDEX = 3;
    public static final int INGREDIENT_C_SLOT_INDEX = 4;
    public static final int RESULT_SLOT_INDEX = 5;
    public static final int SCHEMATIC_SLOT_INDEX = 6;
    public static final int PLAYER_INV_START_INDEX = 7;
    public static final int PLAYER_HOTBAR_START_INDEX = 34;
    public static final int PLAYER_HOTBAR_END_INDEX = 43;

    private final ScreenHandlerContext context;
    private final PlayerEntity player;
    private final Inventory tableInventory;
    private final SimpleInventory attachmentInventory = new SimpleInventory(1);
    private final CraftingResultInventory resultInventory = new CraftingResultInventory();

    private boolean updatingWeaponAndAttachment = false;

    public FletchingTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY, new SimpleInventory(5));
    }

    public FletchingTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, Inventory tableInventory) {
        super(ModScreenHandlerTypes.FLETCHING_TABLE, syncId);
        this.context = context;
        this.player = playerInventory.player;
        this.tableInventory = tableInventory;
        checkSize(tableInventory, 5);
        tableInventory.onOpen(this.player);

        // Slot 0: Weapon slot (center top: x = 80, y = 18) - tableInventory index 0
        this.addSlot(new Slot(this.tableInventory, 0, 80, 18) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return isWeapon(stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                onWeaponSlotChanged();
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                onWeaponSlotChanged();
            }

            @Override
            public void markDirty() {
                super.markDirty();
                onWeaponSlotChanged();
            }
        });

        // Slot 1: Attachment slot (left top: x = 22, y = 18) - attachmentInventory index 0
        this.addSlot(new Slot(this.attachmentInventory, 0, 22, 18) {
            @Override
            public boolean canInsert(ItemStack stack) {
                if (!stack.isOf(ItemInit.VIEWFINDER)) {
                    return false;
                }
                ItemStack weapon = FletchingTableScreenHandler.this.tableInventory.getStack(0);
                return canAcceptViewfinder(weapon);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                ItemStack weapon = FletchingTableScreenHandler.this.tableInventory.getStack(0);
                if (hasCurseOfWelding(weapon)) {
                    return false;
                }
                return super.canTakeItems(playerEntity);
            }

            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                if (!stack.isEmpty() && stack.isOf(ItemInit.VIEWFINDER) && !updatingWeaponAndAttachment) {
                    mountViewfinder();
                }
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                ItemStack weapon = FletchingTableScreenHandler.this.tableInventory.getStack(0);
                if (hasCurseOfWelding(weapon)) {
                    return;
                }
                super.onTakeItem(player, stack);
                if (!updatingWeaponAndAttachment) {
                    unmountViewfinder();
                }
            }
        });

        // Slot 2: Ingredient A (Tail / Upgrade 1) (x = 22, y = 46) - tableInventory index 1
        this.addSlot(new Slot(this.tableInventory, 1, 22, 46) {
            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                updateResult();
            }

            @Override
            public void markDirty() {
                super.markDirty();
                updateResult();
            }
        });

        // Slot 3: Ingredient B (Shaft / Upgrade 2) (x = 53, y = 46) - tableInventory index 2
        this.addSlot(new Slot(this.tableInventory, 2, 53, 46) {
            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                updateResult();
            }

            @Override
            public void markDirty() {
                super.markDirty();
                updateResult();
            }
        });

        // Slot 4: Ingredient C (Head / Upgrade 3) (x = 84, y = 46) - tableInventory index 3
        this.addSlot(new Slot(this.tableInventory, 3, 84, 46) {
            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                updateResult();
            }

            @Override
            public void markDirty() {
                super.markDirty();
                updateResult();
            }
        });

        // Slot 5: Result X (x = 138, y = 46) - resultInventory index 0
        this.addSlot(new Slot(this.resultInventory, 0, 138, 46) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                FletchingTableScreenHandler.this.onTakeResult(player, stack);
                super.onTakeItem(player, stack);
            }
        });

        // Slot 6: Schematic slot (right top: x = 138, y = 18) - tableInventory index 4
        this.addSlot(new Slot(this.tableInventory, 4, 138, 18) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof SchematicItem;
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                updateResult();
            }

            @Override
            public void markDirty() {
                super.markDirty();
                updateResult();
            }
        });

        // Slots 7..33: Player Inventory (3x9)
        // Slots 34..42: Player Hotbar (1x9)
        this.addPlayerSlots(playerInventory, 8, 84);

        // Initial setup for weapon and attachment
        updatingWeaponAndAttachment = true;
        try {
            ItemStack weapon = this.tableInventory.getStack(0);
            if (!weapon.isEmpty() && isViewfinderCrossbow(weapon)) {
                this.attachmentInventory.setStack(0, new ItemStack(ItemInit.VIEWFINDER));
            }
        } finally {
            updatingWeaponAndAttachment = false;
        }
        updateResult();
    }

    public static boolean hasCurseOfWelding(ItemStack weapon) {
        if (weapon.isEmpty()) return false;
        ItemEnchantmentsComponent enchants = weapon.get(DataComponentTypes.ENCHANTMENTS);
        if (enchants != null) {
            for (RegistryEntry<Enchantment> entry : enchants.getEnchantments()) {
                if (entry.matchesKey(ModEnchantments.CURSE_OF_WELDING) ||
                        entry.matchesId(MASTERmaxisVanillaPlus.id("curse_of_welding"))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isWeapon(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Item item = stack.getItem();
        return item instanceof BowItem || item instanceof CrossbowItem;
    }

    public static boolean canAcceptViewfinder(ItemStack weapon) {
        if (weapon.isEmpty()) return false;
        Item item = weapon.getItem();
        return item == Items.CROSSBOW || item == ItemInit.IRON_CROSSBOW || item == ItemInit.NETHER_CROSSBOW;
    }

    public static boolean isViewfinderCrossbow(ItemStack weapon) {
        if (weapon.isEmpty()) return false;
        Item item = weapon.getItem();
        return item == ItemInit.CROSSBOW_VIEWFINDER ||
                item == ItemInit.IRON_CROSSBOW_VIEWFINDER ||
                item == ItemInit.NETHER_CROSSBOW_VIEWFINDER;
    }

    public static Item getViewfinderCrossbow(Item base) {
        if (base == Items.CROSSBOW) return ItemInit.CROSSBOW_VIEWFINDER;
        if (base == ItemInit.IRON_CROSSBOW) return ItemInit.IRON_CROSSBOW_VIEWFINDER;
        if (base == ItemInit.NETHER_CROSSBOW) return ItemInit.NETHER_CROSSBOW_VIEWFINDER;
        return base;
    }

    public static Item getBaseCrossbow(Item vf) {
        if (vf == ItemInit.CROSSBOW_VIEWFINDER) return Items.CROSSBOW;
        if (vf == ItemInit.IRON_CROSSBOW_VIEWFINDER) return ItemInit.IRON_CROSSBOW;
        if (vf == ItemInit.NETHER_CROSSBOW_VIEWFINDER) return ItemInit.NETHER_CROSSBOW;
        return vf;
    }

    private void onWeaponSlotChanged() {
        if (updatingWeaponAndAttachment) return;
        updatingWeaponAndAttachment = true;
        try {
            ItemStack weapon = this.tableInventory.getStack(0);
            if (weapon.isEmpty()) {
                this.attachmentInventory.setStack(0, ItemStack.EMPTY);
            } else if (isViewfinderCrossbow(weapon)) {
                this.attachmentInventory.setStack(0, new ItemStack(ItemInit.VIEWFINDER));
            } else {
                this.attachmentInventory.setStack(0, ItemStack.EMPTY);
            }
        } finally {
            updatingWeaponAndAttachment = false;
        }
        updateResult();
    }

    private void mountViewfinder() {
        updatingWeaponAndAttachment = true;
        try {
            ItemStack weapon = this.tableInventory.getStack(0);
            if (canAcceptViewfinder(weapon)) {
                Item upgraded = getViewfinderCrossbow(weapon.getItem());
                ItemStack newWeapon = weapon.copyComponentsToNewStack(upgraded, 1);
                newWeapon.setDamage(weapon.getDamage());
                this.tableInventory.setStack(0, newWeapon);
                player.playSound(SoundEvents.ITEM_SPYGLASS_USE, 1.0f, 1.0f);
            }
        } finally {
            updatingWeaponAndAttachment = false;
        }
        updateResult();
    }

    private void unmountViewfinder() {
        updatingWeaponAndAttachment = true;
        try {
            ItemStack weapon = this.tableInventory.getStack(0);
            if (isViewfinderCrossbow(weapon)) {
                Item base = getBaseCrossbow(weapon.getItem());
                ItemStack newWeapon = weapon.copyComponentsToNewStack(base, 1);
                newWeapon.setDamage(weapon.getDamage());
                this.tableInventory.setStack(0, newWeapon);
                player.playSound(SoundEvents.ITEM_SPYGLASS_STOP_USING, 1.0f, 1.0f);
            }
        } finally {
            updatingWeaponAndAttachment = false;
        }
        updateResult();
    }

    public SchematicItem getActiveUpgradeScheme() {
        ItemStack weapon = this.tableInventory.getStack(0);
        if (weapon.isEmpty()) return null;

        ItemStack s = this.tableInventory.getStack(4);
        if (!s.isEmpty() && s.getItem() instanceof SchematicItem scheme) {
            if (matchesScheme(scheme, weapon)) {
                return scheme;
            }
        }
        return null;
    }

    private boolean matchesScheme(SchematicItem scheme, ItemStack weapon) {
        Item w = weapon.getItem();
        if (scheme == ItemInit.IRON_CROSSBOW_SCHEME) {
            return w == Items.CROSSBOW || w == ItemInit.CROSSBOW_VIEWFINDER;
        }
        if (scheme == ItemInit.NETHER_CROSSBOW_SCHEME) {
            return w == ItemInit.IRON_CROSSBOW || w == ItemInit.IRON_CROSSBOW_VIEWFINDER;
        }
        return false;
    }

    public void updateResult() {
        SchematicItem scheme = getActiveUpgradeScheme();
        ItemStack weapon = this.tableInventory.getStack(0);

        if (scheme != null && !weapon.isEmpty()) {
            // Mode B: Weapon Upgrade
            ItemStack inA = this.tableInventory.getStack(1);
            ItemStack inB = this.tableInventory.getStack(2);
            ItemStack inC = this.tableInventory.getStack(3);

            if (scheme == ItemInit.IRON_CROSSBOW_SCHEME) {
                // 3 Iron + 1 Slime + 2 String
                if (inA.isOf(Items.IRON_INGOT) && inA.getCount() >= 3 &&
                        inB.isOf(Items.SLIME_BALL) && inB.getCount() >= 1 &&
                        inC.isOf(Items.STRING) && inC.getCount() >= 2) {

                    Item target = weapon.isOf(ItemInit.CROSSBOW_VIEWFINDER) ?
                            ItemInit.IRON_CROSSBOW_VIEWFINDER : ItemInit.IRON_CROSSBOW;
                    ItemStack result = weapon.copyComponentsToNewStack(target, 1);
                    result.setDamage(weapon.getDamage());
                    resultInventory.setStack(0, result);
                    return;
                }
            } else if (scheme == ItemInit.NETHER_CROSSBOW_SCHEME) {
                // 3 Gold + 5 Copper + 1 Netherite Ingot
                if (inA.isOf(Items.GOLD_INGOT) && inA.getCount() >= 3 &&
                        inB.isOf(Items.COPPER_INGOT) && inB.getCount() >= 5 &&
                        inC.isOf(Items.NETHERITE_INGOT) && inC.getCount() >= 1) {

                    Item target = weapon.isOf(ItemInit.IRON_CROSSBOW_VIEWFINDER) ?
                            ItemInit.NETHER_CROSSBOW_VIEWFINDER : ItemInit.NETHER_CROSSBOW;
                    ItemStack result = weapon.copyComponentsToNewStack(target, 1);
                    result.setDamage(weapon.getDamage());
                    resultInventory.setStack(0, result);
                    return;
                }
            }

            // In upgrade mode, but ingredients incomplete
            resultInventory.setStack(0, ItemStack.EMPTY);
            return;
        }

        // Mode A: Projectile Crafting
        ItemStack inA = this.tableInventory.getStack(1);
        ItemStack inB = this.tableInventory.getStack(2);
        ItemStack inC = this.tableInventory.getStack(3);

        // Arrow: Feather + Stick + Flint -> 4 Arrows
        if (inA.isOf(Items.FEATHER) && inA.getCount() >= 1 &&
                inB.isOf(Items.STICK) && inB.getCount() >= 1 &&
                inC.isOf(Items.FLINT) && inC.getCount() >= 1) {
            resultInventory.setStack(0, new ItemStack(Items.ARROW, 4));
            return;
        }

        // Heavy Arrow: Ostrich Feather + Stick + Iron Nugget -> 4 Heavy Arrows
        if (inA.isOf(ItemInit.OSTRICH_FEATHER) && inA.getCount() >= 1 &&
                inB.isOf(Items.STICK) && inB.getCount() >= 1 &&
                inC.isOf(Items.IRON_NUGGET) && inC.getCount() >= 1) {
            resultInventory.setStack(0, new ItemStack(ItemInit.HEAVY_ARROW, 4));
            return;
        }

        // Bolt: empty + Stick + Iron Nugget -> 4 Bolts
        if (inA.isEmpty() &&
                inB.isOf(Items.STICK) && inB.getCount() >= 1 &&
                inC.isOf(Items.IRON_NUGGET) && inC.getCount() >= 1) {
            resultInventory.setStack(0, new ItemStack(ItemInit.BOLT, 4));
            return;
        }

        // Tipping: Projectile (Arrow, Heavy Arrow, or Bolt) + Potion / Lingering Potion -> Tipped Projectiles
        if ((inA.isOf(Items.ARROW) || inA.isOf(ItemInit.HEAVY_ARROW) || inA.isOf(ItemInit.BOLT)) &&
                (inB.isOf(Items.POTION) || inB.isOf(Items.SPLASH_POTION) || inB.isOf(Items.LINGERING_POTION)) &&
                inC.isEmpty()) {
            net.minecraft.component.type.PotionContentsComponent potion = inB.get(DataComponentTypes.POTION_CONTENTS);
            if (potion != null && potion.hasEffects()) {
                int count = Math.min(inA.getCount(), 8);
                Item targetItem;
                if (inA.isOf(Items.ARROW)) {
                    targetItem = Items.TIPPED_ARROW;
                } else if (inA.isOf(ItemInit.HEAVY_ARROW)) {
                    targetItem = ItemInit.HEAVY_ARROW;
                } else {
                    targetItem = ItemInit.BOLT;
                }
                ItemStack result = new ItemStack(targetItem, count);
                result.set(DataComponentTypes.POTION_CONTENTS, potion);
                resultInventory.setStack(0, result);
                return;
            }
        }

        resultInventory.setStack(0, ItemStack.EMPTY);
    }

    public void onTakeResult(@SuppressWarnings("unused") PlayerEntity player, ItemStack resultStack) {
        SchematicItem scheme = getActiveUpgradeScheme();
        ItemStack weapon = this.tableInventory.getStack(0);

        if (scheme != null && !weapon.isEmpty()) {
            // Mode B: Consume base weapon, ingredients & schematic
            updatingWeaponAndAttachment = true;
            try {
                this.tableInventory.removeStack(0, 1);
                this.attachmentInventory.setStack(0, ItemStack.EMPTY);
            } finally {
                updatingWeaponAndAttachment = false;
            }

            // Consume 1 schematic
            this.tableInventory.removeStack(4, 1);

            if (scheme == ItemInit.IRON_CROSSBOW_SCHEME) {
                this.tableInventory.removeStack(1, 3);
                this.tableInventory.removeStack(2, 1);
                this.tableInventory.removeStack(3, 2);
            } else if (scheme == ItemInit.NETHER_CROSSBOW_SCHEME) {
                this.tableInventory.removeStack(1, 3);
                this.tableInventory.removeStack(2, 5);
                this.tableInventory.removeStack(3, 1);
            }

            context.run((world, pos) -> world.playSound(null, pos, SoundEvents.BLOCK_SMITHING_TABLE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f));
        } else {
            // Check if this was a tipping operation
            ItemStack inAStack = this.tableInventory.getStack(1);
            ItemStack inBStack = this.tableInventory.getStack(2);
            if ((inAStack.isOf(Items.ARROW) || inAStack.isOf(ItemInit.HEAVY_ARROW) || inAStack.isOf(ItemInit.BOLT)) &&
                    (inBStack.isOf(Items.POTION) || inBStack.isOf(Items.SPLASH_POTION) || inBStack.isOf(Items.LINGERING_POTION))) {
                int countConsumed = resultStack.getCount();
                this.tableInventory.removeStack(1, countConsumed);
                this.tableInventory.setStack(2, new ItemStack(Items.GLASS_BOTTLE));
                context.run((world, pos) -> world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f));
            } else {
                // Mode A: Consume 1 from each non-empty ingredient slot
                if (!this.tableInventory.getStack(1).isEmpty()) {
                    this.tableInventory.removeStack(1, 1);
                }
                if (!this.tableInventory.getStack(2).isEmpty()) {
                    this.tableInventory.removeStack(2, 1);
                }
                if (!this.tableInventory.getStack(3).isEmpty()) {
                    this.tableInventory.removeStack(3, 1);
                }

                context.run((world, pos) -> world.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1.0f, 1.2f));
            }
        }

        updateResult();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.tableInventory.canPlayerUse(player) && canUse(this.context, player, Blocks.FLETCHING_TABLE);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.tableInventory.onClose(player);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (inventory == this.tableInventory) {
            onWeaponSlotChanged();
            updateResult();
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.getSlot(slotIndex);

        if (slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            if (slotIndex == RESULT_SLOT_INDEX) {
                // Shift-clicking craft result
                if (!this.insertItem(slotStack, PLAYER_INV_START_INDEX, PLAYER_HOTBAR_END_INDEX, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(slotStack, itemStack);
                slot.onTakeItem(player, slotStack);
                return itemStack;
            } else if (slotIndex == ATTACHMENT_SLOT_INDEX) {
                ItemStack weapon = this.tableInventory.getStack(0);
                if (hasCurseOfWelding(weapon)) {
                    return ItemStack.EMPTY;
                }
                // Taking viewfinder from attachment slot
                ItemStack vf = slotStack.copy();
                if (!this.insertItem(vf, PLAYER_INV_START_INDEX, PLAYER_HOTBAR_END_INDEX, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onTakeItem(player, vf);
                slot.setStack(ItemStack.EMPTY);
                return itemStack;
            } else if (slotIndex < PLAYER_INV_START_INDEX) {
                // From table slots (weapon, ingredients, schematic) to player inventory
                if (!this.insertItem(slotStack, PLAYER_INV_START_INDEX, PLAYER_HOTBAR_END_INDEX, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // From player inventory to table
                if (slotStack.getItem() instanceof SchematicItem && !this.getSlot(SCHEMATIC_SLOT_INDEX).hasStack()) {
                    ItemStack singleScheme = slotStack.split(1);
                    this.getSlot(SCHEMATIC_SLOT_INDEX).setStack(singleScheme);
                    return itemStack;
                } else if (slotStack.isOf(ItemInit.VIEWFINDER) && canAcceptViewfinder(this.tableInventory.getStack(0)) && !this.getSlot(ATTACHMENT_SLOT_INDEX).hasStack()) {
                    ItemStack singleVf = slotStack.split(1);
                    this.getSlot(ATTACHMENT_SLOT_INDEX).setStack(singleVf);
                    return itemStack;
                } else if (isWeapon(slotStack) && !this.getSlot(WEAPON_SLOT_INDEX).hasStack()) {
                    ItemStack singleWeapon = slotStack.split(1);
                    this.getSlot(WEAPON_SLOT_INDEX).setStack(singleWeapon);
                    return itemStack;
                } else {
                    // Try ingredients A, B, C
                    if (!this.insertItem(slotStack, INGREDIENT_A_SLOT_INDEX, INGREDIENT_C_SLOT_INDEX + 1, false)) {
                        // Between inventory and hotbar
                        if (slotIndex < PLAYER_HOTBAR_START_INDEX) {
                            if (!this.insertItem(slotStack, PLAYER_HOTBAR_START_INDEX, PLAYER_HOTBAR_END_INDEX, false)) {
                                return ItemStack.EMPTY;
                            }
                        } else if (!this.insertItem(slotStack, PLAYER_INV_START_INDEX, PLAYER_HOTBAR_START_INDEX, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (slotStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, slotStack);
        }

        return itemStack;
    }
}
