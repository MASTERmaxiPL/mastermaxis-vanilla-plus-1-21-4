package net.mastermaxi.mastermaxisvanillaplus.entity;

import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HeavyArrowEntity extends ArrowEntity {
    public HeavyArrowEntity(EntityType<? extends HeavyArrowEntity> entityType, World world) {
        super(entityType, world);
        this.setDamage(this.getDamage() + 1.0);
    }

    public HeavyArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityInit.HEAVY_ARROW, world);
        ProjectileEntityHelper.initProjectilePos(this, x, y, z, world, stack, shotFrom);
        this.setDamage(this.getDamage() + 1.0);
    }

    public HeavyArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityInit.HEAVY_ARROW, world);
        ProjectileEntityHelper.initProjectile(this, owner, world, stack, shotFrom);
        this.setDamage(this.getDamage() + 1.0);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ItemInit.HEAVY_ARROW);
    }
}
