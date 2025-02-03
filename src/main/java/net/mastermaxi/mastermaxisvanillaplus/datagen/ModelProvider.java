package net.mastermaxi.mastermaxisvanillaplus.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.fluid.FluidInit;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.client.data.*;
import net.minecraft.util.Identifier;

import static net.mastermaxi.mastermaxisvanillaplus.block.Blocks.*;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        //Registering simple block models

        blockStateModelGenerator.registerSimpleCubeAll(FEATHER_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(MUD_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(MUDSTONE_FLINT_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(MUDSTONE_IRON_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(MUDSTONE_COAL_ORE);

        //Registering block models with custom textures

        blockStateModelGenerator.registerCubeWithCustomTextures(MUDSTONE_BLOCK, MUDSTONE_BLOCK, (block, otherTextureSource) ->
                TextureMap.sideTopBottom(block)
                        .put(TextureKey.UP, MUDSTONE_BLOCK_TOP_BOTTOM_TEXTURE)
                        .put(TextureKey.DOWN, MUDSTONE_BLOCK_TOP_BOTTOM_TEXTURE)
                        .put(TextureKey.SIDE, MUDSTONE_BLOCK_SIDE_TEXTURE)
                        .put(TextureKey.PARTICLE, MUDSTONE_BLOCK_SIDE_TEXTURE)
        );

        //Registering block using others block model

        BlockStateModelGenerator.BlockTexturePool mudstoneBlockTexturePool = blockStateModelGenerator.registerCubeAllModelTexturePool(MUDSTONE_BLOCK_SIMPLE);
        mudstoneBlockTexturePool.stairs(MUDSTONE_STAIRS);
        mudstoneBlockTexturePool.slab(MUDSTONE_SLAB);
        mudstoneBlockTexturePool.wall(MUDSTONE_WALL);
        mudstoneBlockTexturePool.button(MUDSTONE_BUTTON);
        blockStateModelGenerator.registerDoor(MUDSTONE_DOOR);

        BlockStateModelGenerator.BlockTexturePool polishedMudstoneTexturePool = blockStateModelGenerator.registerCubeAllModelTexturePool(POLISHED_MUDSTONE);
        polishedMudstoneTexturePool.stairs(POLISHED_MUDSTONE_STAIRS);
        polishedMudstoneTexturePool.slab(POLISHED_MUDSTONE_SLAB);
        polishedMudstoneTexturePool.wall(POLISHED_MUDSTONE_WALL);

        BlockStateModelGenerator.BlockTexturePool mudstoneBrickTexturePool = blockStateModelGenerator.registerCubeAllModelTexturePool(MUDSTONE_BRICK);
        mudstoneBrickTexturePool.stairs(MUDSTONE_BRICK_STAIRS);
        mudstoneBrickTexturePool.slab(MUDSTONE_BRICK_SLAB);
        mudstoneBrickTexturePool.wall(MUDSTONE_BRICK_WALL);
        mudstoneBrickTexturePool.pressurePlate(MUDSTONE_BRICK_PRESSURE_PLATE);

        //Adding item models for blocks manually, because it is not done automatically somehow

        blockStateModelGenerator.registerParentedItemModel(FEATHER_BLOCK, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/feather_block"));
        blockStateModelGenerator.registerParentedItemModel(MUD_BLOCK, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mud_block"));
        blockStateModelGenerator.registerParentedItemModel(MUDSTONE_FLINT_ORE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mudstone_flint_ore"));
        blockStateModelGenerator.registerParentedItemModel(MUDSTONE_IRON_ORE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mudstone_iron_ore"));
        blockStateModelGenerator.registerParentedItemModel(MUDSTONE_COAL_ORE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mudstone_coal_ore"));
        blockStateModelGenerator.registerParentedItemModel(MUDSTONE_BLOCK, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mudstone_block"));
        blockStateModelGenerator.registerParentedItemModel(POLISHED_MUDSTONE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/polished_mudstone"));
        blockStateModelGenerator.registerParentedItemModel(MUDSTONE_BRICK, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mudstone_brick"));
        blockStateModelGenerator.registerParentedItemModel(MUDSTONE_BRICK_PRESSURE_PLATE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mudstone_brick_pressure_plate"));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemInit.MUD_BALL, Models.GENERATED);
        itemModelGenerator.register(ItemInit.OSTRICH_FEATHER, Models.GENERATED);
        itemModelGenerator.register(FluidInit.MUD_WATER_BUCKET, Models.GENERATED);
    }
}
