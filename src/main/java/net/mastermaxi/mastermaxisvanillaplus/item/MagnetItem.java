package net.mastermaxi.mastermaxisvanillaplus.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class MagnetItem extends Item {
    public static final double ATTRACTION_RADIUS = 8.0;

    public MagnetItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient() || !(entity instanceof PlayerEntity player)) {
            return;
        }

        if (player.isSpectator()) {
            return;
        }

        // Only attract items when held in main hand or off-hand
        boolean isMainHand = (player.getMainHandStack() == stack);
        boolean isOffHand = (player.getOffHandStack() == stack);
        if (!isMainHand && !isOffHand) {
            return;
        }

        // Avoid ticking twice in the same tick if held in both hands
        if (isOffHand && player.getMainHandStack().isOf(this)) {
            return;
        }

        Box box = new Box(
                player.getX() - ATTRACTION_RADIUS, player.getY() - ATTRACTION_RADIUS, player.getZ() - ATTRACTION_RADIUS,
                player.getX() + ATTRACTION_RADIUS, player.getY() + ATTRACTION_RADIUS, player.getZ() + ATTRACTION_RADIUS
        );

        List<ItemEntity> items = world.getEntitiesByClass(
                ItemEntity.class, box,
                e -> canAttract(e, player)
        );

        if (items.isEmpty()) {
            return;
        }

        Vec3d targetPos = player.getPos().add(0, 0.75, 0);

        for (ItemEntity itemEntity : items) {
            Vec3d itemPos = itemEntity.getPos();
            Vec3d diff = targetPos.subtract(itemPos);
            double dist = diff.length();

            if (dist < 1.5) {
                itemEntity.resetPickupDelay();
                itemEntity.onPlayerCollision(player);
            } else if (dist <= ATTRACTION_RADIUS) {
                double speed = 0.45 + (ATTRACTION_RADIUS - dist) * 0.03;
                Vec3d motion = diff.normalize().multiply(Math.min(speed, 0.75));
                itemEntity.setVelocity(motion);
                itemEntity.velocityDirty = true;
                itemEntity.velocityModified = true;
            }
        }
    }

    public static boolean canAttract(ItemEntity itemEntity, PlayerEntity player) {
        if (!itemEntity.isAlive()) {
            return false;
        }

        int pickupDelay = ((net.mastermaxi.mastermaxisvanillaplus.mixin.ItemEntityAccessor) itemEntity).getPickupDelay();
        // Never attract items marked with infinite delay
        if (pickupDelay >= 32767) {
            return false;
        }

        // If thrown by this player, wait a very brief moment (5 ticks / 0.25s) before dragging back
        // instead of vanilla's 40 ticks (2.0s)
        if (itemEntity.getOwner() == player) {
            return pickupDelay <= 35;
        }

        // For non-player drops (block drops, mob drops, chest drops, etc.), start dragging immediately
        return true;
    }
}
