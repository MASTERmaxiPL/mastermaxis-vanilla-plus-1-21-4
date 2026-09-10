package net.mastermaxi.mastermaxisvanillaplus.world.feature;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class ModFeatures {
    public static final Feature<DefaultFeatureConfig> MUD_POOL = register("mud_pool", new MudPoolFeature(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> TERMITE_MOUND = register("termite_mound", new TermiteMoundFeature(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> MUDSTONE_DEPOSIT = register("mudstone_deposit", new MudstoneDepositFeature(DefaultFeatureConfig.CODEC));

    private static <C extends net.minecraft.world.gen.feature.FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        return Registry.register(Registries.FEATURE, MASTERmaxisVanillaPlus.id(name), feature);
    }

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Registering features for " + MASTERmaxisVanillaPlus.MOD_ID);
    }
}
