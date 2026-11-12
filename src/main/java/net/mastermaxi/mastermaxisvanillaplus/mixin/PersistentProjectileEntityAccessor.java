package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PersistentProjectileEntity.class)
public interface PersistentProjectileEntityAccessor {
    @Accessor("weapon")
    void setWeapon(ItemStack weapon);

    @Invoker("setPierceLevel")
    void callSetPierceLevel(byte level);

    @Invoker("setStack")
    void callSetStack(ItemStack stack);
}
