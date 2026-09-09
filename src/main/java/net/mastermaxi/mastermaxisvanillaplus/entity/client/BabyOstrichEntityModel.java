package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;

@Environment(EnvType.CLIENT)
public class BabyOstrichEntityModel extends EntityModel<OstrichEntityRenderState> {
    public static final EntityModelLayer BABY_OSTRICH = new EntityModelLayer(MASTERmaxisVanillaPlus.id("baby_ostrich"), "main");

    private final ModelPart babyOstrich;

    public BabyOstrichEntityModel(ModelPart root) {
        super(root, RenderLayer::getEntityCutout);
        this.babyOstrich = root.getChild("baby_ostrich");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData babyOstrich = modelPartData.addChild("baby_ostrich", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        babyOstrich.addChild("peek", ModelPartBuilder.create().uv(12, 11).cuboid(-1.0F, -8.0F, -4.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        babyOstrich.addChild("head", ModelPartBuilder.create().uv(0, 7).cuboid(-1.0F, -9.0F, -3.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        babyOstrich.addChild("neck", ModelPartBuilder.create().uv(8, 7).cuboid(-1.0F, -7.0F, -2.0F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        babyOstrich.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -6.0F, -1.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        babyOstrich.addChild("leg_left", ModelPartBuilder.create().uv(0, 13).cuboid(1.0F, -3.0F, 1.0F, 1.0F, 3.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 11).cuboid(1.0F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        babyOstrich.addChild("leg_right", ModelPartBuilder.create().uv(2, 13).cuboid(-2.0F, -3.0F, 1.0F, 1.0F, 3.0F, 0.0F, new Dilation(0.0F))
                .uv(6, 11).cuboid(-2.0F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void setAngles(OstrichEntityRenderState state) {
        super.setAngles(state);

        this.babyOstrich.traverse().forEach(ModelPart::resetTransform);

        if (state.getHitAnimationState.isRunning()) {
            this.animate(state.getHitAnimationState, BabyOstrichEntityAnimations.GET_HIT, state.age, 1.0F);
        } else {
            if (state.limbAmplitudeMultiplier > 0.01F) {
                this.animateWalking(BabyOstrichEntityAnimations.WALK, state.limbFrequency, state.limbAmplitudeMultiplier, 2.0F, 2.5F);
            }
            if (state.limbAmplitudeMultiplier <= 0.01F && state.idlingAnimationState.isRunning()) {
                Animation idleAnim = switch (state.idleAnimationId) {
                    case 1 -> BabyOstrichEntityAnimations.IDLE2;
                    case 2 -> BabyOstrichEntityAnimations.IDLE3;
                    case 3 -> BabyOstrichEntityAnimations.IDLE4;
                    default -> BabyOstrichEntityAnimations.IDLE;
                };
                this.animate(state.idlingAnimationState, idleAnim, state.age, 1.0F);
            }
        }
    }
}
