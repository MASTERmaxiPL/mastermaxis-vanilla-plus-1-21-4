package net.mastermaxi.mastermaxisvanillaplus.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class SchematicItem extends Item {
    private final Text description;

    public SchematicItem(Settings settings, Text description) {
        super(settings);
        this.description = description;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        if (description != null) {
            tooltip.add(description);
        }
    }
}
