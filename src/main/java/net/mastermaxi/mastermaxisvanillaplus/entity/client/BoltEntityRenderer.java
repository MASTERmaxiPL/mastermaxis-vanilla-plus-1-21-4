package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.entity.BoltEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ArrowEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class BoltEntityRenderer extends ProjectileEntityRenderer<BoltEntity, ArrowEntityRenderState> {
    public static final Identifier TEXTURE = MASTERmaxisVanillaPlus.id("textures/entity/projectiles/bolt.png");
    public static final Identifier TIPPED_TEXTURE = MASTERmaxisVanillaPlus.id("textures/entity/projectiles/bolt_tipped.png");

    private final BoltEntityModel customModel;

    public BoltEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.customModel = new BoltEntityModel(context.getPart(BoltEntityModel.LAYER_LOCATION));
    }

    @Override
    public ArrowEntityRenderState createRenderState() {
        return new ArrowEntityRenderState();
    }

    @Override
    public void updateRenderState(BoltEntity entity, ArrowEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.tipped = entity.getColor() > 0;
    }

    @Override
    protected Identifier getTexture(ArrowEntityRenderState state) {
        return state.tipped ? TIPPED_TEXTURE : TEXTURE;
    }

    @Override
    public void render(ArrowEntityRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yaw - 90.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(state.pitch));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(this.getTexture(state)));
        this.customModel.setAngles(state);
        this.customModel.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }
}
