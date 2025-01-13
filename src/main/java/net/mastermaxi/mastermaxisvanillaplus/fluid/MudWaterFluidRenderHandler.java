package net.mastermaxi.mastermaxisvanillaplus.fluid;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.util.Identifier;

public class MudWaterFluidRenderHandler {
    public static void initialize() {
        FluidRenderHandler handler = new SimpleFluidRenderHandler(
                Identifier.of("minecraft:block/water_still"),
                Identifier.of("minecraft:block/water_flow"),
                Identifier.of("minecraft:block/water_overlay"),
                0x8B4513 // Muddy brown color
        );

        FluidRenderHandlerRegistry.INSTANCE.register(FluidInit.MUD_WATER_STILL, handler);
        FluidRenderHandlerRegistry.INSTANCE.register(FluidInit.MUD_WATER_FLOWING, handler);
    }
}