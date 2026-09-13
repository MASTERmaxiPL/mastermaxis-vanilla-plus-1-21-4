package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.OstrichEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class OstrichEntityRenderer extends MobEntityRenderer<OstrichEntity, OstrichEntityRenderState, EntityModel<OstrichEntityRenderState>> {
    private static final Identifier TEXTURE = Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "textures/entity/ostrich/ostrich.png");
    private static final Identifier SADDLED_TEXTURE = Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "textures/entity/ostrich/tammed_ostrich.png");
    private static final Identifier BABY_TEXTURE = Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "textures/entity/ostrich/baby_ostrich.png");
    private static final Identifier ALT_TEXTURE = Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "textures/entity/ostrich/ostrich_alt.png");
    private static final Identifier ALT_SADDLED_TEXTURE = Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "textures/entity/ostrich/ostrich_alt_tammed.png");

    private final OstrichEntityModel adultModel;
    private final BabyOstrichEntityModel babyModel;

    public OstrichEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new OstrichEntityModel(ctx.getPart(OstrichEntityModel.OSTRICH)), 0.7F);
        this.adultModel = (OstrichEntityModel) this.model;
        this.babyModel = new BabyOstrichEntityModel(ctx.getPart(BabyOstrichEntityModel.BABY_OSTRICH));
    }

    @Override
    public void render(OstrichEntityRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.model = state.baby ? this.babyModel : this.adultModel;
        super.render(state, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(OstrichEntityRenderState state) {
        if (state.baby) {
            return BABY_TEXTURE;
        }
        if (state.isAlt) {
            return state.isSaddled ? ALT_SADDLED_TEXTURE : ALT_TEXTURE;
        }
        return state.isSaddled ? SADDLED_TEXTURE : TEXTURE;
    }

    @Override
    public OstrichEntityRenderState createRenderState() {
        return new OstrichEntityRenderState();
    }

    @Override
    public void updateRenderState(OstrichEntity entity, OstrichEntityRenderState state, float f) {
        super.updateRenderState(entity, state, f);
        state.idlingAnimationState.copyFrom(entity.idlingAnimationState);
        state.getHitAnimationState.copyFrom(entity.getHitAnimationState);
        state.attackAnimationState.copyFrom(entity.attackAnimationState);
        state.isSaddled = entity.isSaddled();
        state.isAlt = entity.isRoadOstrich();
        state.idleAnimationId = entity.idleAnimationId;
        state.flapProgress = MathHelper.lerp(f, entity.prevFlapProgress, entity.flapProgress);
        state.maxWingDeviation = MathHelper.lerp(f, entity.prevMaxWingDeviation, entity.maxWingDeviation);
    }

    @Override
    public float getShadowRadius(OstrichEntityRenderState state) {
        float r = super.getShadowRadius(state);
        return state.baby ? r * 0.5F : r;
    }

    protected void setupTransforms(OstrichEntityRenderState state, MatrixStack matrixStack, float f, float g) {
        super.setupTransforms(state, matrixStack, f, g);
        if (!((double)state.limbAmplitudeMultiplier < 0.01)) {
            float i = state.limbFrequency + 6.0F;
            float j = (Math.abs(i % 13.0F - 6.5F) - 3.25F) / 3.25F;
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(6.5F * j));
        }
    }
}
