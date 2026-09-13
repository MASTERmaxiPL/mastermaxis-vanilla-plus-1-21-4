package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.fluid.MudWaterFluid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "travelInFluid", at = @At("HEAD"), argsOnly = true)
    private Vec3d modifyMudWaterMovementInput(Vec3d movementInput) {
        if (mastermaxisvanillaplus$isInMudWater()) {
            return movementInput.multiply(0.8, 0.85, 0.8);
        }
        return movementInput;
    }

    @Inject(method = "travelInFluid", at = @At("TAIL"))
    private void modifyMudWaterVelocity(Vec3d movementInput, CallbackInfo ci) {
        if (mastermaxisvanillaplus$isInMudWater()) {
            Vec3d vel = this.getVelocity();
            this.setVelocity(vel.x * 0.96, vel.y * 0.98, vel.z * 0.96);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void mastermaxisvanillaplus$onTick(CallbackInfo ci) {
        if ((Object) this instanceof net.mastermaxi.mastermaxisvanillaplus.entity.custom.MuddyPigAccess muddyPig) {
            muddyPig.mastermaxisvanillaplus$tickPig();
        }
    }

    @Unique
    private boolean mastermaxisvanillaplus$isInMudWater() {
        if (!this.isTouchingWater()) {
            return false;
        }
        BlockPos pos = this.getBlockPos();
        if (this.getWorld().getFluidState(pos).getFluid() instanceof MudWaterFluid) {
            return true;
        }
        BlockPos eyePos = BlockPos.ofFloored(this.getX(), this.getEyeY(), this.getZ());
        if (this.getWorld().getFluidState(eyePos).getFluid() instanceof MudWaterFluid) {
            return true;
        }
        Box box = this.getBoundingBox().contract(0.001);
        int minX = MathHelper.floor(box.minX);
        int maxX = MathHelper.ceil(box.maxX);
        int minY = MathHelper.floor(box.minY);
        int maxY = MathHelper.ceil(box.maxY);
        int minZ = MathHelper.floor(box.minZ);
        int maxZ = MathHelper.ceil(box.maxZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    mutable.set(x, y, z);
                    FluidState state = this.getWorld().getFluidState(mutable);
                    if (state.getFluid() instanceof MudWaterFluid) {
                        double surfaceY = y + state.getHeight(this.getWorld(), mutable);
                        if (surfaceY >= box.minY) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
