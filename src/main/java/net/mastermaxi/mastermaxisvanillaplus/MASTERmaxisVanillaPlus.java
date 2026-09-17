package net.mastermaxi.mastermaxisvanillaplus;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.mastermaxi.mastermaxisvanillaplus.block.BlockInit;
import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.mastermaxi.mastermaxisvanillaplus.entity.EntityInit;
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.OstrichEntity;
import net.mastermaxi.mastermaxisvanillaplus.fluid.FluidInit;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemGroupsInit;
import net.mastermaxi.mastermaxisvanillaplus.world.gen.WorldGenerationInit;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MASTERmaxisVanillaPlus implements ModInitializer {
	public static final String MOD_ID = "mastermaxisvanillaplus";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("trying to init item groups");
		ItemGroupsInit.initialize();
		LOGGER.info("trying to init fluids");
		FluidInit.initialize();
		LOGGER.info("trying to init blocks");
		Blocks.initialize();
		BlockInit.initialize();
		net.mastermaxi.mastermaxisvanillaplus.block.BlockEntities.initialize();

		StrippableBlockRegistry.register(Blocks.BAOBAB_LOG, Blocks.STRIPPED_BAOBAB_LOG);
		StrippableBlockRegistry.register(Blocks.BAOBAB_WOOD, Blocks.STRIPPED_BAOBAB_WOOD);

		addFlammableBlock(Blocks.BAOBAB_LOG, 5, 5);
		addFlammableBlock(Blocks.STRIPPED_BAOBAB_LOG, 5, 5);
		addFlammableBlock(Blocks.BAOBAB_WOOD, 5, 5);
		addFlammableBlock(Blocks.STRIPPED_BAOBAB_WOOD, 5, 5);
		addFlammableBlock(Blocks.BAOBAB_PLANKS, 5, 25);
		addFlammableBlock(Blocks.BAOBAB_STAIRS, 5, 15);
		addFlammableBlock(Blocks.BAOBAB_SLAB, 5, 15);
		addFlammableBlock(Blocks.BAOBAB_LEAVES, 30, 60);
		addFlammableBlock(Blocks.BAOBAB_LEAVES_FLOWERS, 30, 60);
		addFlammableBlock(Blocks.BAOBAB_LEAVES_FRUIT, 30, 60);

		FuelRegistryEvents.BUILD.register((builder, context) -> {
			int fuelTicks = (int) (context.baseSmeltTime() * 1.2F);
			builder.add(Blocks.MOUND, fuelTicks);
			builder.add(Blocks.INFECTED_MOUND, fuelTicks);
		});

		LOGGER.info("trying to init items");
		ItemInit.initialize();
		net.mastermaxi.mastermaxisvanillaplus.recipe.ModRecipeSerializers.initialize();
		net.mastermaxi.mastermaxisvanillaplus.enchantment.ModEnchantments.initialize();
		net.mastermaxi.mastermaxisvanillaplus.screen.ModScreenHandlerTypes.initialize();
		LOGGER.info("trying to init entities");
		EntityInit.initialize();
		LOGGER.info("trying to register ostrich attributes");
		FabricDefaultAttributeRegistry.register(EntityInit.OSTRICH, OstrichEntity.createAttributes());
		LOGGER.info("trying to init world generation");
		WorldGenerationInit.initialize();
		LOGGER.info("trying to init loot table modifiers");
		net.mastermaxi.mastermaxisvanillaplus.loot.ModLootTableModifiers.initialize();
		LOGGER.info("Hello Fabric world!");
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	private static void addFlammableBlock(Block block, int burnChance, int spreadChance) {
		FlammableBlockRegistry.getDefaultInstance().add(block, burnChance, spreadChance);
	}
}