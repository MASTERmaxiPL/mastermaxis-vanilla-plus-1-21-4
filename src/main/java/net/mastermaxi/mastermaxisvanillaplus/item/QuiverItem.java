package net.mastermaxi.mastermaxisvanillaplus.item;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import io.wispforest.accessories.api.Accessory;
import io.wispforest.accessories.api.AccessoryRegistry;
import net.mastermaxi.mastermaxisvanillaplus.component.ModDataComponentTypes;
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

public class QuiverItem extends Item implements Accessory {
    public static final int SLOTS = 4;

    public QuiverItem(Settings settings) {
        super(settings.maxCount(1));
        AccessoryRegistry.register(this, this);
    }

    @Override
    public boolean canEquipFromUse(ItemStack stack) {
        return false;
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        ItemStack held = player.getStackInHand(hand);

        AccessoriesCapability capability = AccessoriesCapability.get(player);
        if (capability == null) return ActionResult.PASS;

        AccessoriesContainer back = capability.getContainers().get("back");
        if (back == null) return ActionResult.PASS;

        ItemStack current = back.getAccessories().getStack(0);

        if (current.isEmpty()) {
            if (!world.isClient) {
                back.getAccessories().setStack(0, held.copy());
                back.markChanged();
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
                ItemStack old = current.copy();
                back.getAccessories().setStack(0, held.copy());
                player.setStackInHand(hand, old);
                back.markChanged();
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

    public static DefaultedList<ItemStack> getStoredArrows(ItemStack stack) {
        DefaultedList<ItemStack> list = DefaultedList.ofSize(SLOTS, ItemStack.EMPTY);
        ContainerComponent component = stack.getOrDefault(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT);
        component.copyTo(list);
        return list;
    }

    public static void setStoredArrows(ItemStack stack, DefaultedList<ItemStack> arrows) {
        boolean hasArrows = false;
        for (ItemStack arrow : arrows) {
            if (!arrow.isEmpty()) {
                hasArrows = true;
                break;
            }
        }
        if (hasArrows) {
            stack.set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(arrows));
        } else {
            stack.remove(DataComponentTypes.CONTAINER);
        }
    }

    @Override
    public boolean canBeNested() {
        return false;
    }

    public void dropStoredArrows(World world, PlayerEntity player, ItemStack quiverStack) {
        dropStoredArrows(world, player, player != null ? player.getX() : 0.0, player != null ? player.getY() : 0.0, player != null ? player.getZ() : 0.0, quiverStack);
    }

    public void dropStoredArrows(World world, PlayerEntity player, double x, double y, double z, ItemStack quiverStack) {
        if (world.isClient) return;
        DefaultedList<ItemStack> list = getStoredArrows(quiverStack);
        for (ItemStack stack : list) {
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
        quiverStack.remove(DataComponentTypes.CONTAINER);
    }

    public static ItemStack peekFirstArrow(ItemStack quiverStack) {
        DefaultedList<ItemStack> arrows = getStoredArrows(quiverStack);
        for (ItemStack arrow : arrows) {
            if (!arrow.isEmpty()) {
                return arrow;
            }
        }
        return ItemStack.EMPTY;
    }

    public static int getTotalArrows(ItemStack quiverStack) {
        DefaultedList<ItemStack> arrows = getStoredArrows(quiverStack);
        int total = 0;
        for (ItemStack arrow : arrows) {
            if (!arrow.isEmpty()) {
                total += arrow.getCount();
            }
        }
        return total;
    }
}
