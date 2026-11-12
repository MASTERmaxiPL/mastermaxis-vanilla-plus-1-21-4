package net.mastermaxi.mastermaxisvanillaplus.recipe;

import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.mastermaxi.mastermaxisvanillaplus.item.PrismarineArrowRestItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

/**
 * Special crafting recipe allowing players to attach a Prismarine Arrow Rest onto any bow
 * (vanilla Bow, Heavy Bow, Heavy Hardened Bow) in a crafting table,
 * preserving all enchantments, durability, and custom names.
 */
public class AttachPrismarineArrowRestRecipe extends SpecialCraftingRecipe {
    public AttachPrismarineArrowRestRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        ItemStack bow = ItemStack.EMPTY;
        boolean hasRest = false;
        int count = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty()) {
                count++;
                if (stack.getItem() instanceof BowItem && !PrismarineArrowRestItem.hasAttachment(stack)) {
                    if (!bow.isEmpty()) return false;
                    bow = stack;
                } else if (stack.isOf(ItemInit.PRISMARINE_ARROW_REST)) {
                    if (hasRest) return false;
                    hasRest = true;
                } else {
                    return false;
                }
            }
        }
        return count == 2 && !bow.isEmpty() && hasRest;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack bow = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BowItem) {
                bow = stack;
                break;
            }
        }
        if (bow.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = bow.copy();
        PrismarineArrowRestItem.setAttachment(result, true);
        return result;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipeSerializers.ATTACH_PRISMARINE_ARROW_REST;
    }
}
