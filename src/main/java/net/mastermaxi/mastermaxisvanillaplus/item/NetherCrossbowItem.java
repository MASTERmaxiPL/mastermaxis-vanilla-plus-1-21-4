package net.mastermaxi.mastermaxisvanillaplus.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;

/**
 * Nether Crossbow - the most powerful crossbow with +4 damage and +0.4s charge time.
 * Highest accuracy (lowest divergence).
 * Stats vs vanilla crossbow:
 * - Pull time: +8 ticks (+0.4s)
 * - Damage bonus: +4
 * - Divergence: 0.3f (most accurate)
 * - Durability: 800
 */
public class NetherCrossbowItem extends IronCrossbowItem {

    /** Even lower divergence for nether crossbow */
    private static final float NETHER_DIVERGENCE = 0.3f;
    /** Extra pull ticks for nether crossbow (+0.4s = 8 ticks) */
    private static final int NETHER_EXTRA_PULL_TICKS = 8;

    public NetherCrossbowItem(Settings settings) {
        super(settings);
    }

    @Override
    public double getDamageBonus() {
        return 4.0;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return CrossbowItem.getPullTime(stack, user) + NETHER_EXTRA_PULL_TICKS + 3;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, LivingEntity target) {
        super.shoot(shooter, projectile, index, speed, NETHER_DIVERGENCE, yaw, target);
    }
}
