package net.mastermaxi.mastermaxisvanillaplus.recipe;

import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.mastermaxi.mastermaxisvanillaplus.item.NetherCrossbowItem;
import net.mastermaxi.mastermaxisvanillaplus.item.NetherCrossbowOxidizedItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.World;

/**
 * Special crafting recipe allowing players to scrape an Oxidized Nether Crossbow with an Axe,
 * restoring it back into a standard Nether Crossbow while preserving all enchantments and durability.
 */
public class ScrapeNetherCrossbowRecipe extends SpecialCraftingRecipe {
    public ScrapeNetherCrossbowRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        ItemStack crossbow = ItemStack.EMPTY;
        boolean hasAxe = false;
        int count = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty()) {
                count++;
                if (stack.getItem() instanceof NetherCrossbowOxidizedItem) {
                    if (!crossbow.isEmpty()) return false;
                    crossbow = stack;
                } else if (stack.isIn(ItemTags.AXES)) {
                    if (hasAxe) return false;
                    hasAxe = true;
                } else {
                    return false;
                }
            }
        }
        return count == 2 && !crossbow.isEmpty() && hasAxe;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack crossbow = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof NetherCrossbowOxidizedItem) {
                crossbow = stack;
                break;
            }
        }
        if (crossbow.isEmpty()) {
            return ItemStack.EMPTY;
        }

        Item target = crossbow.isOf(ItemInit.NETHER_CROSSBOW_OXIDIZED_VIEWFINDER) ?
                ItemInit.NETHER_CROSSBOW_VIEWFINDER : ItemInit.NETHER_CROSSBOW;
        ItemStack result = crossbow.copyComponentsToNewStack(target, 1);
        result.setDamage(crossbow.getDamage());
        result.set(DataComponentTypes.ENCHANTABLE, target.getComponents().get(DataComponentTypes.ENCHANTABLE));
        // Reset oxidation timer so it starts a fresh cycle
        NetherCrossbowItem.setOxidizeTime(result, 0L);
        return result;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipeSerializers.SCRAPE_NETHER_CROSSBOW;
    }
}
