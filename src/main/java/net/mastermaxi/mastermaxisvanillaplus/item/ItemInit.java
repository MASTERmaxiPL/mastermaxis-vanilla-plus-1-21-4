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
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemInit {
    @FunctionalInterface
    private interface ItemCreator {
        Item createItem(Item.Settings settings);
    }

    // ===== Existing items =====
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

    // ===== Shooting mechanics items =====

    // Heavy Arrow - crafted with iron nugget + stick + ostrich feather
    public static final Item HEAVY_ARROW = register("heavy_arrow", new Item.Settings().maxCount(64), HeavyArrowItem::new);

    // Bolt - crossbow ammunition
    public static final Item BOLT = register("bolt", new Item.Settings().maxCount(64), BoltItem::new);

    // Heavy Bow - upgraded bow with leather + slime + string
    public static final Item HEAVY_BOW = register("heavy_bow", new Item.Settings().maxDamage(250).enchantable(10), HeavyBowItem::new);

    // Heavy Hardened Bow - upgraded with iron + flint&steel
    public static final Item HEAVY_HARDENED_BOW = register("heavy_hardened_bow", new Item.Settings().maxDamage(320).enchantable(15), HeavyHardenedBowItem::new);

    // Viewfinder - compact scope item with spyglass-like zoom and purple tint
    public static final Item VIEWFINDER = register("viewfinder", new Item.Settings().maxCount(1), ViewfinderItem::new);

    // Iron Crossbow - 3 iron + slimeball + 2 strings
    public static final Item IRON_CROSSBOW = register("iron_crossbow", new Item.Settings().maxDamage(600).maxCount(1).enchantable(10), IronCrossbowItem::new);

    // Nether Crossbow - upgraded with gold + copper + slime
    public static final Item NETHER_CROSSBOW = register("nether_crossbow", new Item.Settings().maxDamage(800).maxCount(1).enchantable(15), NetherCrossbowItem::new);

    // Crossbow with Viewfinder variants
    public static final Item CROSSBOW_VIEWFINDER = register("crossbow_viewfinder", new Item.Settings().maxDamage(465).maxCount(1).enchantable(1), CrossbowViewfinderItem::new);
    public static final Item IRON_CROSSBOW_VIEWFINDER = register("iron_crossbow_viewfinder", new Item.Settings().maxDamage(600).maxCount(1).enchantable(10), IronCrossbowViewfinderItem::new);
    public static final Item NETHER_CROSSBOW_VIEWFINDER = register("nether_crossbow_viewfinder", new Item.Settings().maxDamage(800).maxCount(1).enchantable(15), NetherCrossbowViewfinderItem::new);

    // Schematics
    public static final Item IRON_CROSSBOW_SCHEME = register("iron_crossbow_scheme", new Item.Settings().maxCount(1), settings -> new SchematicItem(settings, Text.translatable("item.mastermaxisvanillaplus.iron_crossbow_scheme.desc")));
    public static final Item NETHER_CROSSBOW_SCHEME = register("nether_crossbow_scheme", new Item.Settings().maxCount(1), settings -> new SchematicItem(settings, Text.translatable("item.mastermaxisvanillaplus.nether_crossbow_scheme.desc")));
    public static final Item HEAVY_BOW_SCHEME = register("heavy_bow_scheme", new Item.Settings().maxCount(1), settings -> new SchematicItem(settings, Text.translatable("item.mastermaxisvanillaplus.heavy_bow_scheme.desc")));
    public static final Item HEAVY_HARDENED_BOW_SCHEME = register("heavy_hardened_bow_scheme", new Item.Settings().maxCount(1), settings -> new SchematicItem(settings, Text.translatable("item.mastermaxisvanillaplus.heavy_hardened_bow_scheme.desc")));

    private static Item register(String name, Item.Settings itemSettings, ItemCreator creator) {
        Identifier id = MASTERmaxisVanillaPlus.id(name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
        Item.Settings settings = itemSettings.registryKey(key);

        return Registry.register(Registries.ITEM, key, creator.createItem(settings));
    }

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Registering items for " + MASTERmaxisVanillaPlus.MOD_ID);

        // General Tab: Vanilla+
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

        // Combat Tab: Vanilla+ Combat (bows, crossbows, schematics, arrows)
        ItemGroupEvents.modifyEntriesEvent(ItemGroupsInit.COMBAT_ITEM_GROUP_KEY).register((entries) -> {
            // Bows
            entries.add(HEAVY_BOW);
            entries.add(HEAVY_HARDENED_BOW);
            // Crossbows & Optics
            entries.add(VIEWFINDER);
            entries.add(IRON_CROSSBOW);
            entries.add(NETHER_CROSSBOW);
            entries.add(CROSSBOW_VIEWFINDER);
            entries.add(IRON_CROSSBOW_VIEWFINDER);
            entries.add(NETHER_CROSSBOW_VIEWFINDER);
            // Schematics
            entries.add(HEAVY_BOW_SCHEME);
            entries.add(HEAVY_HARDENED_BOW_SCHEME);
            entries.add(IRON_CROSSBOW_SCHEME);
            entries.add(NETHER_CROSSBOW_SCHEME);
            // Projectiles & Ammunition
            entries.add(HEAVY_ARROW);
            entries.add(BOLT);
            addTippedVariants(entries);
        });

        net.minecraft.block.DispenserBlock.registerProjectileBehavior(OSTRICH_EGG);
    }

    private static void addTippedVariants(net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries entries) {
        Registries.POTION.streamEntries().forEach(entry -> {
            if (!entry.value().getEffects().isEmpty()) {
                entries.add(net.minecraft.component.type.PotionContentsComponent.createStack(HEAVY_ARROW, entry));
                entries.add(net.minecraft.component.type.PotionContentsComponent.createStack(BOLT, entry));
            }
        });
    }
}
