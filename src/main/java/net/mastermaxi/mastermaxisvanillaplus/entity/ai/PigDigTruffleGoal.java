package net.mastermaxi.mastermaxisvanillaplus.entity.ai;

import net.mastermaxi.mastermaxisvanillaplus.entity.custom.MuddyPigAccess;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class PigDigTruffleGoal extends Goal {
    private final PigEntity pig;
    private int digTicks;

    public PigDigTruffleGoal(PigEntity pig) {
        this.pig = pig;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
    }

    @Override
    public boolean canStart() {
        if (this.pig.isBaby()) {
            return false;
        }
        MuddyPigAccess access = (MuddyPigAccess) this.pig;
        if (access.mastermaxisvanillaplus$getMudVariant() == 0) {
            return false;
        }
        if (access.mastermaxisvanillaplus$getHappiness() < 75) {
            return false;
        }
        if (this.pig.getRandom().nextInt(140) != 0) {
            return false;
        }
        BlockPos downPos = this.pig.getBlockPos().down();
        BlockState state = this.pig.getWorld().getBlockState(downPos);
        return state.isIn(BlockTags.DIRT);
    }

    @Override
    public boolean shouldContinue() {
        return this.digTicks > 0;
    }

    @Override
    public void start() {
        this.digTicks = 70; // ~3.5 seconds
        this.pig.getNavigation().stop();
        ((MuddyPigAccess) this.pig).mastermaxisvanillaplus$setDigging(true);
    }

    @Override
    public void stop() {
        this.digTicks = 0;
        ((MuddyPigAccess) this.pig).mastermaxisvanillaplus$setDigging(false);
    }

    @Override
    public void tick() {
        this.digTicks--;
        this.pig.getNavigation().stop();

        BlockPos downPos = this.pig.getBlockPos().down();
        BlockState groundState = this.pig.getWorld().getBlockState(downPos);

        if (this.pig.getWorld() instanceof ServerWorld serverWorld) {
            Vec3d lookVec = this.pig.getRotationVector();
            double frontX = this.pig.getX() + lookVec.x * 0.4;
            double frontY = this.pig.getY();
            double frontZ = this.pig.getZ() + lookVec.z * 0.4;

            if (this.digTicks % 4 == 0) {
                serverWorld.spawnParticles(
                        new BlockStateParticleEffect(ParticleTypes.BLOCK, groundState),
                        frontX, frontY + 0.1, frontZ,
                        5, 0.15, 0.05, 0.15, 0.05
                );
            }

            if (this.digTicks % 12 == 0) {
                this.pig.getWorld().playSound(
                        null, this.pig.getBlockPos(),
                        SoundEvents.BLOCK_GRAVEL_HIT, SoundCategory.NEUTRAL,
                        0.7F, 0.9F + this.pig.getRandom().nextFloat() * 0.2F
                );
            }

            if (this.digTicks == 1) {
                ItemStack reward;
                int roll = this.pig.getRandom().nextInt(100);
                if (roll < 60) {
                    reward = new ItemStack(ItemInit.TRUFFLE, 1);
                } else if (roll < 85) {
                    reward = new ItemStack(Items.BROWN_MUSHROOM, 1);
                } else {
                    reward = new ItemStack(Items.RED_MUSHROOM, 1);
                }

                ItemEntity itemEntity = new ItemEntity(serverWorld, frontX, frontY + 0.3, frontZ, reward);
                itemEntity.setVelocity(lookVec.x * 0.1, 0.25, lookVec.z * 0.1);
                serverWorld.spawnEntity(itemEntity);

                this.pig.getWorld().playSound(
                        null, this.pig.getBlockPos(),
                        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL,
                        1.0F, 1.2F
                );
                this.pig.playSound(SoundEvents.ENTITY_PIG_AMBIENT, 1.0F, 1.3F);

                serverWorld.spawnParticles(
                        ParticleTypes.HAPPY_VILLAGER,
                        this.pig.getX(), this.pig.getY() + 0.6, this.pig.getZ(),
                        6, 0.3, 0.2, 0.3, 0.0
                );

                MuddyPigAccess access = (MuddyPigAccess) this.pig;
                access.mastermaxisvanillaplus$addHappiness(-25);
            }
        }
    }
}
