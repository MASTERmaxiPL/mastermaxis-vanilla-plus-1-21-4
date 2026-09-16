package net.mastermaxi.mastermaxisvanillaplus.loot;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.mastermaxi.mastermaxisvanillaplus.enchantment.ModEnchantments;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetDamageLootFunction;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;
import java.util.Set;

public class ModLootTableModifiers {
    private static final Set<RegistryKey<LootTable>> STRONGHOLD_CHESTS = Set.of(
            LootTables.STRONGHOLD_CORRIDOR_CHEST,
            LootTables.STRONGHOLD_CROSSING_CHEST,
            LootTables.STRONGHOLD_LIBRARY_CHEST
    );

    private static final Set<RegistryKey<LootTable>> SHIPWRECK_CHESTS = Set.of(
            LootTables.SHIPWRECK_SUPPLY_CHEST,
            LootTables.SHIPWRECK_MAP_CHEST,
            LootTables.SHIPWRECK_TREASURE_CHEST
    );

    private static final Set<RegistryKey<LootTable>> SHIPWRECK_AND_RUINED_PORTAL = Set.of(
            LootTables.SHIPWRECK_SUPPLY_CHEST,
            LootTables.SHIPWRECK_MAP_CHEST,
            LootTables.SHIPWRECK_TREASURE_CHEST,
            LootTables.RUINED_PORTAL_CHEST
    );

    private static final Set<RegistryKey<LootTable>> BASTION_REMNANTS_CHESTS = Set.of(
            LootTables.BASTION_TREASURE_CHEST,
            LootTables.BASTION_OTHER_CHEST,
            LootTables.BASTION_BRIDGE_CHEST,
            LootTables.BASTION_HOGLIN_STABLE_CHEST
    );

    private static final Set<RegistryKey<LootTable>> NETHER_FORTRESS_AND_BASTIONS = Set.of(
            LootTables.NETHER_BRIDGE_CHEST,
            LootTables.BASTION_TREASURE_CHEST,
            LootTables.BASTION_OTHER_CHEST,
            LootTables.BASTION_BRIDGE_CHEST,
            LootTables.BASTION_HOGLIN_STABLE_CHEST
    );

    public static void initialize() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) {
                return;
            }

            Optional<RegistryEntry.Reference<Enchantment>> curseOpt = registries.getOrThrow(RegistryKeys.ENCHANTMENT)
                    .getOptional(ModEnchantments.CURSE_OF_WELDING);
            if (curseOpt.isEmpty()) {
                return;
            }
            RegistryEntry<Enchantment> curseEntry = curseOpt.get();

            // 1. Iron Crossbow Scheme
            // Stronghold (5%)
            if (STRONGHOLD_CHESTS.contains(key)) {
                tableBuilder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.05f))
                        .with(ItemEntry.builder(ItemInit.IRON_CROSSBOW_SCHEME)));
            }

            // Mineshafts (2.5%)
            if (LootTables.ABANDONED_MINESHAFT_CHEST.equals(key)) {
                tableBuilder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.025f))
                        .with(ItemEntry.builder(ItemInit.IRON_CROSSBOW_SCHEME)));
            }

            // Shipwreck (2%)
            if (SHIPWRECK_CHESTS.contains(key)) {
                tableBuilder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.02f))
                        .with(ItemEntry.builder(ItemInit.IRON_CROSSBOW_SCHEME)));
            }

            // Fletcher house (3%)
            if (LootTables.VILLAGE_FLETCHER_CHEST.equals(key)) {
                tableBuilder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.03f))
                        .with(ItemEntry.builder(ItemInit.IRON_CROSSBOW_SCHEME)));
            }

            // 2. Nether Crossbow Scheme
            // Nether Fortress (5%)
            if (LootTables.NETHER_BRIDGE_CHEST.equals(key)) {
                tableBuilder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.05f))
                        .with(ItemEntry.builder(ItemInit.NETHER_CROSSBOW_SCHEME)));
            }

            // Bastion Remnants (6% in every part of structure)
            if (BASTION_REMNANTS_CHESTS.contains(key)) {
                tableBuilder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.06f))
                        .with(ItemEntry.builder(ItemInit.NETHER_CROSSBOW_SCHEME)));
            }

            // 3. Shipwreck & Ruined Portal:
            // A) Iron Crossbow: 4% chance, durability up to 75% (damage 0.25 - 0.95), 20% viewfinder, 65% welding if viewfinder
            // B) Standard Crossbow: 5% chance, durability up to 75% (damage 0.25 - 0.95), 30% viewfinder, 85% welding if viewfinder
            if (SHIPWRECK_AND_RUINED_PORTAL.contains(key)) {
                // A) Iron Crossbow pool (4%)
                tableBuilder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.04f))
                        .with(ItemEntry.builder(ItemInit.IRON_CROSSBOW)
                                .weight(80)
                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.95f))))
                        .with(ItemEntry.builder(ItemInit.IRON_CROSSBOW_VIEWFINDER)
                                .weight(7)
                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.95f))))
                        .with(ItemEntry.builder(ItemInit.IRON_CROSSBOW_VIEWFINDER)
                                .weight(13)
                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.95f)))
                                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(curseEntry, ConstantLootNumberProvider.create(1))))
                );

                // B) Standard Crossbow pool (5%)
                tableBuilder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.05f))
                        .with(ItemEntry.builder(Items.CROSSBOW)
                                .weight(140)
                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.95f))))
                        .with(ItemEntry.builder(ItemInit.CROSSBOW_VIEWFINDER)
                                .weight(9)
                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.95f))))
                        .with(ItemEntry.builder(ItemInit.CROSSBOW_VIEWFINDER)
                                .weight(51)
                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.95f)))
                                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(curseEntry, ConstantLootNumberProvider.create(1))))
                );
            }

            // 4. Nether Crossbow in Bastion Remnants & Nether Fortress
            // 7% chance, durability up to 60% (damage 0.40 - 0.95), 25% viewfinder, 50% welding if viewfinder
            if (NETHER_FORTRESS_AND_BASTIONS.contains(key)) {
                tableBuilder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.07f))
                        .with(ItemEntry.builder(ItemInit.NETHER_CROSSBOW)
                                .weight(6)
                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.40f, 0.95f))))
                        .with(ItemEntry.builder(ItemInit.NETHER_CROSSBOW_VIEWFINDER)
                                .weight(1)
                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.40f, 0.95f))))
                        .with(ItemEntry.builder(ItemInit.NETHER_CROSSBOW_VIEWFINDER)
                                .weight(1)
                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.40f, 0.95f)))
                                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(curseEntry, ConstantLootNumberProvider.create(1))))
                );
            }

            // 5. Curse of Welding via Fishing (treasure in every biome)
            if (LootTables.FISHING_TREASURE_GAMEPLAY.equals(key)) {
                tableBuilder.modifyPools(poolBuilder -> poolBuilder.with(ItemEntry.builder(Items.BOOK)
                        .weight(1)
                        .apply(new SetEnchantmentsLootFunction.Builder().enchantment(curseEntry, ConstantLootNumberProvider.create(1)))));
            }

        });
    }
}
