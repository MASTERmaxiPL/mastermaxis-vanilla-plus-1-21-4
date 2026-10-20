package net.mastermaxi.mastermaxisvanillaplus.item;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import io.wispforest.accessories.api.Accessory;
import io.wispforest.accessories.api.AccessoryRegistry;
import net.mastermaxi.mastermaxisvanillaplus.component.ModDataComponentTypes;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class BackpackItem extends Item implements Accessory {

    public enum BackpackTier {
        LEATHER(9, 1, false),
        IRON(18, 2, false),
        IRON_WITH_QUIVER(18, 2, true),
        NETHERITE(27, 3, false),
        ENDER(27, 3, false);

        public final int inventorySlots;
        public final int upgradeSlots;
        public final boolean hasQuiver;

        BackpackTier(int inventorySlots, int upgradeSlots, boolean hasQuiver) {
            this.inventorySlots = inventorySlots;
            this.upgradeSlots = upgradeSlots;
            this.hasQuiver = hasQuiver;
        }
    }

    private final BackpackTier tier;

    public BackpackItem(BackpackTier tier, Settings settings) {
        super(settings.maxCount(1));
        this.tier = tier;
        AccessoryRegistry.register(this, this);
    }

    public BackpackTier getTier() {
        return this.tier;
    }

    @Override
    public boolean canEquipFromUse(ItemStack stack) {
        return false;
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        ItemStack heldStack = player.getStackInHand(hand);

        AccessoriesCapability capability = AccessoriesCapability.get(player);
        if (capability == null) {
            return ActionResult.PASS;
        }

        AccessoriesContainer backContainer = capability.getContainers().get("back");
        if (backContainer == null) {
            return ActionResult.PASS;
        }

        ItemStack current = backContainer.getAccessories().getStack(0);

        if (current.isEmpty()) {
            if (!world.isClient) {
                backContainer.getAccessories().setStack(0, heldStack.copy());
                backContainer.markChanged();
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (player instanceof ServerPlayerEntity serverPlayer) {
                    serverPlayer.playerScreenHandler.syncState();
                    serverPlayer.currentScreenHandler.syncState();
                }
            }
            player.setStackInHand(hand, ItemStack.EMPTY);
            return ActionResult.SUCCESS;
        } else if (current.getItem() instanceof BackpackItem || current.getItem() instanceof QuiverItem || current.isOf(Items.ELYTRA)) {
            if (!world.isClient) {
                if (current.getItem() instanceof BackpackItem oldBackpack) {
                    oldBackpack.dropStoredItems(world, player, current);
                } else if (current.getItem() instanceof QuiverItem oldQuiver) {
                    oldQuiver.dropStoredArrows(world, player, current);
                }
                ItemStack oldStack = current.copy();
                backContainer.getAccessories().setStack(0, heldStack.copy());
                player.setStackInHand(hand, oldStack);
                backContainer.markChanged();
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        current.isOf(Items.ELYTRA) ? SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA.value() : SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value(),
                        SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (player instanceof ServerPlayerEntity serverPlayer) {
                    serverPlayer.playerScreenHandler.syncState();
                    serverPlayer.currentScreenHandler.syncState();
                }
            } else {
                current.remove(DataComponentTypes.CONTAINER);
                current.remove(ModDataComponentTypes.QUIVER);
                current.remove(ModDataComponentTypes.UPGRADES);
                player.setStackInHand(hand, current.copy());
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public boolean canBeNested() {
        return false;
    }

    public void dropStoredItems(World world, PlayerEntity player, ItemStack backpackStack) {
        dropStoredItems(world, player, player != null ? player.getX() : 0.0, player != null ? player.getY() : 0.0, player != null ? player.getZ() : 0.0, backpackStack);
    }

    public void dropStoredItems(World world, PlayerEntity player, double x, double y, double z, ItemStack backpackStack) {
        if (world.isClient) return;

        if (this.tier == BackpackTier.ENDER) {
            // Ender Chest items are personal and never dropped into world
            return;
        }

        // Drop storage items
        DefaultedList<ItemStack> items = getStoredItems(backpackStack);
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                ItemStack drop = stack.copy();
                stack.setCount(0);
                if (player != null) {
                    player.dropItem(drop, true, false);
                } else {
                    net.minecraft.util.ItemScatterer.spawn(world, x, y, z, drop);
                }
            }
        }
        backpackStack.remove(DataComponentTypes.CONTAINER);

        // Drop upgrade items
        DefaultedList<ItemStack> upgrades = getStoredUpgrades(backpackStack);
        for (ItemStack stack : upgrades) {
            if (!stack.isEmpty()) {
                ItemStack drop = stack.copy();
                stack.setCount(0);
                if (player != null) {
                    player.dropItem(drop, true, false);
                } else {
                    net.minecraft.util.ItemScatterer.spawn(world, x, y, z, drop);
                }
            }
        }
        backpackStack.remove(ModDataComponentTypes.UPGRADES);

        // Drop quiver arrows if present
        if (this.tier.hasQuiver) {
            DefaultedList<ItemStack> arrows = getStoredQuiverArrows(backpackStack);
            for (ItemStack stack : arrows) {
                if (!stack.isEmpty()) {
                    ItemStack drop = stack.copy();
                    stack.setCount(0);
                    if (player != null) {
                        player.dropItem(drop, true, false);
                    } else {
                        net.minecraft.util.ItemScatterer.spawn(world, x, y, z, drop);
                    }
                }
            }
            backpackStack.remove(ModDataComponentTypes.QUIVER);
        }
    }

    public DefaultedList<ItemStack> getStoredItems(ItemStack stack) {
        DefaultedList<ItemStack> list = DefaultedList.ofSize(this.tier.inventorySlots, ItemStack.EMPTY);
        ContainerComponent component = stack.getOrDefault(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT);
        component.copyTo(list);
        return list;
    }

    private void setContainerComponent(ItemStack stack, ComponentType<ContainerComponent> type, DefaultedList<ItemStack> items) {
        boolean hasItems = false;
        for (ItemStack item : items) {
            if (!item.isEmpty()) {
                hasItems = true;
                break;
            }
        }
        if (hasItems) {
            stack.set(type, ContainerComponent.fromStacks(items));
        } else {
            stack.remove(type);
        }
    }

    public void setStoredItems(ItemStack stack, DefaultedList<ItemStack> items) {
        setContainerComponent(stack, DataComponentTypes.CONTAINER, items);
    }

    public DefaultedList<ItemStack> getStoredUpgrades(ItemStack stack) {
        DefaultedList<ItemStack> list = DefaultedList.ofSize(this.tier.upgradeSlots, ItemStack.EMPTY);
        ContainerComponent component = stack.getOrDefault(ModDataComponentTypes.UPGRADES, ContainerComponent.DEFAULT);
        component.copyTo(list);
        return list;
    }

    public void setStoredUpgrades(ItemStack stack, DefaultedList<ItemStack> upgrades) {
        setContainerComponent(stack, ModDataComponentTypes.UPGRADES, upgrades);
    }

    public DefaultedList<ItemStack> getStoredQuiverArrows(ItemStack stack) {
        DefaultedList<ItemStack> list = DefaultedList.ofSize(3, ItemStack.EMPTY);
        ContainerComponent component = stack.getOrDefault(ModDataComponentTypes.QUIVER, ContainerComponent.DEFAULT);
        component.copyTo(list);
        return list;
    }

    public void setStoredQuiverArrows(ItemStack stack, DefaultedList<ItemStack> arrows) {
        setContainerComponent(stack, ModDataComponentTypes.QUIVER, arrows);
    }
}
