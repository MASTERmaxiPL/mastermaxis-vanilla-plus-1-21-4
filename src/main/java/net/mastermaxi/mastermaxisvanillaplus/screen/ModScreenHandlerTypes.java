package net.mastermaxi.mastermaxisvanillaplus.screen;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlerTypes {
    public static final ScreenHandlerType<FletchingTableScreenHandler> FLETCHING_TABLE = Registry.register(
            Registries.SCREEN_HANDLER,
            MASTERmaxisVanillaPlus.id("fletching_table"),
            new ScreenHandlerType<>(FletchingTableScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
    );

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Registering ScreenHandlerTypes for " + MASTERmaxisVanillaPlus.MOD_ID);
    }
}
