package net.mastermaxi.mastermaxisvanillaplus.world;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.mastermaxi.mastermaxisvanillaplus.world.tree.BaobabTrunkPlacer;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.OptionalInt;

public class ConfiguredFeaturesInit {
    public static final RegistryKey<ConfiguredFeature<?, ?>> BAOBAB_TREE = registryKey("baobab_tree");

    public static void boostrap(Registerable<ConfiguredFeature<?, ?>> context){
        context.register(BAOBAB_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(Blocks.BAOBAB_LOG),
                new BaobabTrunkPlacer(6, 2, 2),
                BlockStateProvider.of(Blocks.BAOBAB_LEAVES),
                new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(4), 4),
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
        ).build()));
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registryKey(String name){
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, MASTERmaxisVanillaPlus.id(name));
    }
}
