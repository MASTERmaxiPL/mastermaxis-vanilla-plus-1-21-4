package net.mastermaxi.mastermaxisvanillaplus.world;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.DarkOakTrunkPlacer;

import java.util.OptionalInt;

public class ConfiguredFeaturesInit {
    public static final RegistryKey<ConfiguredFeature<?, ?>> BAOBAB_TREE = registryKey("baobab_tree");

    public static void boostrap(Registerable<ConfiguredFeature<?, ?>> context){
        register(context, BAOBAB_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(Blocks.BAOBAB_LOG),
                new DarkOakTrunkPlacer(10, 2, 12),
                BlockStateProvider.of(Blocks.BAOBAB_LEAVES),
                new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(4), 4),
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
        ).build());
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registryKey(String name){
            return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, MASTERmaxisVanillaPlus.id(name));
     }

     private static <FC extends FeatureConfig, F extends Feature<FC>> void register(
             Registerable<ConfiguredFeature<?, ?>> context,
             RegistryKey<ConfiguredFeature<?, ?>> key,
             F feature,
             FC config){
            context.register(key, new ConfiguredFeature<>(feature, config));
     }
}
