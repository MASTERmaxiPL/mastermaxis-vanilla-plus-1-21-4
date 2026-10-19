package net.mastermaxi.mastermaxisvanillaplus.component;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModDataComponentTypes {
    public static final ComponentType<ContainerComponent> UPGRADES = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            MASTERmaxisVanillaPlus.id("upgrades"),
            ComponentType.<ContainerComponent>builder()
                    .codec(ContainerComponent.CODEC)
                    .packetCodec(ContainerComponent.PACKET_CODEC)
                    .cache()
                    .build()
    );

    public static final ComponentType<ContainerComponent> QUIVER = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            MASTERmaxisVanillaPlus.id("quiver"),
            ComponentType.<ContainerComponent>builder()
                    .codec(ContainerComponent.CODEC)
                    .packetCodec(ContainerComponent.PACKET_CODEC)
                    .cache()
                    .build()
    );

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Registering Data Component Types for " + MASTERmaxisVanillaPlus.MOD_ID);
    }
}
