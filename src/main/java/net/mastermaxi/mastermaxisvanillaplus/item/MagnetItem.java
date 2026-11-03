package net.mastermaxi.mastermaxisvanillaplus.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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
        boolean isOffHand = (slot == PlayerInventory.OFF_HAND_SLOT);
        if (!selected && !isOffHand) {
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
                e -> e.isAlive() && !e.cannotPickup()
        );

        if (items.isEmpty()) {
            return;
        }

        Vec3d targetPos = player.getPos().add(0, 0.75, 0);

        for (ItemEntity itemEntity : items) {
            Vec3d itemPos = itemEntity.getPos();
            Vec3d diff = targetPos.subtract(itemPos);
            double dist = diff.length();

            if (dist < 1.2) {
                itemEntity.onPlayerCollision(player);
            } else if (dist <= ATTRACTION_RADIUS) {
                double speed = 0.35 + (ATTRACTION_RADIUS - dist) * 0.02;
                Vec3d motion = diff.normalize().multiply(Math.min(speed, 0.65));
                itemEntity.setVelocity(motion);
                itemEntity.velocityDirty = true;
                itemEntity.velocityModified = true;
            }
        }
    }
}
