package net.mastermaxi.mastermaxisvanillaplus.world;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class ConfiguredFeaturesInit {
    public static void boostrap(Registerable<ConfiguredFeature<?, ?>> context){

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
