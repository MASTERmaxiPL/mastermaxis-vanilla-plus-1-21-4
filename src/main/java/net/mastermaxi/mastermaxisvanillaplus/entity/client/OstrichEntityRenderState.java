package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.AnimationState;

public class OstrichEntityRenderState extends LivingEntityRenderState {
    public final AnimationState idlingAnimationState = new AnimationState();
    public final AnimationState getHitAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public float flapProgress;
    public float maxWingDeviation;
    public boolean isSaddled;
    public boolean isAlt;
    public int idleAnimationId;
}
