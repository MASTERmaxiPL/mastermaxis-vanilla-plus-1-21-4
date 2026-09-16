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

    @SuppressWarnings("unused")
    public BoltEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(world, x, y, z, stack, shotFrom);
    }

    public BoltEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(world, owner, stack, shotFrom);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ItemInit.BOLT);
    }
}
