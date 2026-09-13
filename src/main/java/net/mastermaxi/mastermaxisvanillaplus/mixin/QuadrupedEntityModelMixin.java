package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.entity.client.MuddyPigRenderStateAccess;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(QuadrupedEntityModel.class)
public abstract class QuadrupedEntityModelMixin {
    @Final
    @Shadow
    protected ModelPart head;
    @Final
    @Shadow
    protected ModelPart body;
    @Final
    @Shadow
    protected ModelPart rightHindLeg;
    @Final
    @Shadow
    protected ModelPart leftHindLeg;
    @Final
    @Shadow
    protected ModelPart rightFrontLeg;
    @Final
    @Shadow
    protected ModelPart leftFrontLeg;

    @Inject(method = "setAngles(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;)V", at = @At("TAIL"))
    private void mastermaxisvanillaplus$animateMuddyPig(LivingEntityRenderState state, CallbackInfo ci) {
        if (state instanceof MuddyPigRenderStateAccess access) {
            if (access.mastermaxisvanillaplus$isWallowing()) {
                float time = state.age;
                float roll = MathHelper.sin(time * 0.25F) * 0.85F;
                float sink = 3.5F;

                this.body.pivotY += sink;
                this.head.pivotY += sink;
                this.rightHindLeg.pivotY += sink;
                this.leftHindLeg.pivotY += sink;
                this.rightFrontLeg.pivotY += sink;
                this.leftFrontLeg.pivotY += sink;

                this.body.roll = roll;
                this.head.roll = roll * 0.85F;
                this.rightHindLeg.roll = roll;
                this.leftHindLeg.roll = roll;
                this.rightFrontLeg.roll = roll;
                this.leftFrontLeg.roll = roll;

                this.rightHindLeg.pitch = MathHelper.cos(time * 0.6F) * 0.7F;
                this.leftHindLeg.pitch = MathHelper.sin(time * 0.6F) * 0.7F;
                this.rightFrontLeg.pitch = MathHelper.sin(time * 0.6F) * 0.7F;
                this.leftFrontLeg.pitch = MathHelper.cos(time * 0.6F) * 0.7F;

                this.head.pitch = 0.2F + MathHelper.sin(time * 0.5F) * 0.15F;
                this.head.yaw = MathHelper.cos(time * 0.25F) * 0.3F;
            } else if (access.mastermaxisvanillaplus$isDigging()) {
                float time = state.age;
                this.head.pitch = 0.85F + MathHelper.sin(time * 0.7F) * 0.12F;
                this.head.yaw = MathHelper.sin(time * 0.9F) * 0.25F;
                this.rightFrontLeg.pitch = MathHelper.sin(time * 0.8F) * 0.5F;
                this.leftFrontLeg.pitch = MathHelper.cos(time * 0.8F) * 0.5F;
            }
        }
    }
}
