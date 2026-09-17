package net.mastermaxi.mastermaxisvanillaplus.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

/**
 * Heavy Hardened Bow - further upgrade of the Heavy Bow with iron and flint & steel.
 * Stats vs vanilla bow:
 * - Pull time: 27 ticks (vs 20 ticks = +0.35s)
 * - Velocity multiplier: 1.25x (+6 blocks range)
 * - Damage bonus: +2
 * - Durability: 320 (vs 250 heavy bow)
 */
public class HeavyHardenedBowItem extends HeavyBowItem {

    /** Pull time in ticks for hardened bow (vanilla = 20, hardened = 27 = +0.35s) */
    private static final float HARDENED_PULL_TICKS = 27.0f;
    /** Velocity multiplier (+6 blocks range) */
    private static final float HARDENED_VELOCITY_MULTIPLIER = 1.25f;

    public HeavyHardenedBowItem(Settings settings) {
        super(settings);
    }

    @Override
    public double getDamageBonus() {
        return 2.0;
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
        float progress = MathHelper.clamp((float) usedTicks / HARDENED_PULL_TICKS, 0.0f, 1.0f);
        if (progress < 0.1f) {
            return false;
        }
        List<ItemStack> list = load(stack, itemStack, playerEntity);
        if (world instanceof ServerWorld serverWorld && !list.isEmpty()) {
            this.shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), stack, list, progress * 3.0f * HARDENED_VELOCITY_MULTIPLIER, 1.0f, progress == 1.0f, null);
        }
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f,
                1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + progress * 0.5f);
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        return true;
    }
}
