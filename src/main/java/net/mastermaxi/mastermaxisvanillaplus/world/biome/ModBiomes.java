package net.mastermaxi.mastermaxisvanillaplus.world.biome;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.entity.EntityInit;
import net.mastermaxi.mastermaxisvanillaplus.world.PlacedFeaturesInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.MiscPlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

public class ModBiomes {
    public static final RegistryKey<Biome> RUSTY_SAVANNA = RegistryKey.of(
            RegistryKeys.BIOME,
            MASTERmaxisVanillaPlus.id("rusty_savanna")
    );
    public static final RegistryKey<Biome> SCORCHED_SAVANNA = RegistryKey.of(
            RegistryKeys.BIOME,
            MASTERmaxisVanillaPlus.id("scorched_savanna")
    );

    public static void bootstrap(Registerable<Biome> context) {
        RegistryEntryLookup<PlacedFeature> placedFeatures = context.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        RegistryEntryLookup<ConfiguredCarver<?>> carvers = context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);

        context.register(RUSTY_SAVANNA, createRustySavanna(placedFeatures, carvers));
        context.register(SCORCHED_SAVANNA, createScorchedSavanna(placedFeatures, carvers));
    }

    public static Biome createRustySavanna(
            RegistryEntryLookup<PlacedFeature> placedFeatures,
            RegistryEntryLookup<ConfiguredCarver<?>> carvers
    ) {
        // 1. Generation Settings
        GenerationSettings.LookupBackedBuilder genSettings = new GenerationSettings.LookupBackedBuilder(placedFeatures, carvers);

        // 1. Terrain & Underground basics
        DefaultBiomeFeatures.addLandCarvers(genSettings);
        DefaultBiomeFeatures.addAmethystGeodes(genSettings);
        DefaultBiomeFeatures.addDungeons(genSettings);
        DefaultBiomeFeatures.addMineables(genSettings);
        DefaultBiomeFeatures.addSprings(genSettings);
        DefaultBiomeFeatures.addFrozenTopLayer(genSettings);

        // 2. Early vegetal decoration (tall grass)
        DefaultBiomeFeatures.addSavannaTallGrass(genSettings);

        // 3. Underground Ores & Disks
        DefaultBiomeFeatures.addDefaultOres(genSettings);
        DefaultBiomeFeatures.addDefaultDisks(genSettings);

        // 4. Flowers
        DefaultBiomeFeatures.addDefaultFlowers(genSettings);

        // 6. Grass & dead bushes
        DefaultBiomeFeatures.addSavannaGrass(genSettings);
        genSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_DEAD_BUSH);

        // 7. Mushrooms
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);

        // 8. General vegetation (sugar cane, pumpkins)
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);

        // 9. Melons, Cacti & Redthorn
        genSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_MELON_SPARSE);
        genSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_CACTUS_DECORATED);
        genSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, PlacedFeaturesInit.REDTHORN_PATCH_PLACED);

        // 2. Spawn Settings
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.HORSE, 1, 2, 6));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.DONKEY, 1, 1, 1));
        // Ostriches (dominant herds and small groups)
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityInit.OSTRICH, 10, 6, 12));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityInit.OSTRICH, 3, 2, 4));
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);

        // 3. Biome Effects (Colors & Atmosphere)
        float temperature = 1.2F;
        float f = MathHelper.clamp(temperature / 3.0F, -1.0F, 1.0F);
        int skyColor = MathHelper.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);

        BiomeEffects effects = new BiomeEffects.Builder()
                .waterColor(4159204)       // Savanna water
                .waterFogColor(329011)
                .fogColor(12638463)
                .skyColor(skyColor)
                .grassColor(0xC49C35)      // Złocisto-rdzawy odcień trawy z dodatkiem żółci
                .foliageColor(0xB89A38)    // Ciepły, złocisto-sawannowy odcień liści
                .moodSound(BiomeMoodSound.CAVE)
                .build();

        return new Biome.Builder()
                .precipitation(false)
                .temperature(temperature)
                .downfall(0.0F)
                .effects(effects)
                .spawnSettings(spawnSettings.build())
                .generationSettings(genSettings.build())
                .build();
    }

    public static Biome createScorchedSavanna(
            RegistryEntryLookup<PlacedFeature> placedFeatures,
            RegistryEntryLookup<ConfiguredCarver<?>> carvers
    ) {
        GenerationSettings.LookupBackedBuilder genSettings = new GenerationSettings.LookupBackedBuilder(placedFeatures, carvers);

        // 1. Terrain & Underground basics
        DefaultBiomeFeatures.addLandCarvers(genSettings);
        DefaultBiomeFeatures.addAmethystGeodes(genSettings);
        DefaultBiomeFeatures.addDungeons(genSettings);
        DefaultBiomeFeatures.addMineables(genSettings);

        // 2. Lakes (small surface lava pools only, NO water lakes / mud pools)
        genSettings.feature(GenerationStep.Feature.LAKES, PlacedFeaturesInit.LAVA_POOL_SCORCHED_PLACED);

        // 3. Surface structures (Mudstone rock formations with iron / rare termites or lava)
        genSettings.feature(GenerationStep.Feature.SURFACE_STRUCTURES, PlacedFeaturesInit.SCORCHED_MUDSTONE_DEPOSIT_PLACED);

        // 4. Underground Ores & Disks
        DefaultBiomeFeatures.addDefaultOres(genSettings);
        DefaultBiomeFeatures.addDefaultDisks(genSettings);

        // 5. Fluid springs (Lava springs only, NO water springs!)
        genSettings.feature(GenerationStep.Feature.FLUID_SPRINGS, MiscPlacedFeatures.SPRING_LAVA);

        // 6. Vegetal decoration:
        // - Dried leafless acacias
        // - Dead bushes
        // - Sporadic redthorn
        genSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, PlacedFeaturesInit.DRIED_ACACIA_PLACED);
        genSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_DEAD_BUSH_2);
        genSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, PlacedFeaturesInit.REDTHORN_SCORCHED_PLACED);

        // 7. Top layer modification
        DefaultBiomeFeatures.addFrozenTopLayer(genSettings);

        // 8. Spawn Settings:
        // "W biomie nie powinny generować się rzadne zwierzęta" -> NO animals (SpawnGroup.CREATURE)
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);

        // 9. Biome Effects (Scorched, hot atmosphere)
        float temperature = 2.0F;
        int skyColor = MathHelper.hsvToRgb(0.08F, 0.45F, 1.0F); // Warm, arid, slightly scorched sky

        BiomeEffects effects = new BiomeEffects.Builder()
                .waterColor(0x5b4838)       // Murky, scorched water
                .waterFogColor(0x32281e)
                .fogColor(12638463)
                .skyColor(skyColor)
                .grassColor(0x825e32)      // Thoroughly scorched dried grass color
                .foliageColor(0x73552c)    // Dried withered foliage color
                .moodSound(BiomeMoodSound.CAVE)
                .build();

        return new Biome.Builder()
                .precipitation(false)
                .temperature(temperature)
                .downfall(0.0F)
                .effects(effects)
                .spawnSettings(spawnSettings.build())
                .generationSettings(genSettings.build())
                .build();
    }
}
