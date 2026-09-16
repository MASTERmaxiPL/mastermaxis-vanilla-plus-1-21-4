package net.mastermaxi.mastermaxisvanillaplus.recipe;

import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class TippedHeavyArrowRecipe extends SpecialCraftingRecipe {
    public TippedHeavyArrowRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        if (input.getWidth() != 3 || input.getHeight() != 3 || input.getStackCount() != 9) {
            return false;
        }

        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                ItemStack stack = input.getStackInSlot(x, y);
                if (stack.isEmpty()) {
                    return false;
                }
                if (x == 1 && y == 1) {
                    if (!stack.isOf(Items.LINGERING_POTION)) {
                        return false;
                    }
                } else if (!stack.isOf(ItemInit.HEAVY_ARROW)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack center = input.getStackInSlot(1, 1);
        if (!center.isOf(Items.LINGERING_POTION)) {
            return ItemStack.EMPTY;
        }

        ItemStack result = new ItemStack(ItemInit.HEAVY_ARROW, 8);
        result.set(DataComponentTypes.POTION_CONTENTS, center.get(DataComponentTypes.POTION_CONTENTS));
        return result;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipeSerializers.TIPPED_HEAVY_ARROW;
    }
}
