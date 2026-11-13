package net.mastermaxi.mastermaxisvanillaplus.recipe;

import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

/**
 * Special crafting recipe allowing players to revert an Iron Backpack with Quiver
 * back into an Iron Backpack and a Quiver.
 * The crafted result is the Iron Backpack, while the Quiver is returned as the recipe remainder.
 */
public class RevertIronBackpackWithQuiverRecipe extends SpecialCraftingRecipe {
    public RevertIronBackpackWithQuiverRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        ItemStack backpack = ItemStack.EMPTY;
        int count = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty()) {
                count++;
                if (stack.isOf(ItemInit.IRON_BACKPACK_WITH_QUIVER)) {
                    backpack = stack;
                }
            }
        }
        return count == 1 && !backpack.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isOf(ItemInit.IRON_BACKPACK_WITH_QUIVER)) {
                ItemStack result = new ItemStack(ItemInit.IRON_BACKPACK);
                if (stack.contains(DataComponentTypes.CUSTOM_NAME)) {
                    result.set(DataComponentTypes.CUSTOM_NAME, stack.get(DataComponentTypes.CUSTOM_NAME));
                }
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput input) {
        DefaultedList<ItemStack> remainder = DefaultedList.ofSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isOf(ItemInit.IRON_BACKPACK_WITH_QUIVER)) {
                remainder.set(i, new ItemStack(ItemInit.QUIVER));
                break;
            }
        }
        return remainder;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipeSerializers.REVERT_IRON_BACKPACK_WITH_QUIVER;
    }
}
