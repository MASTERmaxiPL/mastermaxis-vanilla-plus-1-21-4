package net.mastermaxi.mastermaxisvanillaplus.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.entity.EntityInit;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
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
    public static final Item OSTRICH_EGG = register("ostrich_egg", new Item.Settings().maxCount(16), OstrichEggItem::new);

    public static final Item BAOBAB_FRUIT = register("baobab_fruit", new Item.Settings().food(new FoodComponent(1, 0.4F, false)), Item::new);

    public static final ConsumableComponent GUI_JUICE_CONSUMABLE = ConsumableComponents.drink()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.SPEED, 15 * 20, 0)))
            .build();
    public static final Item GUI_JUICE = register("gui_juice", new Item.Settings().food(new FoodComponent(4, 4.0F, true), GUI_JUICE_CONSUMABLE).useRemainder(Items.GLASS_BOTTLE).maxCount(16), Item::new);

    public static final Item MONKEY_BREAD = register("monkey_bread", new Item.Settings().food(new FoodComponent(5, 7.0F, false)), MonkeyBreadItem::new);

    public static final Item BAOBAB_STEW = register("baobab_stew", new Item.Settings().food(new FoodComponent(8, 9.0F, false)).useRemainder(Items.BOWL).maxCount(1), Item::new);

    public static final Item TRUFFLE = register("truffle", new Item.Settings().food(new FoodComponent(2, 2.5F, false)), Item::new);

    public static final ConsumableComponent TRUFFLE_PORKCHOP_CONSUMABLE = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HASTE, 40 * 20, 0)))
            .build();
    public static final Item TRUFFLE_PORKCHOP = register("truffle_porkchop", new Item.Settings().food(new FoodComponent(10, 14.0F, true), TRUFFLE_PORKCHOP_CONSUMABLE), Item::new);

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
            entries.add(OSTRICH_EGG);
            entries.add(OSTRICH_SPAWN_EGG);
            entries.add(BAOBAB_FRUIT);
            entries.add(GUI_JUICE);
            entries.add(MONKEY_BREAD);
            entries.add(BAOBAB_STEW);
            entries.add(TRUFFLE);
            entries.add(TRUFFLE_PORKCHOP);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((entries) -> {
            entries.add(OSTRICH_FEATHER);
            entries.add(OSTRICH_EGG);
            entries.add(BAOBAB_FRUIT);
            entries.add(TRUFFLE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(OSTRICH_EGG);
            entries.add(BAOBAB_FRUIT);
            entries.add(GUI_JUICE);
            entries.add(MONKEY_BREAD);
            entries.add(BAOBAB_STEW);
            entries.add(TRUFFLE);
            entries.add(TRUFFLE_PORKCHOP);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(MUD_BALL));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> entries.add(OSTRICH_SPAWN_EGG));

        net.minecraft.block.DispenserBlock.registerProjectileBehavior(OSTRICH_EGG);
    }
}
