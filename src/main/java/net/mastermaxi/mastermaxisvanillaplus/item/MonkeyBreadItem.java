package net.mastermaxi.mastermaxisvanillaplus.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MonkeyBreadItem extends Item {
    public MonkeyBreadItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack result = super.finishUsing(stack, world, user);
        if (!world.isClient()) {
            user.heal(2.0F);
        }
        return result;
    }
}
