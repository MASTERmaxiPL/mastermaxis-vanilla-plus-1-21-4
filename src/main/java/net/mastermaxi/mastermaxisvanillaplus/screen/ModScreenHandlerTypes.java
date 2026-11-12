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

    public static final ScreenHandlerType<BackpackScreenHandler> BACKPACK = Registry.register(
            Registries.SCREEN_HANDLER,
            MASTERmaxisVanillaPlus.id("backpack"),
            new net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType<>(
                    BackpackScreenHandler::new,
                    net.mastermaxi.mastermaxisvanillaplus.network.BackpackPayload.PACKET_CODEC
            )
    );

    public static final ScreenHandlerType<QuiverScreenHandler> QUIVER = Registry.register(
            Registries.SCREEN_HANDLER,
            MASTERmaxisVanillaPlus.id("quiver"),
            new ScreenHandlerType<>(QuiverScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
    );

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Registering ScreenHandlerTypes for " + MASTERmaxisVanillaPlus.MOD_ID);
    }
}
