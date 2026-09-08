package net.mastermaxi.mastermaxisvanillaplus;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.mastermaxi.mastermaxisvanillaplus.datagen.*;
import net.mastermaxi.mastermaxisvanillaplus.world.ConfiguredFeaturesInit;
import net.mastermaxi.mastermaxisvanillaplus.world.PlacedFeaturesInit;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class MASTERmaxisVanillaPlusDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		MASTERmaxisVanillaPlus.LOGGER.info("Registering Data Generators");
		pack.addProvider(BlockTagProvider::new);
		MASTERmaxisVanillaPlus.LOGGER.info("Blocks passed");
		pack.addProvider(ItemTagProvider::new);
		MASTERmaxisVanillaPlus.LOGGER.info("Items passed");
		pack.addProvider(ModFluidTagProvider::new);
		MASTERmaxisVanillaPlus.LOGGER.info("Fluids passed");
		pack.addProvider(ModelProvider::new);
		MASTERmaxisVanillaPlus.LOGGER.info("Models passed");
		pack.addProvider(LootTableProvider::new);
		MASTERmaxisVanillaPlus.LOGGER.info("Loot Tables passed");
		pack.addProvider(RecipeProvider::new);
		MASTERmaxisVanillaPlus.LOGGER.info("Recipes passed");
		pack.addProvider(RegistryDataGeneration::new);
		MASTERmaxisVanillaPlus.LOGGER.info("DataGen passed");
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder){
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ConfiguredFeaturesInit::boostrap);
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, PlacedFeaturesInit::boostrap);
	}
}
