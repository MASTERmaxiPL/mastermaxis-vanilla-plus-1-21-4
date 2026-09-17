package net.mastermaxi.mastermaxisvanillaplus.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BlockEntities {
    public static final BlockEntityType<InfectedWoodBlockEntity> INFECTED_WOOD = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            MASTERmaxisVanillaPlus.id("infected_wood"),
            FabricBlockEntityTypeBuilder.create(InfectedWoodBlockEntity::new, Blocks.INFECTED_WOOD).build()
    );

    public static final BlockEntityType<FletchingTableBlockEntity> FLETCHING_TABLE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            MASTERmaxisVanillaPlus.id("fletching_table"),
            FabricBlockEntityTypeBuilder.create(FletchingTableBlockEntity::new, net.minecraft.block.Blocks.FLETCHING_TABLE).build()
    );

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Registering block entities for " + MASTERmaxisVanillaPlus.MOD_ID);
    }
}
