package net.mastermaxi.mastermaxisvanillaplus.recipe;

import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.mastermaxi.mastermaxisvanillaplus.item.PyrotechnicTubeItem;
import net.mastermaxi.mastermaxisvanillaplus.screen.FletchingTableScreenHandler;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

/**
 * Special crafting recipe allowing players to attach a Pyrotechnic Tube onto any crossbow
 * in a crafting table, preserving all enchantments, durability, and custom names.
 */
public class AttachPyrotechnicTubeRecipe extends SpecialCraftingRecipe {
    public AttachPyrotechnicTubeRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        ItemStack crossbow = ItemStack.EMPTY;
        boolean hasTube = false;
        int count = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty()) {
                count++;
                if (stack.getItem() instanceof CrossbowItem
                        && !PyrotechnicTubeItem.hasAttachment(stack)
                        && !FletchingTableScreenHandler.isViewfinderCrossbow(stack)) {
                    if (!crossbow.isEmpty()) return false;
                    crossbow = stack;
                } else if (stack.isOf(ItemInit.PYROTECHNIC_TUBE)) {
                    if (hasTube) return false;
                    hasTube = true;
                } else {
                    return false;
                }
            }
        }
        return count == 2 && !crossbow.isEmpty() && hasTube;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack crossbow = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof CrossbowItem) {
                crossbow = stack;
                break;
            }
        }
        if (crossbow.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = crossbow.copy();
        PyrotechnicTubeItem.setAttachment(result, true);
        return result;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipeSerializers.ATTACH_PYROTECHNIC_TUBE;
    }
}
