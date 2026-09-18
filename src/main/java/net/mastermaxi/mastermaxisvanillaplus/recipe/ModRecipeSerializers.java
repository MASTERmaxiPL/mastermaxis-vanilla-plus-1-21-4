package net.mastermaxi.mastermaxisvanillaplus.recipe;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModRecipeSerializers {
    public static final RecipeSerializer<TippedBoltRecipe> TIPPED_BOLT = Registry.register(
            Registries.RECIPE_SERIALIZER,
            MASTERmaxisVanillaPlus.id("crafting_special_tipped_bolt"),
            new SpecialCraftingRecipe.SpecialRecipeSerializer<>(TippedBoltRecipe::new)
    );

    public static final RecipeSerializer<TippedHeavyArrowRecipe> TIPPED_HEAVY_ARROW = Registry.register(
            Registries.RECIPE_SERIALIZER,
            MASTERmaxisVanillaPlus.id("crafting_special_tipped_heavy_arrow"),
            new SpecialCraftingRecipe.SpecialRecipeSerializer<>(TippedHeavyArrowRecipe::new)
    );

    public static final RecipeSerializer<WaxNetherCrossbowRecipe> WAX_NETHER_CROSSBOW = Registry.register(
            Registries.RECIPE_SERIALIZER,
            MASTERmaxisVanillaPlus.id("crafting_special_wax_nether_crossbow"),
            new SpecialCraftingRecipe.SpecialRecipeSerializer<>(WaxNetherCrossbowRecipe::new)
    );

    public static final RecipeSerializer<ScrapeNetherCrossbowRecipe> SCRAPE_NETHER_CROSSBOW = Registry.register(
            Registries.RECIPE_SERIALIZER,
            MASTERmaxisVanillaPlus.id("crafting_special_scrape_nether_crossbow"),
            new SpecialCraftingRecipe.SpecialRecipeSerializer<>(ScrapeNetherCrossbowRecipe::new)
    );

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Registering recipe serializers for " + MASTERmaxisVanillaPlus.MOD_ID);
    }
}
