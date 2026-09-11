package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.math.MathHelper;

public class OstrichEntityModel extends EntityModel<OstrichEntityRenderState> {
    public static final EntityModelLayer OSTRICH = new EntityModelLayer(MASTERmaxisVanillaPlus.id("ostrich"), "main");

    private final ModelPart ostrich;
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public OstrichEntityModel(ModelPart root) {
        super(root, RenderLayer::getEntityCutout);
        this.ostrich = root.getChild("ostrich");
        ModelPart body = this.ostrich.getChild("body");
        this.leftWing = body.getChild("left_wing");
        this.rightWing = body.getChild("right_wing");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData ostrich = modelPartData.addChild("ostrich", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData body = ostrich.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData rest = body.addChild("rest", ModelPartBuilder.create().uv(36, 48).cuboid(-3.0F, -16.0F, 0.0F, 6.0F, 7.0F, 6.0F, new Dilation(0.001F))
                .uv(23, 1).cuboid(-3.0F, -17.0F, -2.0F, 6.0F, 8.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 27).cuboid(-3.0F, -17.0F, -5.0F, 6.0F, 7.0F, 3.0F, new Dilation(0.0F))
                .uv(19, 28).cuboid(-2.0F, -16.0F, -7.0F, 4.0F, 4.0F, 2.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        rest.addChild("top_left_leg", ModelPartBuilder.create().uv(0, 47).cuboid(0.0F, -12.0F, 5.0F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, -4.0F));

        rest.addChild("top_right_leg", ModelPartBuilder.create().uv(12, 47).cuboid(-3.0F, -12.0F, 5.0F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, -4.0F));

        body.addChild("left_wing", ModelPartBuilder.create().uv(0, 12).cuboid(3.0F, -16.0F, -2.0F, 1.0F, 7.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        body.addChild("right_wing", ModelPartBuilder.create().uv(18, 12).cuboid(-4.0F, -16.0F, -2.0F, 1.0F, 7.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ostrich.addChild("tail", ModelPartBuilder.create().uv(32, 27).cuboid(-2.0F, -13.0F, 6.0F, 4.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData neck = ostrich.addChild("neck", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 2.0F, -4.0F));

        ModelPartData head = neck.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        head.addChild("main", ModelPartBuilder.create().uv(30, 35).cuboid(-2.0F, -25.0F, -6.0F, 4.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(36, 11).cuboid(-2.0F, -27.0F, -6.0F, 4.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

        head.addChild("pick_top", ModelPartBuilder.create().uv(40, 3).cuboid(-2.0F, -25.0F, -8.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

        head.addChild("pick_bottom", ModelPartBuilder.create().uv(40, 0).cuboid(-2.0F, -24.0F, -8.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

        neck.addChild("neck_high", ModelPartBuilder.create().uv(40, 40).cuboid(-1.0F, -22.0F, -6.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        neck.addChild("neck_low", ModelPartBuilder.create().uv(6, 37).cuboid(-1.0F, -20.0F, -7.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        neck.addChild("neck_to_body", ModelPartBuilder.create().uv(5, 37).cuboid(-1.0F, -17.0F, -6.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData legs = ostrich.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        legs.addChild("top_left", ModelPartBuilder.create().uv(0, 37).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, -6.0F, 3.0F));

        ModelPartData left = legs.addChild("left", ModelPartBuilder.create().uv(0, 37).cuboid(1.0F, -6.0F, 2.0F, 2.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        left.addChild("foot2", ModelPartBuilder.create().uv(30, 40).cuboid(1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        legs.addChild("top_right", ModelPartBuilder.create().uv(35, 16).cuboid(-5.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, -6.0F, 3.0F));

        ModelPartData right = legs.addChild("right", ModelPartBuilder.create().uv(36, 16).cuboid(-3.0F, -6.0F, 2.0F, 2.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        right.addChild("foot", ModelPartBuilder.create().uv(40, 6).cuboid(-3.0F, -1.0F, -1.0F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(OstrichEntityRenderState ostrichEntityRenderState) {
        super.setAngles(ostrichEntityRenderState);

        this.getPart().traverse().forEach(ModelPart::resetTransform);

        if (ostrichEntityRenderState.getHitAnimationState.isRunning()) {
            this.animate(ostrichEntityRenderState.getHitAnimationState, OstrichEntityAnimations.GET_HIT, ostrichEntityRenderState.age, 1.0F);
        } else if (ostrichEntityRenderState.attackAnimationState.isRunning()) {
            this.animate(ostrichEntityRenderState.attackAnimationState, OstrichEntityAnimations.ATTACK, ostrichEntityRenderState.age, 1.0F);
        } else {
            if (ostrichEntityRenderState.limbAmplitudeMultiplier > 0.01F) {
                this.animateWalking(OstrichEntityAnimations.WALK, ostrichEntityRenderState.limbFrequency, ostrichEntityRenderState.limbAmplitudeMultiplier, 2.0F, 2.5F);
            }
            if (ostrichEntityRenderState.limbAmplitudeMultiplier <= 0.01F && ostrichEntityRenderState.idlingAnimationState.isRunning()) {
                this.animate(ostrichEntityRenderState.idlingAnimationState, OstrichEntityAnimations.IDLE, ostrichEntityRenderState.age, 1.0F);
            }
        }

        if (ostrichEntityRenderState.maxWingDeviation > 0.01F) {
            float flap = (MathHelper.sin(ostrichEntityRenderState.flapProgress) + 1.0F) * ostrichEntityRenderState.maxWingDeviation;
            this.leftWing.pivotX += flap * 1.5F;
            this.leftWing.roll -= flap * 0.25F;
            this.rightWing.pivotX += flap * 1.5F;
            this.rightWing.roll += flap * 0.25F;
        }
    }

    public ModelPart getPart() {
        return this.ostrich;
    }
}
