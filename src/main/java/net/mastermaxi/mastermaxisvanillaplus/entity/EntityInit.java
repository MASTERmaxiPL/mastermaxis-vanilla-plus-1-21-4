package net.mastermaxi.mastermaxisvanillaplus.entity;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.BackpackEntity;
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.OstrichEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import net.mastermaxi.mastermaxisvanillaplus.mixin.SpawnRestrictionAccessor;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;

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
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, MASTERmaxisVanillaPlus.id("ostrich")))
    );

    public static final EntityType<OstrichEggEntity> OSTRICH_EGG = Registry.register(
            Registries.ENTITY_TYPE,
            MASTERmaxisVanillaPlus.id("ostrich_egg"),
            EntityType.Builder.<OstrichEggEntity>create(OstrichEggEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25F, 0.25F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, MASTERmaxisVanillaPlus.id("ostrich_egg")))
    );

    public static final EntityType<HeavyArrowEntity> HEAVY_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            MASTERmaxisVanillaPlus.id("heavy_arrow"),
            EntityType.Builder.<HeavyArrowEntity>create(HeavyArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5F, 0.5F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, MASTERmaxisVanillaPlus.id("heavy_arrow")))
    );

    public static final EntityType<BoltEntity> BOLT = Registry.register(
            Registries.ENTITY_TYPE,
            MASTERmaxisVanillaPlus.id("bolt"),
            EntityType.Builder.<BoltEntity>create(BoltEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5F, 0.5F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, MASTERmaxisVanillaPlus.id("bolt")))
    );

    public static final EntityType<BackpackEntity> BACKPACK = Registry.register(
            Registries.ENTITY_TYPE,
            MASTERmaxisVanillaPlus.id("backpack"),
            EntityType.Builder.create(BackpackEntity::new, SpawnGroup.MISC)
                    .dimensions(0.6F, 0.6F)
                    .maxTrackingRange(8)
                    .trackingTickInterval(5)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, MASTERmaxisVanillaPlus.id("backpack")))
    );

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Registering entities for" + MASTERmaxisVanillaPlus.MOD_ID);
        SpawnRestrictionAccessor.callRegister(
                OSTRICH,
                SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                AnimalEntity::isValidNaturalSpawn
        );
    }
}
