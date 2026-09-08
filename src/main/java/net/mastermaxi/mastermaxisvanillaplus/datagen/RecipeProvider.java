package net.mastermaxi.mastermaxisvanillaplus.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.recipe.book.RecipeCategory.*;

public class RecipeProvider extends FabricRecipeProvider {

    public RecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new RecipeGenerator(wrapperLookup, recipeExporter) {
            @Override
            public void generate() {
                Identifier id;

                offer2x2CompactingRecipe(BUILDING_BLOCKS, Blocks.MUD_BLOCK, ItemInit.MUD_BALL);

                id = MASTERmaxisVanillaPlus.id("ostrich_feather_to_feather_block");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), TRANSPORTATION, Blocks.FEATHER_BLOCK)
                        .pattern("FFF")
                        .pattern("FFF")
                        .pattern("FFF")
                        .input('F', ItemInit.OSTRICH_FEATHER)
                        .criterion(hasItem(ItemInit.OSTRICH_FEATHER), conditionsFromItem(ItemInit.OSTRICH_FEATHER))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                id = MASTERmaxisVanillaPlus.id("chicken_feather_to_feather_block");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), TRANSPORTATION, Blocks.FEATHER_BLOCK)
                        .pattern("FFF")
                        .pattern("FFF")
                        .pattern("FFF")
                        .input('F', Items.FEATHER)
                        .criterion(hasItem(Items.FEATHER), conditionsFromItem(Items.FEATHER))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                // Mudstone Block Recipes
                offerStonecuttingRecipe(BUILDING_BLOCKS, Blocks.MUDSTONE_STAIRS, Blocks.MUDSTONE_BLOCK);
                offerStonecuttingRecipe(BUILDING_BLOCKS, Blocks.MUDSTONE_SLAB, Blocks.MUDSTONE_BLOCK);
                offerStonecuttingRecipe(BUILDING_BLOCKS, Blocks.MUDSTONE_WALL, Blocks.MUDSTONE_BLOCK);

                // Mudstone Brick Recipes
                offerStonecuttingRecipe(BUILDING_BLOCKS, Blocks.MUDSTONE_BRICK_STAIRS, Blocks.MUDSTONE_BRICK);
                offerStonecuttingRecipe(BUILDING_BLOCKS, Blocks.MUDSTONE_BRICK_SLAB, Blocks.MUDSTONE_BRICK);
                offerStonecuttingRecipe(BUILDING_BLOCKS, Blocks.MUDSTONE_BRICK_WALL, Blocks.MUDSTONE_BRICK);

                // Polished Mudstone Recipes
                offerStonecuttingRecipe(BUILDING_BLOCKS, Blocks.POLISHED_MUDSTONE_STAIRS, Blocks.POLISHED_MUDSTONE);
                offerStonecuttingRecipe(BUILDING_BLOCKS, Blocks.POLISHED_MUDSTONE_SLAB, Blocks.POLISHED_MUDSTONE);
                offerStonecuttingRecipe(BUILDING_BLOCKS, Blocks.POLISHED_MUDSTONE_WALL, Blocks.POLISHED_MUDSTONE);

                id = MASTERmaxisVanillaPlus.id("mudstone_block_to_mudstone_brick");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), BUILDING_BLOCKS, Blocks.MUDSTONE_BRICK)
                        .pattern("MM")
                        .pattern("MM")
                        .input('M', Blocks.MUDSTONE_BLOCK)
                        .criterion(hasItem(Blocks.MUDSTONE_BLOCK), conditionsFromItem(Blocks.MUDSTONE_BLOCK))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                id = MASTERmaxisVanillaPlus.id("polished_mudstone");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), BUILDING_BLOCKS, Blocks.POLISHED_MUDSTONE, 4)
                        .pattern("MM")
                        .pattern("MM")
                        .input('M', Blocks.MUDSTONE_BLOCK)
                        .criterion(hasItem(Blocks.MUDSTONE_BLOCK), conditionsFromItem(Blocks.MUDSTONE_BLOCK))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                id = MASTERmaxisVanillaPlus.id("mudstone_button");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), BUILDING_BLOCKS, Blocks.MUDSTONE_BUTTON)
                        .pattern("M")
                        .input('M', Blocks.MUDSTONE_BLOCK)
                        .criterion(hasItem(Blocks.MUDSTONE_BLOCK), conditionsFromItem(Blocks.MUDSTONE_BLOCK))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                id = MASTERmaxisVanillaPlus.id("mudstone_door");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), BUILDING_BLOCKS, Blocks.MUDSTONE_DOOR, 3)
                        .pattern("MM")
                        .pattern("MM")
                        .pattern("MM")
                        .input('M', Blocks.MUDSTONE_BLOCK)
                        .criterion(hasItem(Blocks.MUDSTONE_BLOCK), conditionsFromItem(Blocks.MUDSTONE_BLOCK))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                id = MASTERmaxisVanillaPlus.id("mudstone_brick_pressure_plate");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), BUILDING_BLOCKS, Blocks.MUDSTONE_BRICK_PRESSURE_PLATE)
                        .pattern("MM")
                        .input('M', Blocks.MUDSTONE_BRICK)
                        .criterion(hasItem(Blocks.MUDSTONE_BRICK), conditionsFromItem(Blocks.MUDSTONE_BRICK))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));
            }
        };
    }

    @Override
    public String getName() {
        return "MASTERmaxisVanillaPlus Recipes";
    }
}
