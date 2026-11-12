package net.mastermaxi.mastermaxisvanillaplus.entity;

import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Prismarine Heavy Arrow entity.
 * Inherits underwater flight and shorter range from PrismarineArrowEntity,
 * and adds +1.0 base damage bonus like HeavyArrowEntity.
 */
public class PrismarineHeavyArrowEntity extends PrismarineArrowEntity {

    public PrismarineHeavyArrowEntity(EntityType<? extends PrismarineHeavyArrowEntity> entityType, World world) {
        super(entityType, world);
        this.setDamage(this.getDamage() + 1.0);
    }

    public PrismarineHeavyArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityInit.PRISMARINE_HEAVY_ARROW, world);
        ProjectileEntityHelper.initProjectilePos(this, x, y, z, world, stack, shotFrom);
        this.setDamage(this.getDamage() + 1.0);
    }

    public PrismarineHeavyArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityInit.PRISMARINE_HEAVY_ARROW, world);
        ProjectileEntityHelper.initProjectile(this, owner, world, stack, shotFrom);
        this.setDamage(this.getDamage() + 1.0);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ItemInit.PRISMARINE_HEAVY_ARROW);
    }
}
