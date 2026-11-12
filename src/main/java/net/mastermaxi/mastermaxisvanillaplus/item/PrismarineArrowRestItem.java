package net.mastermaxi.mastermaxisvanillaplus.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Prismarine Arrow Rest - bow attachment enabling the shooting of prismarine arrows.
 * Can be mounted/unmounted on Bows, Heavy Bows, and Heavy Hardened Bows in the Fletching Table
 * or crafted onto a bow in a crafting table.
 */
public class PrismarineArrowRestItem extends Item {
    public static final String NBT_KEY = "PrismarineArrowRest";

    public PrismarineArrowRestItem(Settings settings) {
        super(settings);
    }

    public static boolean hasAttachment(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData == null || !customData.contains(NBT_KEY)) {
            return false;
        }
        return customData.copyNbt().getBoolean(NBT_KEY);
    }

    public static void setAttachment(ItemStack stack, boolean attached) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        if (attached) {
            nbt.putBoolean(NBT_KEY, true);
            stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
        } else {
            nbt.remove(NBT_KEY);
            if (nbt.isEmpty()) {
                stack.remove(DataComponentTypes.CUSTOM_DATA);
            } else {
                stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
            }
        }
    }

    public static final java.util.function.Predicate<ItemStack> VANILLA_BOW_PROJECTILES = stack ->
            stack.isIn(net.minecraft.registry.tag.ItemTags.ARROWS)
                    && !stack.isOf(ItemInit.HEAVY_ARROW)
                    && !stack.isOf(ItemInit.PRISMARINE_ARROW)
                    && !stack.isOf(ItemInit.PRISMARINE_HEAVY_ARROW);

    public static java.util.function.Predicate<ItemStack> getBowProjectilePredicate(ItemStack weapon) {
        boolean isHeavyBow = weapon.getItem() instanceof HeavyBowItem;
        boolean hasRest = hasAttachment(weapon);

        return stack -> {
            if (!stack.isIn(net.minecraft.registry.tag.ItemTags.ARROWS)) {
                return false;
            }

            if (stack.isOf(ItemInit.PRISMARINE_HEAVY_ARROW)) {
                return isHeavyBow && hasRest;
            }

            if (stack.isOf(ItemInit.PRISMARINE_ARROW)) {
                return hasRest;
            }

            if (stack.isOf(ItemInit.HEAVY_ARROW)) {
                return isHeavyBow;
            }

            return true;
        };
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("item.mastermaxisvanillaplus.prismarine_arrow_rest.desc"));
    }
}
