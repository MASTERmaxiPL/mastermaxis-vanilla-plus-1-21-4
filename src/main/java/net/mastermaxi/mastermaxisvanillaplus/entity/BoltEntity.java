package net.mastermaxi.mastermaxisvanillaplus.entity;

import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BoltEntity extends ArrowEntity {
    public BoltEntity(EntityType<? extends BoltEntity> entityType, World world) {
        super(entityType, world);
    }

    public BoltEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityInit.BOLT, world);
        ProjectileEntityHelper.initProjectilePos(this, x, y, z, world, stack, shotFrom);
    }

    public BoltEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityInit.BOLT, world);
        ProjectileEntityHelper.initProjectile(this, owner, world, stack, shotFrom);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ItemInit.BOLT);
    }
}
