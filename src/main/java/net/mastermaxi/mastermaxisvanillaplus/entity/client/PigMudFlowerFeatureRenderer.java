package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.client.render.entity.state.PigEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PigMudFlowerFeatureRenderer extends FeatureRenderer<PigEntityRenderState, PigEntityModel> {
    private static final Identifier TEXTURE = Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "textures/entity/pig/pig_in_mud_top.png");
    private final PigMuddedTopEntityModel flowerModel;

    public PigMudFlowerFeatureRenderer(FeatureRendererContext<PigEntityRenderState, PigEntityModel> context, ModelPart flowerPart) {
        super(context);
        this.flowerModel = new PigMuddedTopEntityModel(flowerPart);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PigEntityRenderState state, float limbAngle, float limbDistance) {
        MuddyPigRenderStateAccess access = (MuddyPigRenderStateAccess) state;
        if (access.mastermaxisvanillaplus$getMudVariant() == 2) {
            this.flowerModel.setAngles(state);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
            this.flowerModel.renderFlower(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        }
    }
}
