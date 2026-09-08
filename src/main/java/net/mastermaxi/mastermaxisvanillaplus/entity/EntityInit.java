package net.mastermaxi.mastermaxisvanillaplus.entity;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.OstrichEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class EntityInit {
    public static final EntityType<MudBallEntity> MUD_BALL = Registry.register(
            Registries.ENTITY_TYPE,
            MASTERmaxisVanillaPlus.id("mud_ball"),
            EntityType.Builder.<MudBallEntity>create(MudBallEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25F, 0.25F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, MASTERmaxisVanillaPlus.id("mud_ball")))
    );

    public static final EntityType<OstrichEntity> OSTRICH = Registry.register(
            Registries.ENTITY_TYPE,
            MASTERmaxisVanillaPlus.id("ostrich"),
            EntityType.Builder.create(OstrichEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.9F, 1.4F)
                    .maxTrackingRange(8)
                    .trackingTickInterval(10)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, MASTERmaxisVanillaPlus.id("ostrich")))
    );

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Registering entities for" + MASTERmaxisVanillaPlus.MOD_ID);
    }
}
