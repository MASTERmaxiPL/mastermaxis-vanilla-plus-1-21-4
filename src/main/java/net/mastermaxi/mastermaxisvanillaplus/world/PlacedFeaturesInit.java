package net.mastermaxi.mastermaxisvanillaplus.world;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.feature.VegetationConfiguredFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.List;

public class PlacedFeaturesInit {
    public static final RegistryKey<PlacedFeature> BAOBAB_TREE_PLACED = registryKey("baobab_tree_placed");
    public static final RegistryKey<PlacedFeature> BAOBAB_TREE_PLATEAU_PLACED = registryKey("baobab_tree_plateau_placed");
    public static final RegistryKey<PlacedFeature> SPARSE_SAVANNA_TREES_PLACED = registryKey("sparse_savanna_trees_placed");
    public static final RegistryKey<PlacedFeature> MUD_POOL_PLACED = registryKey("mud_pool_placed");
    public static final RegistryKey<PlacedFeature> TERMITE_MOUND_PLACED = registryKey("termite_mound_placed");
    public static final RegistryKey<PlacedFeature> MUDSTONE_DEPOSIT_PLACED = registryKey("mudstone_deposit_placed");

    public static void boostrap(Registerable<PlacedFeature> context){
        var configFeatures = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        // 1. Baobab tree (rare for Savanna)
        register(
                context,
                BAOBAB_TREE_PLACED,
                configFeatures.getOrThrow(ConfiguredFeaturesInit.BAOBAB_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(RarityFilterPlacementModifier.of(32), Blocks.BAOBAB_SAPLING)
        );

        // 2. Baobab tree (very rare for Savanna Plateau)
        register(
                context,
                BAOBAB_TREE_PLATEAU_PLACED,
                configFeatures.getOrThrow(ConfiguredFeaturesInit.BAOBAB_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(RarityFilterPlacementModifier.of(72), Blocks.BAOBAB_SAPLING)
        );

        // 3. Sparser savanna trees (replaces dense vanilla trees_savanna)
        register(
                context,
                SPARSE_SAVANNA_TREES_PLACED,
                configFeatures.getOrThrow(VegetationConfiguredFeatures.TREES_SAVANNA),
                VegetationPlacedFeatures.treeModifiers(PlacedFeatures.createCountExtraModifier(0, 0.5f, 1))
        );

        // 4. Mud pool (rare wetland feature)
        register(
                context,
                MUD_POOL_PLACED,
                configFeatures.getOrThrow(ConfiguredFeaturesInit.MUD_POOL),
                List.of(
                        RarityFilterPlacementModifier.of(28),
                        SquarePlacementModifier.of(),
                        PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                        BiomePlacementModifier.of()
                )
        );

        // 5. Termite mound (occasional colony)
        register(
                context,
                TERMITE_MOUND_PLACED,
                configFeatures.getOrThrow(ConfiguredFeaturesInit.TERMITE_MOUND),
                List.of(
                        RarityFilterPlacementModifier.of(64),
                        SquarePlacementModifier.of(),
                        PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                        BiomePlacementModifier.of()
                )
        );

        // 6. Mudstone deposit (very sporadic landmark)
        register(
                context,
                MUDSTONE_DEPOSIT_PLACED,
                configFeatures.getOrThrow(ConfiguredFeaturesInit.MUDSTONE_DEPOSIT),
                List.of(
                        RarityFilterPlacementModifier.of(80),
                        SquarePlacementModifier.of(),
                        PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                        BiomePlacementModifier.of()
                )
        );
    }

    public static RegistryKey<PlacedFeature> registryKey(String name){
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, MASTERmaxisVanillaPlus.id(name));
    }

    private static void register(
            Registerable<PlacedFeature> context,
            RegistryKey<PlacedFeature> key,
            RegistryEntry<ConfiguredFeature<?, ?>> config,
            List<PlacementModifier> modifiers){
        context.register(key, new PlacedFeature(config, List.copyOf(modifiers)));
    }
}
