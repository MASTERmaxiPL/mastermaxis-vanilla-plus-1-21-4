package net.mastermaxi.mastermaxisvanillaplus.entity.ai;

import net.mastermaxi.mastermaxisvanillaplus.entity.custom.MuddyPigAccess;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.EnumSet;

public class PigEatGrassGoal extends Goal {
    private final PigEntity pig;
    private final World world;
    private int timer;

    public PigEatGrassGoal(PigEntity pig) {
        this.pig = pig;
        this.world = pig.getWorld();
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
    }

    @Override
    public boolean canStart() {
        if (this.pig.getRandom().nextInt(120) != 0) {
            return false;
        }
        if (((MuddyPigAccess) this.pig).mastermaxisvanillaplus$isDigging()) {
            return false;
        }
        BlockPos pos = this.pig.getBlockPos();
        if (this.world.getBlockState(pos).isOf(Blocks.SHORT_GRASS)) {
            return true;
        }
        BlockPos downPos = pos.down();
        return this.world.getBlockState(downPos).isOf(Blocks.GRASS_BLOCK)
                || this.world.getBlockState(downPos).isOf(net.mastermaxi.mastermaxisvanillaplus.block.Blocks.GRASSY_RED_SOIL);
    }

    @Override
    public void start() {
        this.timer = this.getTickCount(40);
        this.world.sendEntityStatus(this.pig, (byte) 10);
        this.pig.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.timer = 0;
    }

    @Override
    public boolean shouldContinue() {
        return this.timer > 0;
    }

    @Override
    public void tick() {
        this.timer = Math.max(0, this.timer - 1);
        if (this.timer == this.getTickCount(4) && this.world instanceof ServerWorld serverWorld) {
            BlockPos pos = this.pig.getBlockPos();
            BlockPos downPos = pos.down();
            boolean ate = false;
            boolean canGrief = serverWorld.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);

            if (this.world.getBlockState(pos).isOf(Blocks.SHORT_GRASS)) {
                if (canGrief) {
                    this.world.breakBlock(pos, false);
                }
                ate = true;
            } else if (this.world.getBlockState(downPos).isOf(Blocks.GRASS_BLOCK)) {
                if (canGrief) {
                    this.world.syncWorldEvent(2001, downPos, Block.getRawIdFromState(Blocks.GRASS_BLOCK.getDefaultState()));
                    this.world.setBlockState(downPos, Blocks.DIRT.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
                ate = true;
            } else if (this.world.getBlockState(downPos).isOf(net.mastermaxi.mastermaxisvanillaplus.block.Blocks.GRASSY_RED_SOIL)) {
                if (canGrief) {
                    this.world.syncWorldEvent(2001, downPos, Block.getRawIdFromState(net.mastermaxi.mastermaxisvanillaplus.block.Blocks.GRASSY_RED_SOIL.getDefaultState()));
                    this.world.setBlockState(downPos, net.mastermaxi.mastermaxisvanillaplus.block.Blocks.RED_SOIL.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
                ate = true;
            }

            if (ate) {
                ((MuddyPigAccess) this.pig).mastermaxisvanillaplus$addHappiness(15);
                this.pig.playSound(SoundEvents.ENTITY_PIG_AMBIENT, 1.0F, (this.pig.getRandom().nextFloat() - this.pig.getRandom().nextFloat()) * 0.2F + 1.0F);
                serverWorld.spawnParticles(
                        ParticleTypes.HAPPY_VILLAGER,
                        this.pig.getX(), this.pig.getY() + 0.5, this.pig.getZ(),
                        3, 0.2, 0.2, 0.2, 0.0
                );
            }
        }
    }
}
