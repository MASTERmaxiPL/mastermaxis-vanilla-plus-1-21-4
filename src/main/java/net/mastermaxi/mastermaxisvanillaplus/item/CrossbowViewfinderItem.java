package net.mastermaxi.mastermaxisvanillaplus.item;

import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

/**
 * Crossbow with Viewfinder - vanilla crossbow with attached viewfinder.
 * Uses bolts as ammunition.
 * Scoping via sneak provides ~0.35x FOV magnification and purple scope overlay.
 */
public class CrossbowViewfinderItem extends CrossbowItem implements ViewfinderScopeItem {
    public CrossbowViewfinderItem(Settings settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getHeldProjectiles() {
        return stack -> stack.getItem() instanceof BoltItem;
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return stack -> stack.getItem() instanceof BoltItem;
    }
}
