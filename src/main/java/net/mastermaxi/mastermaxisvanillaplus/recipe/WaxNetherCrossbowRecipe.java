package net.mastermaxi.mastermaxisvanillaplus.recipe;

import net.mastermaxi.mastermaxisvanillaplus.item.NetherCrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

/**
 * Special crafting recipe allowing players to wax a Nether Crossbow (normal or scoped)
 * using Honeycomb, preserving all enchantments, durability, and custom names.
 */
public class WaxNetherCrossbowRecipe extends SpecialCraftingRecipe {
    public WaxNetherCrossbowRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        ItemStack crossbow = ItemStack.EMPTY;
        boolean hasWax = false;
        int count = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty()) {
                count++;
                if (stack.getItem() instanceof NetherCrossbowItem && !NetherCrossbowItem.isWaxed(stack)) {
                    if (!crossbow.isEmpty()) return false;
                    crossbow = stack;
                } else if (stack.isOf(Items.HONEYCOMB)) {
                    if (hasWax) return false;
                    hasWax = true;
                } else {
                    return false;
                }
            }
        }
        return count == 2 && !crossbow.isEmpty() && hasWax;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack crossbow = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof NetherCrossbowItem) {
                crossbow = stack;
                break;
            }
        }
        if (crossbow.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = crossbow.copy();
        NetherCrossbowItem.setWaxed(result, true);
        return result;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipeSerializers.WAX_NETHER_CROSSBOW;
    }
}
