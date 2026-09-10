package net.mastermaxi.mastermaxisvanillaplus.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.world.PlacedFeaturesInit;
import net.mastermaxi.mastermaxisvanillaplus.world.feature.ModFeatures;
import net.mastermaxi.mastermaxisvanillaplus.world.tree.BaobabTrunkPlacer;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

import java.util.Objects;

public class WorldGenerationInit {
    public static void initialize() {
        Objects.requireNonNull(BaobabTrunkPlacer.BAOBAB_TRUNK_PLACER);
        ModFeatures.initialize();

        var savannaBiomeSelector = BiomeSelectors.includeByKey(
                BiomeKeys.SAVANNA,
                BiomeKeys.SAVANNA_PLATEAU
        );

        var allSavannaSelector = BiomeSelectors.includeByKey(
                BiomeKeys.SAVANNA,
                BiomeKeys.SAVANNA_PLATEAU,
                BiomeKeys.WINDSWEPT_SAVANNA
        );

        // 1. Mud pools (Lakes step) - Savanna & Savanna Plateau
        BiomeModifications.addFeature(
                savannaBiomeSelector,
                GenerationStep.Feature.LAKES,
                PlacedFeaturesInit.MUD_POOL_PLACED
        );

        // 2. Termite mounds (Surface structures step) - All savannas
        BiomeModifications.addFeature(
                allSavannaSelector,
                GenerationStep.Feature.SURFACE_STRUCTURES,
                PlacedFeaturesInit.TERMITE_MOUND_PLACED
        );

        // 3. Mudstone deposits (Surface structures step) - Savanna & Savanna Plateau
        BiomeModifications.addFeature(
                savannaBiomeSelector,
                GenerationStep.Feature.SURFACE_STRUCTURES,
                PlacedFeaturesInit.MUDSTONE_DEPOSIT_PLACED
        );

        // 4. Savanna tree density adjustment:
        // Remove dense vanilla acacia trees from SAVANNA
        BiomeModifications.create(MASTERmaxisVanillaPlus.id("reduce_savanna_trees"))
                .add(
                        ModificationPhase.REMOVALS,
                        BiomeSelectors.includeByKey(BiomeKeys.SAVANNA),
                        context -> context.getGenerationSettings().removeFeature(
                                GenerationStep.Feature.VEGETAL_DECORATION,
                                VegetationPlacedFeatures.TREES_SAVANNA
                        )
                );

        // Add sparser savanna trees to SAVANNA
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(BiomeKeys.SAVANNA),
                GenerationStep.Feature.VEGETAL_DECORATION,
                PlacedFeaturesInit.SPARSE_SAVANNA_TREES_PLACED
        );

        // 5. Baobab trees:
        // Rare in SAVANNA
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(BiomeKeys.SAVANNA),
                GenerationStep.Feature.VEGETAL_DECORATION,
                PlacedFeaturesInit.BAOBAB_TREE_PLACED
        );

        // Very rare in SAVANNA_PLATEAU (and WINDSWEPT_SAVANNA)
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(BiomeKeys.SAVANNA_PLATEAU, BiomeKeys.WINDSWEPT_SAVANNA),
                GenerationStep.Feature.VEGETAL_DECORATION,
                PlacedFeaturesInit.BAOBAB_TREE_PLATEAU_PLACED
        );

        // 6. Ostriches in Savanna:
        // Dominant herds: 6 to 12 individuals (weight 10)
        BiomeModifications.addSpawn(
                allSavannaSelector,
                net.minecraft.entity.SpawnGroup.CREATURE,
                net.mastermaxi.mastermaxisvanillaplus.entity.EntityInit.OSTRICH,
                10,
                6,
                12
        );

        // Occasional smaller groups: 2 to 4 individuals (weight 3)
        BiomeModifications.addSpawn(
                allSavannaSelector,
                net.minecraft.entity.SpawnGroup.CREATURE,
                net.mastermaxi.mastermaxisvanillaplus.entity.EntityInit.OSTRICH,
                3,
                2,
                4
        );
    }
}
