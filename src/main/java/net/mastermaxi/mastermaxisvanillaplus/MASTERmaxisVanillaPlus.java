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
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.mastermaxi.mastermaxisvanillaplus.component.ModDataComponentTypes;
import net.mastermaxi.mastermaxisvanillaplus.item.BackpackItem;
import net.mastermaxi.mastermaxisvanillaplus.item.QuiverItem;
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

		LOGGER.info("trying to init data component types");
		net.mastermaxi.mastermaxisvanillaplus.component.ModDataComponentTypes.initialize();
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
		net.mastermaxi.mastermaxisvanillaplus.network.ModNetworking.initialize();

		net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> {
			if (entity instanceof net.minecraft.entity.player.PlayerEntity player) {
				io.wispforest.accessories.api.AccessoriesCapability cap = io.wispforest.accessories.api.AccessoriesCapability.get(player);
				if (cap != null) {
					io.wispforest.accessories.api.AccessoriesContainer back = cap.getContainers().get("back");
					if (back != null) {
						net.minecraft.item.ItemStack stack = back.getAccessories().getStack(0);
						if (stack.isOf(net.minecraft.item.Items.ELYTRA) && (stack.getMaxDamage() == 0 || stack.getDamage() < stack.getMaxDamage() - 1)) {
							if (tickElytra) {
								int nextRoll = entity.getGlidingTicks() + 1;
								if (!entity.getWorld().isClient && nextRoll % 10 == 0) {
									if ((nextRoll / 10) % 2 == 0) {
										stack.damage(1, (net.minecraft.server.world.ServerWorld) entity.getWorld(), null, s -> {});
										back.markChanged();
									}
									entity.emitGameEvent(net.minecraft.world.event.GameEvent.ELYTRA_GLIDE);
								}
							}
							return true;
						}
					}
				}
			}
			return false;
		});

		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack held = player.getStackInHand(hand);
			if (!held.isOf(Items.ELYTRA)) {
				return ActionResult.PASS;
			}

			io.wispforest.accessories.api.AccessoriesCapability capability = io.wispforest.accessories.api.AccessoriesCapability.get(player);
			if (capability == null) return ActionResult.PASS;

			io.wispforest.accessories.api.AccessoriesContainer back = capability.getContainers().get("back");
			if (back == null) return ActionResult.PASS;

			ItemStack current = back.getAccessories().getStack(0);

			if (current.isEmpty()) {
				if (!world.isClient) {
					back.getAccessories().setStack(0, held.copy());
					back.markChanged();
					world.playSound(null, player.getX(), player.getY(), player.getZ(),
							SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
					if (player instanceof ServerPlayerEntity serverPlayer) {
						serverPlayer.playerScreenHandler.syncState();
						serverPlayer.currentScreenHandler.syncState();
					}
				}
				player.setStackInHand(hand, ItemStack.EMPTY);
				return ActionResult.SUCCESS;
			} else if (current.getItem() instanceof BackpackItem || current.getItem() instanceof QuiverItem || current.isOf(Items.ELYTRA)) {
				if (!world.isClient) {
					if (current.getItem() instanceof BackpackItem oldBackpack) {
						oldBackpack.dropStoredItems(world, player, current);
					} else if (current.getItem() instanceof QuiverItem oldQuiver) {
						oldQuiver.dropStoredArrows(world, player, current);
					}
					ItemStack old = current.copy();
					back.getAccessories().setStack(0, held.copy());
					player.setStackInHand(hand, old);
					back.markChanged();
					world.playSound(null, player.getX(), player.getY(), player.getZ(),
							SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
					if (player instanceof ServerPlayerEntity serverPlayer) {
						serverPlayer.playerScreenHandler.syncState();
						serverPlayer.currentScreenHandler.syncState();
					}
				} else {
					current.remove(DataComponentTypes.CONTAINER);
					current.remove(ModDataComponentTypes.QUIVER);
					current.remove(ModDataComponentTypes.UPGRADES);
					player.setStackInHand(hand, current.copy());
				}
				return ActionResult.SUCCESS;
			}

			return ActionResult.PASS;
		});

		LOGGER.info("Hello Fabric world!");
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	private static void addFlammableBlock(Block block, int burnChance, int spreadChance) {
		FlammableBlockRegistry.getDefaultInstance().add(block, burnChance, spreadChance);
	}
}