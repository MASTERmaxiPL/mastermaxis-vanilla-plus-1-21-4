package net.mastermaxi.mastermaxisvanillaplus.item;

import net.mastermaxi.mastermaxisvanillaplus.entity.HeavyArrowEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Language;
import net.minecraft.world.World;

import java.util.List;

public class HeavyArrowItem extends ArrowItem {
    public HeavyArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, ItemStack shotFrom) {
        return new HeavyArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        PotionContentsComponent potionContentsComponent = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContentsComponent != null) {
            potionContentsComponent.buildTooltip(tooltip::add, 0.125F, context.getUpdateTickRate());
        }
    }

    @Override
    public Text getName(ItemStack stack) {
        PotionContentsComponent potionContentsComponent = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContentsComponent != null) {
            Text effectName = potionContentsComponent.getName(this.getTranslationKey() + ".effect.");
            if (effectName.getContent() instanceof TranslatableTextContent translatable &&
                    !Language.getInstance().hasTranslation(translatable.getKey())) {
                Text vanillaArrow = potionContentsComponent.getName("item.minecraft.tipped_arrow.effect.");
                String str = vanillaArrow.getString();
                if (str.startsWith("Arrow of ")) {
                    return Text.literal("Heavy Arrow of " + str.substring("Arrow of ".length()));
                } else if (str.startsWith("Strzała z ")) {
                    return Text.literal("Ciężka strzała z " + str.substring("Strzała z ".length()));
                } else if (str.startsWith("Strzała ")) {
                    return Text.literal("Ciężka strzała " + str.substring("Strzała ".length()));
                }
            }
            return effectName;
        }
        return super.getName(stack);
    }
}
