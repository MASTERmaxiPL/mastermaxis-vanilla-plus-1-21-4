package net.mastermaxi.mastermaxisvanillaplus;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.mastermaxi.mastermaxisvanillaplus.block.BlockInit;
import net.mastermaxi.mastermaxisvanillaplus.entity.EntityInit;
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.OstrichEntity;
import net.mastermaxi.mastermaxisvanillaplus.fluid.FluidInit;
import net.mastermaxi.mastermaxisvanillaplus.fluid.MudWaterFluidRenderHandler;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MASTERmaxisVanillaPlus implements ModInitializer {
	public static final String MOD_ID = "mastermaxisvanillaplus";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("trying to init fluids");
		FluidInit.initialize();
		MudWaterFluidRenderHandler.initialize();
		LOGGER.info("trying to init blocks");
		BlockInit.initialize();
		LOGGER.info("trying to init items");
		ItemInit.initialize();
		LOGGER.info("trying to init entities");
		EntityInit.initialize();
		LOGGER.info("trying to register ostrich attributes");
		FabricDefaultAttributeRegistry.register(EntityInit.OSTRICH, OstrichEntity.createAttributes());

		LOGGER.info("Hello Fabric world!");
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}