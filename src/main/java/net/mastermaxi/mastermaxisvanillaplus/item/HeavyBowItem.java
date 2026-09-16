package net.mastermaxi.mastermaxisvanillaplus.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

/**
 * Heavy Bow - upgraded bow crafted with leather, slime ball and string from a regular bow.
 * Stats vs vanilla bow:
 * - Pull time: 25 ticks (vs 20 ticks = +0.25s)
 * - Velocity multiplier: 1.15x (+4 blocks range)
 * - Damage bonus: +1
 * - Durability: 250 (vs 384 vanilla)
 * - Can shoot Heavy Arrows (unlike vanilla bow)
 */
public class HeavyBowItem extends BowItem {

    /** Pull time in ticks (vanilla = 20 ticks = 1.0s, heavy = 25 ticks = 1.25s) */
    protected static final float PULL_TICKS = 25.0f;
    /** Velocity multiplier for projectile speed */
    protected static final float VELOCITY_MULTIPLIER = 1.15f;

    public HeavyBowItem(Settings settings) {
        super(settings);
    }

    public double getDamageBonus() {
        return 1.0;
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
    public Predicate<ItemStack> getProjectiles() {
        // Can shoot any arrow, including Heavy Arrow
        return stack -> stack.isIn(ItemTags.ARROWS);
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity playerEntity)) {
            return false;
        }
        ItemStack itemStack = playerEntity.getProjectileType(stack);
        if (itemStack.isEmpty()) {
            return false;
        }
        int usedTicks = this.getMaxUseTime(stack, user) - remainingUseTicks;
        float progress = MathHelper.clamp((float) usedTicks / PULL_TICKS, 0.0f, 1.0f);
        if (progress < 0.1f) {
            return false;
        }
        List<ItemStack> list = load(stack, itemStack, playerEntity);
        if (world instanceof ServerWorld serverWorld && !list.isEmpty()) {
            this.shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), stack, list, progress * 3.0f * VELOCITY_MULTIPLIER, 1.0f, progress == 1.0f, null);
        }
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f,
                1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + progress * 0.5f);
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        return true;
    }
}
