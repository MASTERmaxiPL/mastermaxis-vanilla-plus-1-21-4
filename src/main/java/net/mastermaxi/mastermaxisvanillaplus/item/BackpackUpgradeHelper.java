package net.mastermaxi.mastermaxisvanillaplus.item;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.mastermaxi.mastermaxisvanillaplus.network.PlayBackpackJukeboxS2CPayload;
import net.mastermaxi.mastermaxisvanillaplus.network.StopBackpackJukeboxS2CPayload;
import net.mastermaxi.mastermaxisvanillaplus.screen.BackpackScreenHandler;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BackpackUpgradeHelper {

    // Jukebox tracking
    public static class JukeboxState {
        public final ItemStack disc;
        public final RegistryEntry<JukeboxSong> song;
        public int remainingTicks;
        public BlockPos startPos;
        public final ItemStack backpackStack;
        public final BackpackItem.BackpackTier tier;

        public JukeboxState(ItemStack disc, RegistryEntry<JukeboxSong> song, int remainingTicks, BlockPos startPos, ItemStack backpackStack, BackpackItem.BackpackTier tier) {
            this.disc = disc;
            this.song = song;
            this.remainingTicks = remainingTicks;
            this.startPos = startPos;
            this.backpackStack = backpackStack;
            this.tier = tier;
        }
    }

    private static final Map<UUID, JukeboxState> ACTIVE_JUKEBOXES = new HashMap<>();

    public static int getBackpackLanternLuminance(LivingEntity entity) {
        AccessoriesCapability cap = AccessoriesCapability.get(entity);
        if (cap == null) return 0;
        AccessoriesContainer back = cap.getContainers().get("back");
        if (back == null) return 0;
        ItemStack stack = back.getAccessories().getStack(0);
        if (stack.getItem() instanceof BackpackItem bp) {
            DefaultedList<ItemStack> upgrades = bp.getStoredUpgrades(stack);
            boolean hasSoulLantern = false;
            for (ItemStack u : upgrades) {
                if (u.isOf(Items.LANTERN)) {
                    return 15;
                } else if (u.isOf(Items.SOUL_LANTERN)) {
                    hasSoulLantern = true;
                }
            }
            if (hasSoulLantern) {
                return 10;
            }
        }
        return 0;
    }

    public static void tick(ServerPlayerEntity player, ItemStack backpackStack, BackpackItem bp) {
        DefaultedList<ItemStack> upgrades = bp.getStoredUpgrades(backpackStack);
        boolean hasJukebox = false;
        boolean hasMagnet = false;

        for (ItemStack u : upgrades) {
            if (u.isOf(Items.JUKEBOX)) hasJukebox = true;
            else if (u.isOf(ItemInit.MAGNET)) hasMagnet = true;
        }

        if (hasMagnet) {
            tickMagnet(player, backpackStack, bp);
        }

        tickJukebox(player, hasJukebox);
    }

    // ================= MAGNET UPGRADE =================

    private static void tickMagnet(ServerPlayerEntity player, ItemStack backpackStack, BackpackItem bp) {
        Box box = new Box(
                player.getX() - 3.0, player.getY() - 1.0, player.getZ() - 3.0,
                player.getX() + 3.0, player.getY() + 2.0, player.getZ() + 3.0
        );

        List<ItemEntity> items = player.getWorld().getEntitiesByClass(
                ItemEntity.class, box,
                e -> e.isAlive() && !e.cannotPickup()
        );

        if (items.isEmpty()) return;

        for (ItemEntity itemEntity : items) {
            ItemStack stack = itemEntity.getStack();
            if (stack.isEmpty()) continue;

            int initialCount = stack.getCount();
            ItemStack remaining = insertIntoBackpackStorage(player, backpackStack, bp, stack);
            int inserted = initialCount - remaining.getCount();

            if (inserted > 0) {
                player.sendPickup(itemEntity, inserted);
                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
                        (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2F + 1.0F);

                if (remaining.isEmpty()) {
                    itemEntity.discard();
                } else {
                    itemEntity.setStack(remaining);
                }
            }
        }
    }

    private static ItemStack insertIntoBackpackStorage(ServerPlayerEntity player, ItemStack backpackStack, BackpackItem bp, ItemStack toInsert) {
        if (player.currentScreenHandler instanceof BackpackScreenHandler handler && handler.canUse(player)) {
            return handler.insertIntoStorage(toInsert);
        }

        if (bp.getTier() == BackpackItem.BackpackTier.ENDER) {
            return player.getEnderChestInventory().addStack(toInsert);
        }

        DefaultedList<ItemStack> stored = bp.getStoredItems(backpackStack);
        ItemStack remaining = toInsert.copy();

        // 1. Merge with existing stacks
        for (int i = 0; i < stored.size(); i++) {
            ItemStack slot = stored.get(i);
            if (!slot.isEmpty() && ItemStack.areItemsAndComponentsEqual(slot, remaining) && slot.getCount() < slot.getMaxCount()) {
                int canAdd = Math.min(remaining.getCount(), slot.getMaxCount() - slot.getCount());
                slot.increment(canAdd);
                remaining.decrement(canAdd);
                if (remaining.isEmpty()) {
                    bp.setStoredItems(backpackStack, stored);
                    markBackpackChanged(player);
                    return ItemStack.EMPTY;
                }
            }
        }

        // 2. Insert into empty slots
        for (int i = 0; i < stored.size(); i++) {
            if (stored.get(i).isEmpty()) {
                stored.set(i, remaining.copy());
                remaining.setCount(0);
                bp.setStoredItems(backpackStack, stored);
                markBackpackChanged(player);
                return ItemStack.EMPTY;
            }
        }

        if (remaining.getCount() != toInsert.getCount()) {
            bp.setStoredItems(backpackStack, stored);
            markBackpackChanged(player);
        }

        return remaining;
    }

    // ================= JUKEBOX UPGRADE =================

    public static boolean isPlayingJukebox(ServerPlayerEntity player) {
        return ACTIVE_JUKEBOXES.containsKey(player.getUuid());
    }

    public static void startJukebox(ServerPlayerEntity player, ItemStack disc, RegistryEntry<JukeboxSong> song, ItemStack backpackStack, BackpackItem.BackpackTier tier) {
        if (ACTIVE_JUKEBOXES.containsKey(player.getUuid())) {
            stopJukebox(player, true);
        }

        PlayBackpackJukeboxS2CPayload payload = new PlayBackpackJukeboxS2CPayload(player.getId(), song);
        ServerPlayNetworking.send(player, payload);
        for (ServerPlayerEntity trackingPlayer : PlayerLookup.tracking(player)) {
            ServerPlayNetworking.send(trackingPlayer, payload);
        }

        player.sendMessage(Text.translatable("record.nowPlaying", song.value().description()), true);

        int lengthTicks = song.value().getLengthInTicks();
        ACTIVE_JUKEBOXES.put(player.getUuid(), new JukeboxState(disc.copy(), song, lengthTicks, player.getBlockPos(), backpackStack, tier));
    }

    public static ItemStack stopJukebox(ServerPlayerEntity player, boolean returnToInventory) {
        JukeboxState state = ACTIVE_JUKEBOXES.remove(player.getUuid());
        if (state == null) return ItemStack.EMPTY;

        StopBackpackJukeboxS2CPayload payload = new StopBackpackJukeboxS2CPayload(player.getId());
        ServerPlayNetworking.send(player, payload);
        for (ServerPlayerEntity trackingPlayer : PlayerLookup.tracking(player)) {
            ServerPlayNetworking.send(trackingPlayer, payload);
        }

        if (returnToInventory) {
            returnMusicDisc(player, state.disc, state.backpackStack, state.tier);
            return ItemStack.EMPTY;
        }
        return state.disc;
    }

    private static void tickJukebox(ServerPlayerEntity player, boolean hasJukebox) {
        JukeboxState state = ACTIVE_JUKEBOXES.get(player.getUuid());
        if (state == null) return;

        if (!player.isAlive() || !hasJukebox) {
            stopJukebox(player, true);
            return;
        }

        if (player.age % 20 == 0) {
            ServerWorld serverWorld = (ServerWorld) player.getWorld();
            serverWorld.spawnParticles(ParticleTypes.NOTE,
                    player.getX(), player.getY() + 1.2, player.getZ(),
                    1, 0.3, 0.3, 0.3, player.getRandom().nextDouble());
        }

        state.remainingTicks--;
        if (state.remainingTicks <= 0) {
            stopJukebox(player, true);
        }
    }

    public static void returnMusicDisc(PlayerEntity player, ItemStack disc, ItemStack backpackStack, BackpackItem.BackpackTier tier) {
        if (disc.isEmpty()) return;

        PlayerInventory playerInv = player.getInventory();

        // 1. Hotbar first: slots 0..8
        for (int i = 0; i < 9; i++) {
            ItemStack hotbarStack = playerInv.getStack(i);
            if (hotbarStack.isEmpty()) {
                playerInv.setStack(i, disc.copy());
                disc.setCount(0);
                return;
            } else if (ItemStack.areItemsAndComponentsEqual(hotbarStack, disc) && hotbarStack.getCount() < hotbarStack.getMaxCount()) {
                int toAdd = Math.min(disc.getCount(), hotbarStack.getMaxCount() - hotbarStack.getCount());
                hotbarStack.increment(toAdd);
                disc.decrement(toAdd);
                if (disc.isEmpty()) return;
            }
        }

        // 2. Backpack next
        if (player.currentScreenHandler instanceof BackpackScreenHandler handler && handler.canUse(player)) {
            disc = handler.insertIntoStorage(disc);
            if (disc.isEmpty()) return;
        } else if (tier == BackpackItem.BackpackTier.ENDER) {
            SimpleInventory enderInv = player.getEnderChestInventory();
            disc = enderInv.addStack(disc);
            if (disc.isEmpty()) return;
        } else if (!backpackStack.isEmpty() && backpackStack.getItem() instanceof BackpackItem backpackItem) {
            DefaultedList<ItemStack> stored = backpackItem.getStoredItems(backpackStack);
            for (int i = 0; i < stored.size(); i++) {
                ItemStack slotStack = stored.get(i);
                if (!slotStack.isEmpty() && ItemStack.areItemsAndComponentsEqual(slotStack, disc) && slotStack.getCount() < slotStack.getMaxCount()) {
                    int toAdd = Math.min(disc.getCount(), slotStack.getMaxCount() - slotStack.getCount());
                    slotStack.increment(toAdd);
                    disc.decrement(toAdd);
                    if (disc.isEmpty()) {
                        backpackItem.setStoredItems(backpackStack, stored);
                        markBackpackChanged(player);
                        return;
                    }
                }
            }
            for (int i = 0; i < stored.size(); i++) {
                if (stored.get(i).isEmpty()) {
                    stored.set(i, disc.copy());
                    disc.setCount(0);
                    backpackItem.setStoredItems(backpackStack, stored);
                    markBackpackChanged(player);
                    return;
                }
            }
            backpackItem.setStoredItems(backpackStack, stored);
            markBackpackChanged(player);
        }

        // 3. Main inventory next: slots 9..35
        for (int i = 9; i < 36; i++) {
            ItemStack invStack = playerInv.getStack(i);
            if (invStack.isEmpty()) {
                playerInv.setStack(i, disc.copy());
                disc.setCount(0);
                return;
            } else if (ItemStack.areItemsAndComponentsEqual(invStack, disc) && invStack.getCount() < invStack.getMaxCount()) {
                int toAdd = Math.min(disc.getCount(), invStack.getMaxCount() - invStack.getCount());
                invStack.increment(toAdd);
                disc.decrement(toAdd);
                if (disc.isEmpty()) return;
            }
        }

        // 4. Drop on ground
        if (!disc.isEmpty()) {
            player.dropItem(disc.copy(), false);
            disc.setCount(0);
        }
    }

    public static void onUnequip(ServerPlayerEntity player) {
        if (ACTIVE_JUKEBOXES.containsKey(player.getUuid())) {
            stopJukebox(player, true);
        }
    }

    public static void onPlayerDisconnect(ServerPlayerEntity player) {
        if (ACTIVE_JUKEBOXES.containsKey(player.getUuid())) {
            stopJukebox(player, true);
        }
    }

    public static void onPlayerRespawn(ServerPlayerEntity oldPlayer) {
        if (ACTIVE_JUKEBOXES.containsKey(oldPlayer.getUuid())) {
            stopJukebox(oldPlayer, true);
        }
    }

    public static void markBackpackChanged(PlayerEntity player) {
        AccessoriesCapability cap = AccessoriesCapability.get(player);
        if (cap != null) {
            AccessoriesContainer back = cap.getContainers().get("back");
            if (back != null) back.markChanged();
        }
    }
}
