package net.mastermaxi.mastermaxisvanillaplus.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.entity.EntityInit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ItemInit {
    @FunctionalInterface
    private interface ItemCreator {
        Item createItem(Item.Settings settings);
    }

    public static final Item OSTRICH_FEATHER = register("ostrich_feather", new Item.Settings(), Item::new);
    public static final Item MUD_BALL = register("mud_ball", new Item.Settings().maxCount(32), MudBallItem::new);
    public static final Item OSTRICH_SPAWN_EGG = register("ostrich_spawn_egg", new Item.Settings(), settings -> new SpawnEggItem(EntityInit.OSTRICH, settings));

    private static Item register(String name, Item.Settings itemSettings, ItemCreator creator) {
        Identifier id = MASTERmaxisVanillaPlus.id(name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
        Item.Settings settings = itemSettings.registryKey(key);

        return Registry.register(Registries.ITEM, key, creator.createItem(settings));
    }

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Registering items for " + MASTERmaxisVanillaPlus.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroupsInit.MOD_ITEM_GROUP_KEY).register((entries) -> {
            entries.add(OSTRICH_FEATHER);
            entries.add(MUD_BALL);
            entries.add(OSTRICH_SPAWN_EGG);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((entries) -> {
            entries.add(OSTRICH_FEATHER);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((entries) -> {
            entries.add(MUD_BALL);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register((entries) -> {
            entries.add(OSTRICH_SPAWN_EGG);
        });
    }
}
