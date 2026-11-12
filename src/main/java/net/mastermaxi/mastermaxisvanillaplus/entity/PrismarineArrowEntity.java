package net.mastermaxi.mastermaxisvanillaplus.entity;

import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Prismarine Arrow entity.
 * - Drastically reduced drag in water (0.99F vs 0.6F vanilla) so it flies freely underwater.
 * - Higher gravity (0.075 vs 0.05 vanilla) and slightly reduced launch speed (0.85x),
 *   giving it a shorter maximum flight distance compared to standard arrows.
 */
public class PrismarineArrowEntity extends ArrowEntity {

    public PrismarineArrowEntity(EntityType<? extends PrismarineArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public PrismarineArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        this(EntityInit.PRISMARINE_ARROW, world);
        ProjectileEntityHelper.initProjectilePos(this, x, y, z, world, stack, shotFrom);
    }

    public PrismarineArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        this(EntityInit.PRISMARINE_ARROW, world);
        ProjectileEntityHelper.initProjectile(this, owner, world, stack, shotFrom);
    }

    @Override
    protected double getGravity() {
        return 0.075;
    }

    @Override
    protected float getDragInWater() {
        return 0.99f;
    }

    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        super.setVelocity(x, y, z, speed * 0.85f, divergence);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ItemInit.PRISMARINE_ARROW);
    }
}
