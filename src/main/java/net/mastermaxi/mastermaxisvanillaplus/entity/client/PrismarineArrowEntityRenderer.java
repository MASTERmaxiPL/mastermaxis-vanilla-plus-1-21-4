package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.entity.PrismarineArrowEntity;
import net.mastermaxi.mastermaxisvanillaplus.entity.PrismarineHeavyArrowEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class PrismarineArrowEntityRenderer<T extends PrismarineArrowEntity> extends ProjectileEntityRenderer<T, PrismarineArrowRenderState> {
    public static final Identifier TEXTURE = MASTERmaxisVanillaPlus.id("textures/entity/projectiles/prismarine_arrow.png");
    public static final Identifier HEAVY_TEXTURE = MASTERmaxisVanillaPlus.id("textures/entity/projectiles/prismarine_heavy_arrow.png");

    private final PrismarineArrowEntityModel customModel;

    public PrismarineArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.customModel = new PrismarineArrowEntityModel(context.getPart(PrismarineArrowEntityModel.LAYER_LOCATION));
    }

    @Override
    public PrismarineArrowRenderState createRenderState() {
        return new PrismarineArrowRenderState();
    }

    @Override
    public void updateRenderState(T entity, PrismarineArrowRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.tipped = entity.getColor() > 0;
        state.heavy = entity instanceof PrismarineHeavyArrowEntity;
    }

    @Override
    protected Identifier getTexture(PrismarineArrowRenderState state) {
        return state.heavy ? HEAVY_TEXTURE : TEXTURE;
    }

    @Override
    public void render(PrismarineArrowRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yaw - 90.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(state.pitch));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(this.getTexture(state)));
        this.customModel.setAngles(state);
        this.customModel.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }
}
