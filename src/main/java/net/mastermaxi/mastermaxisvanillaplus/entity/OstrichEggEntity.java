package net.mastermaxi.mastermaxisvanillaplus.entity;

import net.mastermaxi.mastermaxisvanillaplus.entity.custom.OstrichEntity;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class OstrichEggEntity extends ThrownItemEntity {
    public OstrichEggEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public OstrichEggEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityInit.OSTRICH_EGG, owner, world, stack);
    }

    public OstrichEggEntity(World world, double x, double y, double z, ItemStack stack) {
        super(EntityInit.OSTRICH_EGG, x, y, z, world, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemInit.OSTRICH_EGG;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            for (int i = 0; i < 8; ++i) {
                this.getWorld().addParticle(
                        new ItemStackParticleEffect(ParticleTypes.ITEM, this.getStack()),
                        this.getX(), this.getY(), this.getZ(),
                        ((double)this.random.nextFloat() - 0.5) * 0.08,
                        ((double)this.random.nextFloat() - 0.5) * 0.08,
                        ((double)this.random.nextFloat() - 0.5) * 0.08
                );
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            entityHitResult.getEntity().damage(serverWorld, this.getDamageSources().thrown(this, this.getOwner()), 0.0F);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            if (this.random.nextInt(8) == 0) {
                int count = 1;
                if (this.random.nextInt(32) == 0) {
                    count = 4;
                }

                for (int i = 0; i < count; ++i) {
                    OstrichEntity ostrich = EntityInit.OSTRICH.create(serverWorld, SpawnReason.TRIGGERED);
                    if (ostrich != null) {
                        ostrich.setBreedingAge(-24000);
                        ostrich.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
                        serverWorld.spawnEntity(ostrich);
                    }
                }
            }

            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
    }
}
