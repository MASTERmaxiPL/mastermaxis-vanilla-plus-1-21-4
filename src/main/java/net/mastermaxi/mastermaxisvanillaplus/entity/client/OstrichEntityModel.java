package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class OstrichEntityModel extends EntityModel<OstrichEntityRenderState> {
    public static final EntityModelLayer OSTRICH = new EntityModelLayer(MASTERmaxisVanillaPlus.id("ostrich"), "main");

    private final ModelPart ostrich;
    private final ModelPart head;

    public OstrichEntityModel(ModelPart root) {
        super(root, RenderLayer::getEntityCutout);
        this.ostrich = root.getChild("ostrich");
        ModelPart neck = this.ostrich.getChild("neck");
        this.head = neck.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData ostrich = modelPartData.addChild("ostrich", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData body = ostrich.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData rest = body.addChild("rest", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -16.0F, 1.0F, 6.0F, 7.0F, 5.0F, new Dilation(0.001F))
                .uv(22, 0).cuboid(-3.0F, -17.0F, -2.0F, 6.0F, 8.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 27).cuboid(-3.0F, -17.0F, -5.0F, 6.0F, 7.0F, 3.0F, new Dilation(0.0F))
                .uv(20, 29).cuboid(-2.0F, -17.0F, -6.0F, 4.0F, 3.0F, 1.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData left_wing = body.addChild("left_wing", ModelPartBuilder.create().uv(0, 12).cuboid(3.0F, -16.0F, -2.0F, 1.0F, 7.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData right_wing = body.addChild("right_wing", ModelPartBuilder.create().uv(18, 12).cuboid(-4.0F, -16.0F, -2.0F, 1.0F, 7.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData tail = ostrich.addChild("tail", ModelPartBuilder.create().uv(32, 27).cuboid(-2.0F, -14.0F, 6.0F, 4.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData neck = ostrich.addChild("neck", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData head = neck.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData main = head.addChild("main", ModelPartBuilder.create().uv(30, 35).cuboid(-2.0F, -25.0F, -6.0F, 4.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(36, 11).cuboid(-2.0F, -27.0F, -6.0F, 4.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData pick_top = head.addChild("pick_top", ModelPartBuilder.create().uv(40, 3).cuboid(-2.0F, -25.0F, -8.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData pick_bottom = head.addChild("pick_bottom", ModelPartBuilder.create().uv(40, 0).cuboid(-2.0F, -24.0F, -8.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData neck_high = neck.addChild("neck_high", ModelPartBuilder.create().uv(40, 40).cuboid(-1.0F, -23.0F, -6.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData neck_low = neck.addChild("neck_low", ModelPartBuilder.create().uv(6, 37).cuboid(-1.0F, -20.0F, -7.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData legs = ostrich.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData left = legs.addChild("left", ModelPartBuilder.create().uv(0, 37).cuboid(1.0F, -9.0F, 2.0F, 2.0F, 9.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData foot2 = left.addChild("foot2", ModelPartBuilder.create().uv(30, 40).cuboid(1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData right = legs.addChild("right", ModelPartBuilder.create().uv(36, 16).cuboid(-3.0F, -9.0F, 2.0F, 2.0F, 9.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData foot = right.addChild("foot", ModelPartBuilder.create().uv(40, 6).cuboid(-3.0F, -1.0F, -1.0F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(OstrichEntityRenderState ostrichEntityRenderState) {
        super.setAngles(ostrichEntityRenderState);

        this.getPart().traverse().forEach(ModelPart::resetTransform);

        this.animate(ostrichEntityRenderState.getHitAnimationState, OstrichEntityAnimations.GET_HIT, ostrichEntityRenderState.age, 1.0F);
        this.animate(ostrichEntityRenderState.idlingAnimationState, OstrichEntityAnimations.IDLE, ostrichEntityRenderState.age, 1.0F);
        this.animateWalking(OstrichEntityAnimations.WALK, 1, 1, 2f, 2.5f);
    }

    public ModelPart getPart() {
        return this.ostrich;
    }
}
