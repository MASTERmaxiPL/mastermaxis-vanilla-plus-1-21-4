package net.mastermaxi.mastermaxisvanillaplus.entity;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class MudBallEntity extends ThrownItemEntity implements FlyingItemEntity {

    public MudBallEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public MudBallEntity(EntityType<? extends ThrownItemEntity> entityType, World world, LivingEntity owner, ItemStack thrownStack) {
        super(entityType, owner, world, thrownStack);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemInit.MUD_BALL;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.getWorld().isClient() && hitResult instanceof EntityHitResult entityHitResult && this.getWorld() instanceof ServerWorld serverWorld) {
            Entity target = entityHitResult.getEntity();

            DamageSource mudBallSource = this.getDamageSource();
            target.damage(serverWorld, mudBallSource, 0.5F);

            if (target instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 0), this.getEffectCause());
            }

            MASTERmaxisVanillaPlus.LOGGER.info("Mud Ball dealt 2 damage to {}", target.getName().getString());
        }

        this.getWorld().addParticle(
                ParticleTypes.DUST_PLUME,
                this.getX(), this.getY(), this.getZ(),
                0, 0, 0
        );
        this.discard();
    }

    private DamageSource getDamageSource() {
        var owner = this.getOwner();
        return this.getWorld().getDamageSources().thrown(this, owner);
    }
}
