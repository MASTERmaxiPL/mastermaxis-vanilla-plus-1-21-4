package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.OstrichEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class OstrichEntityRenderer extends MobEntityRenderer<OstrichEntity, OstrichEntityRenderState, OstrichEntityModel> {
    private static final Identifier TEXTURE = Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "textures/entity/ostrich/ostrich.png");

    public OstrichEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new OstrichEntityModel(ctx.getPart(OstrichEntityModel.OSTRICH)), 0.7F);
    }

    @Override
    public Identifier getTexture(OstrichEntityRenderState state) {
        return TEXTURE;
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
        state.flapProgress = MathHelper.lerp(f, entity.prevFlapProgress, entity.flapProgress);
        state.maxWingDeviation = MathHelper.lerp(f, entity.prevMaxWingDeviation, entity.maxWingDeviation);
    }

    @Override
    protected void scale(OstrichEntityRenderState state, MatrixStack matrixStack) {
        super.scale(state, matrixStack);
        if (state.baby) {
            matrixStack.scale(0.5F, 0.5F, 0.5F);
        }
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
