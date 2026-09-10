package net.mastermaxi.mastermaxisvanillaplus.block.client;

import net.mastermaxi.mastermaxisvanillaplus.block.InfectedWoodBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;

public class InfectedWoodBlockEntityRenderer implements BlockEntityRenderer<InfectedWoodBlockEntity> {
    private final BlockRenderManager blockRenderManager;

    public InfectedWoodBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = ctx.getRenderManager();
    }

    @Override
    public void render(InfectedWoodBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        // 1. Render the original wood / planks block as entity geometry
        BlockState originalState = entity.getOriginalState();
        if (originalState != null && !originalState.isAir()) {
            this.blockRenderManager.renderBlockAsEntity(originalState, matrices, vertexConsumers, light, overlay);
        }

        // 2. Render the animated termite overlay corresponding to the block's ANIMATION variant (0-3)
        BlockState infectedState = entity.getCachedState();
        BakedModel overlayModel = this.blockRenderManager.getModel(infectedState);
        if (overlayModel != null) {
            this.blockRenderManager.getModelRenderer().render(
                    matrices.peek(),
                    vertexConsumers.getBuffer(RenderLayer.getCutout()),
                    infectedState,
                    overlayModel,
                    1.0F, 1.0F, 1.0F,
                    light,
                    overlay
            );
        }
    }
}
