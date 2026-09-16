package net.mastermaxi.mastermaxisvanillaplus.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.function.Predicate;

/**
 * Iron Crossbow - stronger crossbow with +2 damage and +0.2s charge time.
 * Uses bolts as ammunition. Better accuracy (lower divergence).
 * Stats vs vanilla crossbow:
 * - Pull time: +4 ticks (+0.2s)
 * - Damage bonus: +2
 * - Divergence: 0.6f (vs 1.0f vanilla = more accurate)
 * - Durability: 600
 */
public class IronCrossbowItem extends CrossbowItem {

    /** Divergence for iron crossbow (lower = more accurate; vanilla = 1.0f) */
    protected static final float DIVERGENCE = 0.6f;
    /** Additional pull ticks over vanilla (25 base + 4 = +0.2s) */
    protected static final int EXTRA_PULL_TICKS = 4;

    public IronCrossbowItem(Settings settings) {
        super(settings);
    }

    public double getDamageBonus() {
        return 2.0;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return CrossbowItem.getPullTime(stack, user) + EXTRA_PULL_TICKS + 3;
    }

    @Override
    protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        ProjectileEntity projectile = super.createArrowEntity(world, shooter, weaponStack, projectileStack, critical);
        if (projectile instanceof PersistentProjectileEntity persistentProjectile) {
            persistentProjectile.setDamage(persistentProjectile.getDamage() + getDamageBonus());
        }
        return projectile;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, LivingEntity target) {
        super.shoot(shooter, projectile, index, speed, DIVERGENCE, yaw, target);
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
