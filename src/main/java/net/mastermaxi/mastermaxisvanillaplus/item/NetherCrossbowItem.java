package net.mastermaxi.mastermaxisvanillaplus.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * Nether Crossbow - the most powerful crossbow with +4 damage and +0.4s charge time.
 * Highest accuracy (lowest divergence).
 * Stats vs vanilla crossbow:
 * - Pull time: +8 ticks (+0.4s)
 * - Damage bonus: +4
 * - Divergence: 0.3f (most accurate)
 * - Durability: 800
 * - Enchantability: 15
 * Copper Oxidation Mechanic:
 * Since the Nether Crossbow is partially forged with copper, it naturally oxidizes over time
 * into an Oxidized Nether Crossbow (unless waxed with honey/honeycomb).
 */
public class NetherCrossbowItem extends IronCrossbowItem {

    /** Even lower divergence for nether crossbow */
    private static final float NETHER_DIVERGENCE = 0.3f;
    /** Extra pull ticks for nether crossbow (+0.4s = 8 ticks) */
    private static final int NETHER_EXTRA_PULL_TICKS = 8;
    /** Oxidation duration in ticks: 24000 ticks = 20 minutes (1 in-game day) */
    public static final long OXIDATION_DURATION = 24000L;

    public NetherCrossbowItem(Settings settings) {
        super(settings);
    }

    public static boolean isWaxed(ItemStack stack) {
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData == null || !customData.contains("Waxed")) {
            return false;
        }
        return customData.copyNbt().getBoolean("Waxed");
    }

    public static void setWaxed(ItemStack stack, boolean waxed) {
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        nbt.putBoolean("Waxed", waxed);
        if (waxed) {
            nbt.remove("OxidizeTime");
        }
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }

    public static long getOxidizeTime(ItemStack stack) {
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData != null && customData.contains("OxidizeTime")) {
            return customData.copyNbt().getLong("OxidizeTime");
        }
        return 0L;
    }

    public static void setOxidizeTime(ItemStack stack, long time) {
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        if (time <= 0L) {
            nbt.remove("OxidizeTime");
        } else {
            nbt.putLong("OxidizeTime", time);
        }
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }

    @Override
    public double getDamageBonus() {
        return 4.0;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return CrossbowItem.getPullTime(stack, user) + NETHER_EXTRA_PULL_TICKS + 3;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, LivingEntity target) {
        super.shoot(shooter, projectile, index, speed, NETHER_DIVERGENCE, yaw, target);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (world.isClient() || isWaxed(stack)) {
            return;
        }

        long currentTime = world.getTime();
        long targetTime = getOxidizeTime(stack);

        if (targetTime <= 0L) {
            setOxidizeTime(stack, currentTime + OXIDATION_DURATION);
            return;
        }

        // Accelerate oxidation when exposed to water or rainfall (reduces target time)
        if (currentTime % 20 == 0 && (entity.isSubmergedInWater() || entity.isTouchingWaterOrRain())) {
            long accelerated = Math.max(currentTime, targetTime - 20L);
            setOxidizeTime(stack, accelerated);
            targetTime = accelerated;
        }

        if (currentTime >= targetTime) {
            if (entity instanceof PlayerEntity player) {
                Item targetItem = stack.isOf(ItemInit.NETHER_CROSSBOW_VIEWFINDER) ?
                        ItemInit.NETHER_CROSSBOW_OXIDIZED_VIEWFINDER : ItemInit.NETHER_CROSSBOW_OXIDIZED;
                ItemStack newStack = stack.copyComponentsToNewStack(targetItem, 1);
                newStack.setDamage(stack.getDamage());
                newStack.set(DataComponentTypes.ENCHANTABLE, targetItem.getComponents().get(DataComponentTypes.ENCHANTABLE));

                // Clean up oxidation timer tag on the oxidized item
                NbtCompound nbt = newStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
                nbt.remove("OxidizeTime");
                newStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

                player.getInventory().setStack(slot, newStack);

                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.BLOCK_COPPER_DOOR_OPEN, SoundCategory.PLAYERS, 1.0f, 0.7f);
                    serverWorld.spawnParticles(ParticleTypes.SCRAPE, player.getX(), player.getY() + 1.0, player.getZ(),
                            12, 0.3, 0.3, 0.3, 0.1);
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        if (isWaxed(stack)) {
            tooltip.add(Text.translatable("tooltip.mastermaxisvanillaplus.waxed").formatted(Formatting.GOLD));
        }
    }
}
