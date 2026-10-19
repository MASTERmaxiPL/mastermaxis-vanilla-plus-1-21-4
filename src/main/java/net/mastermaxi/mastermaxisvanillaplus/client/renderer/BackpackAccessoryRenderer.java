package net.mastermaxi.mastermaxisvanillaplus.client.renderer;

import io.wispforest.accessories.api.client.AccessoryRenderer;
import io.wispforest.accessories.api.slot.SlotReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.entity.client.BackpackEntityRenderer;
import net.mastermaxi.mastermaxisvanillaplus.item.BackpackItem;
import net.mastermaxi.mastermaxisvanillaplus.item.QuiverItem;
import net.mastermaxi.mastermaxisvanillaplus.model.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

@Environment(EnvType.CLIENT)
public class BackpackAccessoryRenderer implements AccessoryRenderer {

    private Leather_Backpack leatherModel;
    private Iron_Netherite_Backpack ironNetheriteModel;
    private Backpack_With_Quiver ironQuiverModel;
    private Backpack_With_Quiver_Arrows ironQuiverArrowsModel;
    private Ender_Backpack enderModel;
    private QuiverModel quiverModel;

    private void initModels() {
        if (leatherModel == null) {
            LoadedEntityModels models = MinecraftClient.getInstance().getLoadedEntityModels();
            leatherModel = new Leather_Backpack(models.getModelPart(Leather_Backpack.LAYER_LOCATION));
            ironNetheriteModel = new Iron_Netherite_Backpack(models.getModelPart(Iron_Netherite_Backpack.LAYER_LOCATION));
            ironQuiverModel = new Backpack_With_Quiver(models.getModelPart(Backpack_With_Quiver.LAYER_LOCATION));
            ironQuiverArrowsModel = new Backpack_With_Quiver_Arrows(models.getModelPart(Backpack_With_Quiver_Arrows.LAYER_LOCATION));
            enderModel = new Ender_Backpack(models.getModelPart(Ender_Backpack.LAYER_LOCATION));
            quiverModel = new QuiverModel(models.getModelPart(QuiverModel.LAYER_LOCATION));
        }
    }

    @Override
    public <S extends LivingEntityRenderState> void render(
            ItemStack stack,
            SlotReference reference,
            MatrixStack matrices,
            EntityModel<S> model,
            S state,
            VertexConsumerProvider vertexConsumers,
            int light,
            float tickDelta
    ) {
        if (stack.isEmpty()) return;
        if (!(model instanceof BipedEntityModel<?> bipedModel)) return;

        initModels();

        matrices.push();
        bipedModel.body.rotate(matrices);

        if (stack.getItem() instanceof BackpackItem backpackItem) {
            switch (backpackItem.getTier()) {
                case LEATHER -> {
                    VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(BackpackEntityRenderer.LEATHER_TEXTURE));
                    this.leatherModel.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);
                }
                case IRON -> {
                    VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(BackpackEntityRenderer.IRON_TEXTURE));
                    this.ironNetheriteModel.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);
                }
                case IRON_WITH_QUIVER -> {
                    VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(BackpackEntityRenderer.IRON_QUIVER_TEXTURE));
                    this.ironQuiverModel.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);

                    DefaultedList<ItemStack> arrows = backpackItem.getStoredQuiverArrows(stack);
                    if (arrows.stream().anyMatch(s -> !s.isEmpty())) {
                        VertexConsumer arrowVc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(BackpackEntityRenderer.BACKPACK_ARROW_TEXTURE));
                        this.ironQuiverArrowsModel.render(matrices, arrowVc, light, OverlayTexture.DEFAULT_UV);
                    }
                }
                case NETHERITE -> {
                    VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(BackpackEntityRenderer.NETHERITE_TEXTURE));
                    this.ironNetheriteModel.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);
                }
                case ENDER -> {
                    VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(BackpackEntityRenderer.ENDER_TEXTURE));
                    this.enderModel.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);
                }
            }
        } else if (stack.getItem() instanceof QuiverItem) {
            boolean hasArrows = QuiverItem.getTotalArrows(stack) > 0;
            Identifier texture = hasArrows ? BackpackEntityRenderer.QUIVER_ARROWS_TEXTURE : BackpackEntityRenderer.QUIVER_EMPTY_TEXTURE;
            this.quiverModel.setArrowsVisible(hasArrows);
            VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(texture));
            this.quiverModel.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);
        }

        matrices.pop();
    }
}
