package net.mastermaxi.mastermaxisvanillaplus.entity.ai;

import net.mastermaxi.mastermaxisvanillaplus.entity.custom.MuddyPigAccess;
import net.mastermaxi.mastermaxisvanillaplus.fluid.FluidInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import net.minecraft.fluid.FluidState;

public class PigWanderToMudGoal extends MoveToTargetPosGoal {
    private final PigEntity pig;

    public PigWanderToMudGoal(PigEntity pig, double speed) {
        super(pig, speed, 16, 6);
        this.pig = pig;
    }

    @Override
    public boolean canStart() {
        MuddyPigAccess access = (MuddyPigAccess) this.pig;
        if (access.mastermaxisvanillaplus$getMudVariant() != 0 && access.mastermaxisvanillaplus$getHappiness() >= 80) {
            return false;
        }
        if (access.mastermaxisvanillaplus$isDigging() || access.mastermaxisvanillaplus$isWallowing()) {
            return false;
        }
        return super.canStart();
    }

    @Override
    public boolean shouldContinue() {
        MuddyPigAccess access = (MuddyPigAccess) this.pig;
        if (access.mastermaxisvanillaplus$isWallowing()) {
            return false;
        }
        return super.shouldContinue();
    }

    @Override
    protected int getInterval(PathAwareEntity mob) {
        return toGoalTicks(160 + mob.getRandom().nextInt(160));
    }

    @Override
    public double getDesiredDistanceToTarget() {
        return 0.5;
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        FluidState fluid = world.getFluidState(pos);
        if (fluid.getFluid() == FluidInit.MUD_WATER_STILL || fluid.getFluid() == FluidInit.MUD_WATER_FLOWING) {
            return true;
        }
        BlockState state = world.getBlockState(pos);
        return state.isOf(FluidInit.MUD_WATER_BLOCK);
    }
}
