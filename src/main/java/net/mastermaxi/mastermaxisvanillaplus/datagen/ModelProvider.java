package net.mastermaxi.mastermaxisvanillaplus.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.fluid.FluidInit;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.tint.GrassTintSource;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.FoliageColors;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static net.mastermaxi.mastermaxisvanillaplus.block.Blocks.*;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        //Registering simple block models

        blockStateModelGenerator.registerSimpleCubeAll(FEATHER_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(MUD_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(MUDSTONE_FLINT_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(MUDSTONE_IRON_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(MUDSTONE_COAL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(MUDSTONE_COPPER_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(SILTED_GRAVEL);

        //Registering block models with custom textures

        blockStateModelGenerator.registerCubeWithCustomTextures(MUDSTONE_BLOCK, MUDSTONE_BLOCK, (block, otherTextureSource) ->
                TextureMap.sideTopBottom(block)
                        .put(TextureKey.UP, MUDSTONE_BLOCK_TOP_BOTTOM_TEXTURE)
                        .put(TextureKey.DOWN, MUDSTONE_BLOCK_TOP_BOTTOM_TEXTURE)
                        .put(TextureKey.SIDE, MUDSTONE_BLOCK_SIDE_TEXTURE)
                        .put(TextureKey.PARTICLE, MUDSTONE_BLOCK_SIDE_TEXTURE)
        );

        //Registering block using others block net.mastermaxi.mastermaxisvanillaplus.entity.client.model

        Consumer<BlockStateSupplier> blockStateCollector;
        BiConsumer<Identifier, ModelSupplier> modelCollector;
        java.lang.reflect.Field baseModelIdField;
        try {
            java.lang.reflect.Field bscField = BlockStateModelGenerator.class.getDeclaredField("blockStateCollector");
            bscField.setAccessible(true);
            blockStateCollector = (Consumer<BlockStateSupplier>) bscField.get(blockStateModelGenerator);

            java.lang.reflect.Field mcField = BlockStateModelGenerator.class.getDeclaredField("modelCollector");
            mcField.setAccessible(true);
            modelCollector = (BiConsumer<Identifier, ModelSupplier>) mcField.get(blockStateModelGenerator);

            baseModelIdField = BlockStateModelGenerator.BlockTexturePool.class.getDeclaredField("baseModelId");
            baseModelIdField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access model/blockstate collectors or pool fields", e);
        }

        TextureMap redSoilTextureMap = TextureMap.all(RED_SOIL);
        Identifier redSoilModelId = registerCubeAllRotated(RED_SOIL, redSoilTextureMap, blockStateCollector, modelCollector);

        BlockStateModelGenerator.BlockTexturePool redSoilTexturePool = blockStateModelGenerator.new BlockTexturePool(redSoilTextureMap);
        try {
            baseModelIdField.set(redSoilTexturePool, redSoilModelId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        redSoilTexturePool.stairs(RED_SOIL_STAIRS);
        redSoilTexturePool.slab(RED_SOIL_SLAB);

        TextureMap lateriteTextureMap = new TextureMap()
                .put(TextureKey.TOP, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/laterite_top"))
                .put(TextureKey.BOTTOM, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/laterite_side_bot"))
                .put(TextureKey.SIDE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/laterite_side_bot"))
                .put(TextureKey.PARTICLE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/laterite_side_bot"));

        Identifier lateriteModelId = registerCubeBottomTopRotated(LATERITE, lateriteTextureMap, blockStateCollector, modelCollector);

        BlockStateModelGenerator.BlockTexturePool lateriteTexturePool = blockStateModelGenerator.new BlockTexturePool(lateriteTextureMap);
        try {
            baseModelIdField.set(lateriteTexturePool, lateriteModelId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        lateriteTexturePool.stairs(LATERITE_STAIRS);
        lateriteTexturePool.slab(LATERITE_SLAB);

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

        blockStateModelGenerator.registerLog(BAOBAB_LOG).log(BAOBAB_LOG).wood(BAOBAB_WOOD);
        blockStateModelGenerator.registerLog(STRIPPED_BAOBAB_LOG).log(STRIPPED_BAOBAB_LOG).wood(STRIPPED_BAOBAB_WOOD);
        blockStateModelGenerator.registerTintedBlockAndItem(BAOBAB_LEAVES, TexturedModel.LEAVES, FoliageColors.DEFAULT);
        blockStateModelGenerator.registerTintableCrossBlockState(BAOBAB_SAPLING, BlockStateModelGenerator.CrossType.NOT_TINTED);
        BlockStateModelGenerator.BlockTexturePool BaobabPlanksTexturePool = blockStateModelGenerator.registerCubeAllModelTexturePool(BAOBAB_PLANKS);
        BaobabPlanksTexturePool.slab(BAOBAB_SLAB);
        BaobabPlanksTexturePool.stairs(BAOBAB_STAIRS);
        BaobabPlanksTexturePool.fence(BAOBAB_FENCE);
        BaobabPlanksTexturePool.fenceGate(BAOBAB_GATE);
        BaobabPlanksTexturePool.button(BAOBAB_BUTTON);
        BaobabPlanksTexturePool.pressurePlate(BAOBAB_PRESSURE_PLATE);
        blockStateModelGenerator.registerDoor(BAOBAB_DOOR);
        blockStateModelGenerator.registerTrapdoor(BAOBAB_TRAPDOOR);

        //Adding item models for blocks manually, because it is not done automatically somehow

        blockStateModelGenerator.registerParentedItemModel(FEATHER_BLOCK, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/feather_block"));
        blockStateModelGenerator.registerParentedItemModel(RED_SOIL, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/red_soil"));
        blockStateModelGenerator.registerParentedItemModel(LATERITE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/laterite"));
        blockStateModelGenerator.registerParentedItemModel(MUD_BLOCK, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mud_block"));
        blockStateModelGenerator.registerParentedItemModel(MUDSTONE_FLINT_ORE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mudstone_flint_ore"));
        blockStateModelGenerator.registerParentedItemModel(MUDSTONE_IRON_ORE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mudstone_iron_ore"));
        blockStateModelGenerator.registerParentedItemModel(MUDSTONE_COAL_ORE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mudstone_coal_ore"));
        blockStateModelGenerator.registerParentedItemModel(MUDSTONE_BLOCK, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mudstone_block"));
        blockStateModelGenerator.registerParentedItemModel(POLISHED_MUDSTONE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/polished_mudstone"));
        blockStateModelGenerator.registerParentedItemModel(MUDSTONE_BRICK, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mudstone_brick"));
        blockStateModelGenerator.registerParentedItemModel(MUDSTONE_BRICK_PRESSURE_PLATE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mudstone_brick_pressure_plate"));

        blockStateModelGenerator.registerParentedItemModel(MOUND, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/mound"));
        blockStateModelGenerator.registerParentedItemModel(INFECTED_MOUND, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/infected_mound_0"));
        blockStateModelGenerator.registerParentedItemModel(INFECTED_WOOD, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/infected_wood_overlay_0"));
        blockStateModelGenerator.registerParentedItemModel(TERMITE_TRAIL, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/termite_trail_0"));

        // Register tinted item models via reflection (itemModelOutput is package-private).
        // This prevents Fabric's mixin from auto-generating plain (non-tinted) versions on each runDatagen.
        try {
            java.lang.reflect.Field field = BlockStateModelGenerator.class.getDeclaredField("itemModelOutput");
            field.setAccessible(true);
            ItemModelOutput itemModelOutput = (ItemModelOutput) field.get(blockStateModelGenerator);

            // GRASSY_RED_SOIL: grass tint
            itemModelOutput.accept(
                GRASSY_RED_SOIL.asItem(),
                ItemModels.tinted(
                    Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/grassy_red_soil"),
                    new GrassTintSource()
                )
            );

            // REDTHORN: foliage tint (same as biome layer in block model)
            itemModelOutput.accept(
                REDTHORN.asItem(),
                ItemModels.tinted(
                    Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "item/redthorn"),
                    // tintindex 0 = layer0 (spikes) → no color change
                    new net.minecraft.client.render.item.tint.ConstantTintSource(-1),
                    // tintindex 1 = layer1 (biome) → savanna foliage color (temperature=1.0, downfall=0.0)
                    new GrassTintSource(1.0f, 0.0f)
                )
            );

            // BAOBAB_LEAVES_FLOWERS: foliage tint
            itemModelOutput.accept(
                BAOBAB_LEAVES_FLOWERS.asItem(),
                ItemModels.tinted(
                    Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/baobab_leaves_flowers"),
                    new net.minecraft.client.render.item.tint.ConstantTintSource(FoliageColors.DEFAULT)
                )
            );

            // BAOBAB_LEAVES_FRUIT: foliage tint
            itemModelOutput.accept(
                BAOBAB_LEAVES_FRUIT.asItem(),
                ItemModels.tinted(
                    Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/baobab_leaves_fruit"),
                    new net.minecraft.client.render.item.tint.ConstantTintSource(FoliageColors.DEFAULT)
                )
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to register tinted item models", e);
        }

        blockStateModelGenerator.registerParentedItemModel(BAOBAB_LOG, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/baobab_log"));
        blockStateModelGenerator.registerParentedItemModel(STRIPPED_BAOBAB_LOG, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/stripped_baobab_log"));
        blockStateModelGenerator.registerParentedItemModel(BAOBAB_WOOD, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/baobab_wood"));
        blockStateModelGenerator.registerParentedItemModel(STRIPPED_BAOBAB_WOOD, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/stripped_baobab_wood"));
        blockStateModelGenerator.registerParentedItemModel(BAOBAB_PLANKS, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/baobab_planks"));
        blockStateModelGenerator.registerParentedItemModel(BAOBAB_PRESSURE_PLATE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/baobab_pressure_plate"));
        blockStateModelGenerator.registerParentedItemModel(BAOBAB_GATE, Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "block/baobab_gate"));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemInit.MUD_BALL, Models.GENERATED);
        itemModelGenerator.register(ItemInit.OSTRICH_FEATHER, Models.GENERATED);
        itemModelGenerator.register(FluidInit.MUD_WATER_BUCKET, Models.GENERATED);
        itemModelGenerator.register(BAOBAB_SAPLING.asItem(), Models.GENERATED);
        itemModelGenerator.register(ItemInit.OSTRICH_SPAWN_EGG, Models.GENERATED);
        itemModelGenerator.register(ItemInit.OSTRICH_EGG, Models.GENERATED);
        itemModelGenerator.register(ItemInit.BAOBAB_FRUIT, Models.GENERATED);
        itemModelGenerator.register(ItemInit.GUI_JUICE, Models.GENERATED);
        itemModelGenerator.register(ItemInit.MONKEY_BREAD, Models.GENERATED);
        itemModelGenerator.register(ItemInit.BAOBAB_STEW, Models.GENERATED);
    }

    private static Identifier registerCubeBottomTopRotated(
            Block block,
            TextureMap textureMap,
            Consumer<BlockStateSupplier> blockStateCollector,
            BiConsumer<Identifier, ModelSupplier> modelCollector
    ) {
        Identifier modelId = Models.CUBE_BOTTOM_TOP.upload(block, textureMap, modelCollector);
        BlockStateVariant v0 = BlockStateVariant.create().put(VariantSettings.MODEL, modelId);
        BlockStateVariant v90 = BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.Y, VariantSettings.Rotation.R90);
        BlockStateVariant v180 = BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.Y, VariantSettings.Rotation.R180);
        BlockStateVariant v270 = BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.Y, VariantSettings.Rotation.R270);

        blockStateCollector.accept(VariantsBlockStateSupplier.create(block, v0, v90, v180, v270));
        return modelId;
    }

    private static Identifier registerCubeAllRotated(
            Block block,
            TextureMap textureMap,
            Consumer<BlockStateSupplier> blockStateCollector,
            BiConsumer<Identifier, ModelSupplier> modelCollector
    ) {
        Identifier modelId = Models.CUBE_ALL.upload(block, textureMap, modelCollector);
        BlockStateVariant v0 = BlockStateVariant.create().put(VariantSettings.MODEL, modelId);
        BlockStateVariant v90 = BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.Y, VariantSettings.Rotation.R90);
        BlockStateVariant v180 = BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.Y, VariantSettings.Rotation.R180);
        BlockStateVariant v270 = BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.Y, VariantSettings.Rotation.R270);

        blockStateCollector.accept(VariantsBlockStateSupplier.create(block, v0, v90, v180, v270));
        return modelId;
    }
}
