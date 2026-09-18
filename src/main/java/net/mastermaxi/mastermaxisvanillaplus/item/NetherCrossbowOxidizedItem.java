package net.mastermaxi.mastermaxisvanillaplus.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;

/**
 * Oxidized Nether Crossbow - aged copper variant of the Nether Crossbow.
 * Stats vs Nether Crossbow:
 * - Damage bonus: +3 (1 HP less than Nether Crossbow's +4)
 * - Pull time: +13 ticks (+0.65s, which is 0.25s / 5 ticks slower than Nether Crossbow's +8 ticks)
 * - Accuracy: 0.3f divergence (pinpoint accuracy identical to Nether Crossbow)
 * - Durability: 800 uses
 * - Enchantability: 15
 */
public class NetherCrossbowOxidizedItem extends IronCrossbowItem {

    /** Tight divergence identical to nether crossbow */
    private static final float NETHER_DIVERGENCE = 0.3f;
    /** Extra pull ticks (+0.4s base from nether + 5 ticks / 0.25s = 13 ticks) */
    private static final int OXIDIZED_EXTRA_PULL_TICKS = 13;

    public NetherCrossbowOxidizedItem(Settings settings) {
        super(settings);
    }

    @Override
    public double getDamageBonus() {
        return 3.0; // 1 HP less than Nether Crossbow (4.0)
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return CrossbowItem.getPullTime(stack, user) + OXIDIZED_EXTRA_PULL_TICKS + 3;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, LivingEntity target) {
        super.shoot(shooter, projectile, index, speed, NETHER_DIVERGENCE, yaw, target);
    }
}
