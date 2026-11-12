package net.mastermaxi.mastermaxisvanillaplus.entity;

import net.mastermaxi.mastermaxisvanillaplus.mixin.PersistentProjectileEntityAccessor;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Unit;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ProjectileEntityHelper {

    public static void initProjectile(PersistentProjectileEntity projectile, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack weapon) {
        initProjectilePos(projectile, owner.getX(), owner.getEyeY() - 0.10000000149011612, owner.getZ(), world, stack, weapon);
        projectile.setOwner(owner);
    }

    public static void initProjectilePos(PersistentProjectileEntity projectile, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        ItemStack copy = stack.copy();
        projectile.setCustomName(copy.get(DataComponentTypes.CUSTOM_NAME));
        Unit unit = copy.remove(DataComponentTypes.INTANGIBLE_PROJECTILE);
        if (unit != null) {
            projectile.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        }
        projectile.setPosition(x, y, z);
        ((PersistentProjectileEntityAccessor) projectile).callSetStack(copy);

        if (weapon != null && world instanceof ServerWorld serverWorld) {
            if (!weapon.isEmpty()) {
                PersistentProjectileEntityAccessor accessor = (PersistentProjectileEntityAccessor) projectile;
                accessor.setWeapon(weapon.copy());
                int i = EnchantmentHelper.getProjectilePiercing(serverWorld, weapon, copy);
                if (i > 0) {
                    accessor.callSetPierceLevel((byte) i);
                }
            }
        }
    }
}
