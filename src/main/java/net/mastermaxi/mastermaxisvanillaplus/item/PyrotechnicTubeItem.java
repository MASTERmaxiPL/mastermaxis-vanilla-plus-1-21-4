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
 * Pyrotechnic Tube - crossbow attachment enabling the use of fireworks as ammunition.
 * Can be mounted/unmounted in the Fletching Table attachment slot or crafted onto a crossbow.
 */
public class PyrotechnicTubeItem extends Item {
    public static final String NBT_KEY = "PyrotechnicTube";

    public PyrotechnicTubeItem(Settings settings) {
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
        } else {
            nbt.remove(NBT_KEY);
        }
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("item.mastermaxisvanillaplus.pyrotechnic_tube.desc"));
    }
}
