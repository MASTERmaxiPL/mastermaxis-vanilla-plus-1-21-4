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
    public static final Item NETHER_CROSSBOW_OXIDIZED = register("nether_crossbow_oxidized", new Item.Settings().maxDamage(800).maxCount(1).enchantable(15), NetherCrossbowOxidizedItem::new);
    public static final Item NETHER_CROSSBOW_OXIDIZED_VIEWFINDER = register("nether_crossbow_oxidized_viewfinder", new Item.Settings().maxDamage(800).maxCount(1).enchantable(15), NetherCrossbowOxidizedViewfinderItem::new);

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
            entries.add(NETHER_CROSSBOW_OXIDIZED);
            entries.add(NETHER_CROSSBOW_OXIDIZED_VIEWFINDER);
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

        // Interaction: Waxing nether crossbow with Honeycomb and scraping oxidized crossbow with Axe
        net.fabricmc.fabric.api.event.player.UseItemCallback.EVENT.register((player, world, hand) -> {
            net.minecraft.item.ItemStack held = player.getStackInHand(hand);
            net.minecraft.util.Hand otherHand = hand == net.minecraft.util.Hand.MAIN_HAND ? net.minecraft.util.Hand.OFF_HAND : net.minecraft.util.Hand.MAIN_HAND;
            net.minecraft.item.ItemStack other = player.getStackInHand(otherHand);

            // 1. Waxing: using Honeycomb on Nether Crossbow in other hand
            if (held.isOf(net.minecraft.item.Items.HONEYCOMB)
                    && other.getItem() instanceof NetherCrossbowItem && !NetherCrossbowItem.isWaxed(other)) {
                if (!world.isClient()) {
                    NetherCrossbowItem.setWaxed(other, true);
                    if (!player.getAbilities().creativeMode) {
                        held.decrement(1);
                    }
                    if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                        serverWorld.playSound(null, player.getX(), player.getY(), player.getZ(),
                                net.minecraft.sound.SoundEvents.ITEM_HONEYCOMB_WAX_ON, net.minecraft.sound.SoundCategory.PLAYERS, 1.0f, 1.0f);
                        serverWorld.spawnParticles(net.minecraft.particle.ParticleTypes.WAX_ON, player.getX(), player.getY() + 1.0, player.getZ(),
                                8, 0.3, 0.3, 0.3, 0.1);
                    }
                }
                return net.minecraft.util.ActionResult.SUCCESS;
            }

            // 2. Scraping: using Axe on Oxidized Nether Crossbow in other hand
            if (held.isIn(net.minecraft.registry.tag.ItemTags.AXES) && other.getItem() instanceof NetherCrossbowOxidizedItem) {
                if (!world.isClient()) {
                    net.minecraft.item.Item restoredItem = other.isOf(NETHER_CROSSBOW_OXIDIZED_VIEWFINDER) ?
                            NETHER_CROSSBOW_VIEWFINDER : NETHER_CROSSBOW;
                    net.minecraft.item.ItemStack restored = other.copyComponentsToNewStack(restoredItem, 1);
                    restored.setDamage(other.getDamage());
                    restored.set(net.minecraft.component.DataComponentTypes.ENCHANTABLE, restoredItem.getComponents().get(net.minecraft.component.DataComponentTypes.ENCHANTABLE));
                    NetherCrossbowItem.setOxidizeTime(restored, 0L);

                    player.setStackInHand(otherHand, restored);
                    if (!player.getAbilities().creativeMode) {
                        held.damage(1, (net.minecraft.server.world.ServerWorld) world, player instanceof net.minecraft.server.network.ServerPlayerEntity sp ? sp : null, item -> {});
                    }
                    if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                        serverWorld.playSound(null, player.getX(), player.getY(), player.getZ(),
                                net.minecraft.sound.SoundEvents.ITEM_AXE_SCRAPE, net.minecraft.sound.SoundCategory.PLAYERS, 1.0f, 1.0f);
                        serverWorld.spawnParticles(net.minecraft.particle.ParticleTypes.SCRAPE, player.getX(), player.getY() + 1.0, player.getZ(),
                                8, 0.3, 0.3, 0.3, 0.1);
                    }
                }
                return net.minecraft.util.ActionResult.SUCCESS;
            }

            return net.minecraft.util.ActionResult.PASS;
        });
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
