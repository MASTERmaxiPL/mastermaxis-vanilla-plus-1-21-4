package net.mastermaxi.mastermaxisvanillaplus.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
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

                id = MASTERmaxisVanillaPlus.id("feather_block");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), TRANSPORTATION, Blocks.FEATHER_BLOCK)
                        .pattern("FFF")
                        .pattern("FFF")
                        .pattern("FFF")
                        .input('F', Ingredient.ofItems(Items.FEATHER, ItemInit.OSTRICH_FEATHER))
                        .criterion(hasItem(Items.FEATHER), conditionsFromItem(Items.FEATHER))
                        .criterion(hasItem(ItemInit.OSTRICH_FEATHER), conditionsFromItem(ItemInit.OSTRICH_FEATHER))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                // Red Soil Recipes
                createStairsRecipe(Blocks.RED_SOIL_STAIRS, Ingredient.ofItems(Blocks.RED_SOIL))
                        .criterion(hasItem(Blocks.RED_SOIL), conditionsFromItem(Blocks.RED_SOIL))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, MASTERmaxisVanillaPlus.id("red_soil_stairs")));
                createSlabRecipe(BUILDING_BLOCKS, Blocks.RED_SOIL_SLAB, Ingredient.ofItems(Blocks.RED_SOIL))
                        .criterion(hasItem(Blocks.RED_SOIL), conditionsFromItem(Blocks.RED_SOIL))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, MASTERmaxisVanillaPlus.id("red_soil_slab")));

                // Laterite Recipes
                offerStonecuttingRecipe(BUILDING_BLOCKS, Blocks.LATERITE_STAIRS, Blocks.LATERITE);
                offerStonecuttingRecipe(BUILDING_BLOCKS, Blocks.LATERITE_SLAB, Blocks.LATERITE);
                createStairsRecipe(Blocks.LATERITE_STAIRS, Ingredient.ofItems(Blocks.LATERITE))
                        .criterion(hasItem(Blocks.LATERITE), conditionsFromItem(Blocks.LATERITE))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, MASTERmaxisVanillaPlus.id("laterite_stairs")));
                createSlabRecipe(BUILDING_BLOCKS, Blocks.LATERITE_SLAB, Ingredient.ofItems(Blocks.LATERITE))
                        .criterion(hasItem(Blocks.LATERITE), conditionsFromItem(Blocks.LATERITE))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, MASTERmaxisVanillaPlus.id("laterite_slab")));

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

                id = Identifier.ofVanilla("cake");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), FOOD, Items.CAKE)
                        .pattern("MMM")
                        .pattern("SES")
                        .pattern("WWW")
                        .input('M', Items.MILK_BUCKET)
                        .input('S', Items.SUGAR)
                        .input('E', Ingredient.ofItems(Items.EGG, ItemInit.OSTRICH_EGG))
                        .input('W', Items.WHEAT)
                        .criterion(hasItem(Items.EGG), conditionsFromItem(Items.EGG))
                        .criterion(hasItem(ItemInit.OSTRICH_EGG), conditionsFromItem(ItemInit.OSTRICH_EGG))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                id = Identifier.ofVanilla("pumpkin_pie");
                ShapelessRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), FOOD, Items.PUMPKIN_PIE)
                        .input(Items.PUMPKIN)
                        .input(Items.SUGAR)
                        .input(Ingredient.ofItems(Items.EGG, ItemInit.OSTRICH_EGG))
                        .criterion(hasItem(Items.EGG), conditionsFromItem(Items.EGG))
                        .criterion(hasItem(ItemInit.OSTRICH_EGG), conditionsFromItem(ItemInit.OSTRICH_EGG))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                id = Identifier.ofVanilla("writable_book");
                ShapelessRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), MISC, Items.WRITABLE_BOOK)
                        .input(Items.BOOK)
                        .input(Items.INK_SAC)
                        .input(Ingredient.ofItems(Items.FEATHER, ItemInit.OSTRICH_FEATHER))
                        .criterion(hasItem(Items.FEATHER), conditionsFromItem(Items.FEATHER))
                        .criterion(hasItem(ItemInit.OSTRICH_FEATHER), conditionsFromItem(ItemInit.OSTRICH_FEATHER))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                id = Identifier.ofVanilla("brush");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), TOOLS, Items.BRUSH)
                        .pattern("F")
                        .pattern("C")
                        .pattern("S")
                        .input('F', Ingredient.ofItems(Items.FEATHER, ItemInit.OSTRICH_FEATHER))
                        .input('C', Items.COPPER_INGOT)
                        .input('S', Items.STICK)
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .criterion(hasItem(Items.FEATHER), conditionsFromItem(Items.FEATHER))
                        .criterion(hasItem(ItemInit.OSTRICH_FEATHER), conditionsFromItem(ItemInit.OSTRICH_FEATHER))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                id = MASTERmaxisVanillaPlus.id("gui_juice");
                ShapelessRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), FOOD, ItemInit.GUI_JUICE)
                        .input(Items.POTION)
                        .input(ItemInit.BAOBAB_FRUIT)
                        .input(Items.SUGAR)
                        .criterion(hasItem(ItemInit.BAOBAB_FRUIT), conditionsFromItem(ItemInit.BAOBAB_FRUIT))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                id = MASTERmaxisVanillaPlus.id("monkey_bread");
                ShapelessRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), FOOD, ItemInit.MONKEY_BREAD)
                        .input(Items.WHEAT)
                        .input(Items.WHEAT)
                        .input(ItemInit.BAOBAB_FRUIT)
                        .criterion(hasItem(ItemInit.BAOBAB_FRUIT), conditionsFromItem(ItemInit.BAOBAB_FRUIT))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                id = MASTERmaxisVanillaPlus.id("baobab_stew");
                ShapelessRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), FOOD, ItemInit.BAOBAB_STEW)
                    .input(Items.BOWL)
                    .input(ItemInit.BAOBAB_FRUIT)
                    .input(Ingredient.ofItems(Items.COOKED_BEEF, Items.COOKED_PORKCHOP))
                    .input(Items.BROWN_MUSHROOM)
                    .criterion(hasItem(ItemInit.BAOBAB_FRUIT), conditionsFromItem(ItemInit.BAOBAB_FRUIT))
                    .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                id = MASTERmaxisVanillaPlus.id("truffle_porkchop");
                ShapelessRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), FOOD, ItemInit.TRUFFLE_PORKCHOP)
                    .input(Items.COOKED_PORKCHOP)
                    .input(ItemInit.TRUFFLE)
                    .criterion(hasItem(ItemInit.TRUFFLE), conditionsFromItem(ItemInit.TRUFFLE))
                    .criterion(hasItem(Items.COOKED_PORKCHOP), conditionsFromItem(Items.COOKED_PORKCHOP))
                    .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                // ===== SHOOTING MECHANICS =====

                // Heavy Arrow: iron nugget + stick + ostrich feather => 4x heavy arrow
                id = MASTERmaxisVanillaPlus.id("heavy_arrow");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), COMBAT, ItemInit.HEAVY_ARROW, 4)
                        .pattern("I")
                        .pattern("S")
                        .pattern("F")
                        .input('I', Items.IRON_NUGGET)
                        .input('S', Items.STICK)
                        .input('F', ItemInit.OSTRICH_FEATHER)
                        .criterion(hasItem(Items.IRON_NUGGET), conditionsFromItem(Items.IRON_NUGGET))
                        .criterion(hasItem(ItemInit.OSTRICH_FEATHER), conditionsFromItem(ItemInit.OSTRICH_FEATHER))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                // Bolt: iron nugget + stick => 4x bolt
                id = MASTERmaxisVanillaPlus.id("bolt");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), COMBAT, ItemInit.BOLT, 4)
                        .pattern("I")
                        .pattern("S")
                        .input('I', Items.IRON_NUGGET)
                        .input('S', Items.STICK)
                        .criterion(hasItem(Items.IRON_NUGGET), conditionsFromItem(Items.IRON_NUGGET))
                        .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                // Iron Crossbow: 3 iron bars + slimeball + 2 strings
                id = MASTERmaxisVanillaPlus.id("iron_crossbow");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), COMBAT, ItemInit.IRON_CROSSBOW)
                        .pattern("ISI")
                        .pattern("SCS")
                        .pattern("ISI")
                        .input('I', Items.IRON_INGOT)
                        .input('S', Items.STRING)
                        .input('C', Items.SLIME_BALL)
                        .criterion(hasItem(Items.CROSSBOW), conditionsFromItem(Items.CROSSBOW))
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                        .criterion(hasItem(Items.SLIME_BALL), conditionsFromItem(Items.SLIME_BALL))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                // Nether Crossbow: iron_crossbow + 2 gold bars + 4 copper bars + 2 slime balls
                id = MASTERmaxisVanillaPlus.id("nether_crossbow");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), COMBAT, ItemInit.NETHER_CROSSBOW)
                        .pattern("GCG")
                        .pattern("CSC")
                        .pattern("GCG")
                        .input('G', Items.GOLD_INGOT)
                        .input('C', Items.COPPER_INGOT)
                        .input('S', ItemInit.IRON_CROSSBOW)
                        .criterion(hasItem(ItemInit.IRON_CROSSBOW), conditionsFromItem(ItemInit.IRON_CROSSBOW))
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                // Crossbow + viewfinder (vanilla crossbow with viewfinder)
                id = MASTERmaxisVanillaPlus.id("crossbow_viewfinder");
                ShapelessRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), COMBAT, ItemInit.CROSSBOW_VIEWFINDER)
                        .input(Items.CROSSBOW)
                        .input(ItemInit.VIEWFINDER)
                        .criterion(hasItem(Items.CROSSBOW), conditionsFromItem(Items.CROSSBOW))
                        .criterion(hasItem(ItemInit.VIEWFINDER), conditionsFromItem(ItemInit.VIEWFINDER))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                // Iron Crossbow + viewfinder
                id = MASTERmaxisVanillaPlus.id("iron_crossbow_viewfinder");
                ShapelessRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), COMBAT, ItemInit.IRON_CROSSBOW_VIEWFINDER)
                        .input(ItemInit.IRON_CROSSBOW)
                        .input(ItemInit.VIEWFINDER)
                        .criterion(hasItem(ItemInit.IRON_CROSSBOW), conditionsFromItem(ItemInit.IRON_CROSSBOW))
                        .criterion(hasItem(ItemInit.VIEWFINDER), conditionsFromItem(ItemInit.VIEWFINDER))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                // Nether Crossbow + viewfinder
                id = MASTERmaxisVanillaPlus.id("nether_crossbow_viewfinder");
                ShapelessRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), COMBAT, ItemInit.NETHER_CROSSBOW_VIEWFINDER)
                        .input(ItemInit.NETHER_CROSSBOW)
                        .input(ItemInit.VIEWFINDER)
                        .criterion(hasItem(ItemInit.NETHER_CROSSBOW), conditionsFromItem(ItemInit.NETHER_CROSSBOW))
                        .criterion(hasItem(ItemInit.VIEWFINDER), conditionsFromItem(ItemInit.VIEWFINDER))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                // Viewfinder item: amethyst shard above gold ingot in middle column
                id = MASTERmaxisVanillaPlus.id("viewfinder");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), TOOLS, ItemInit.VIEWFINDER)
                        .pattern(" A ")
                        .pattern(" G ")
                        .input('A', Items.AMETHYST_SHARD)
                        .input('G', Items.GOLD_INGOT)
                        .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                // Pyrotechnic Tube: Iron block | gunpowder slime ball | Iron block
                id = MASTERmaxisVanillaPlus.id("pyrotechnic_tube");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), COMBAT, ItemInit.PYROTECHNIC_TUBE)
                        .pattern("IGI")
                        .pattern(" S ")
                        .input('I', Items.IRON_BLOCK)
                        .input('G', Items.GUNPOWDER)
                        .input('S', Items.SLIME_BALL)
                        .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                        .criterion(hasItem(Items.GUNPOWDER), conditionsFromItem(Items.GUNPOWDER))
                        .criterion(hasItem(Items.SLIME_BALL), conditionsFromItem(Items.SLIME_BALL))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));

                id = MASTERmaxisVanillaPlus.id("pyrotechnic_tube_alt");
                ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), COMBAT, ItemInit.PYROTECHNIC_TUBE)
                        .pattern("ISI")
                        .pattern(" G ")
                        .input('I', Items.IRON_BLOCK)
                        .input('G', Items.GUNPOWDER)
                        .input('S', Items.SLIME_BALL)
                        .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                        .criterion(hasItem(Items.GUNPOWDER), conditionsFromItem(Items.GUNPOWDER))
                        .criterion(hasItem(Items.SLIME_BALL), conditionsFromItem(Items.SLIME_BALL))
                        .offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, id));
            }
        };
    }

    @Override
    protected Identifier getRecipeIdentifier(Identifier identifier) {
        return identifier;
    }

    @Override
    public String getName() {
        return "MASTERmaxisVanillaPlus Recipes";
    }
}
