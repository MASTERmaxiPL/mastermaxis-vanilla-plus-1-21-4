package net.mastermaxi.mastermaxisvanillaplus.entity.ai;

import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.OstrichEntity;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class EatTermitesGoal extends MoveToTargetPosGoal {
    private final OstrichEntity ostrich;
    private int eatingTicks = 0;
    private boolean hasFinishedEating = false;

    public EatTermitesGoal(OstrichEntity ostrich, double speed, int range) {
        super(ostrich, speed, range, 6);
        this.ostrich = ostrich;
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        return world.getBlockState(pos).isOf(Blocks.INFECTED_MOUND);
    }

    @Override
    public double getDesiredDistanceToTarget() {
        return 2.5;
    }

    @Override
    protected int getInterval(PathAwareEntity mob) {
        // Delay between trying to find infected mounds: 30 to 60 seconds
        return toGoalTicks(600 + mob.getRandom().nextInt(600));
    }

    @Override
    public boolean shouldContinue() {
        if (this.hasFinishedEating) {
            return false;
        }
        if (!this.ostrich.getWorld().getBlockState(this.targetPos).isOf(Blocks.INFECTED_MOUND)) {
            return false;
        }
        return super.shouldContinue();
    }

    @Override
    public void start() {
        super.start();
        this.eatingTicks = 0;
        this.hasFinishedEating = false;
    }

    @Override
    public void tick() {
        super.tick();

        BlockPos moundPos = this.targetPos;
        // Verify target is still an infected mound
        if (!this.ostrich.getWorld().getBlockState(moundPos).isOf(Blocks.INFECTED_MOUND)) {
            this.hasFinishedEating = true;
            return;
        }

        if (this.hasReached()) {
            this.ostrich.getLookControl().lookAt(moundPos.getX() + 0.5, moundPos.getY() + 0.5, moundPos.getZ() + 0.5);

            this.eatingTicks++;
            // Trigger pecking animation periodically while eating
            if (this.eatingTicks % 12 == 0) {
                this.ostrich.playPeckAnimation();
                if (this.ostrich.getWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.INFECTED_MOUND.getDefaultState()),
                            moundPos.getX() + 0.5, moundPos.getY() + 1.0, moundPos.getZ() + 0.5, 6, 0.2, 0.1, 0.2, 0.05);
                }
            }

            // After pecking for ~36 ticks (~1.8s), cure the mound and finish
            if (this.eatingTicks >= 36) {
                this.ostrich.getWorld().setBlockState(moundPos, Blocks.MOUND.getDefaultState(), net.minecraft.block.Block.NOTIFY_ALL);
                this.ostrich.playSound(SoundEvents.ENTITY_GENERIC_EAT.value(), 1.0F, 1.0F);
                this.ostrich.heal(1.0F);

                this.hasFinishedEating = true;
                this.eatingTicks = 0;
                this.cooldown = toGoalTicks(800 + this.ostrich.getRandom().nextInt(600)); // 40-70s cooldown
            }
        } else {
            this.eatingTicks = 0;
        }
    }
}
