package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.BackpackEntity;
import net.mastermaxi.mastermaxisvanillaplus.item.BackpackItem;
import net.mastermaxi.mastermaxisvanillaplus.item.QuiverItem;
import net.mastermaxi.mastermaxisvanillaplus.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class BackpackEntityRenderer extends EntityRenderer<BackpackEntity, BackpackEntityRenderState> {

    public static final Identifier LEATHER_TEXTURE = MASTERmaxisVanillaPlus.id("textures/entity/backpacks/leather_backpack_texture.png");
    public static final Identifier IRON_TEXTURE = MASTERmaxisVanillaPlus.id("textures/entity/backpacks/iron_backpack_texture.png");
    public static final Identifier IRON_QUIVER_TEXTURE = MASTERmaxisVanillaPlus.id("textures/entity/backpacks/backpack_with_quiver_texture.png");
    public static final Identifier BACKPACK_ARROW_TEXTURE = MASTERmaxisVanillaPlus.id("textures/entity/backpacks/backpack_arrow_texture.png");
    public static final Identifier NETHERITE_TEXTURE = MASTERmaxisVanillaPlus.id("textures/entity/backpacks/netherite_backpack_texture.png");
    public static final Identifier ENDER_TEXTURE = MASTERmaxisVanillaPlus.id("textures/entity/backpacks/ender_backpack_texture.png");

    public static final Identifier QUIVER_EMPTY_TEXTURE = MASTERmaxisVanillaPlus.id("textures/entity/quiver/quiver_empty_texture.png");
    public static final Identifier QUIVER_ARROWS_TEXTURE = MASTERmaxisVanillaPlus.id("textures/entity/quiver/quiver_with_arrows_texture.png");

    private final Leather_Backpack leatherModel;
    private final Iron_Netherite_Backpack ironNetheriteModel;
    private final Backpack_With_Quiver ironQuiverModel;
    private final Backpack_With_Quiver_Arrows ironQuiverArrowsModel;
    private final Ender_Backpack enderModel;
    private final QuiverModel quiverModel;

    public BackpackEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.leatherModel = new Leather_Backpack(ctx.getPart(Leather_Backpack.LAYER_LOCATION));
        this.ironNetheriteModel = new Iron_Netherite_Backpack(ctx.getPart(Iron_Netherite_Backpack.LAYER_LOCATION));
        this.ironQuiverModel = new Backpack_With_Quiver(ctx.getPart(Backpack_With_Quiver.LAYER_LOCATION));
        this.ironQuiverArrowsModel = new Backpack_With_Quiver_Arrows(ctx.getPart(Backpack_With_Quiver_Arrows.LAYER_LOCATION));
        this.enderModel = new Ender_Backpack(ctx.getPart(Ender_Backpack.LAYER_LOCATION));
        this.quiverModel = new QuiverModel(ctx.getPart(QuiverModel.LAYER_LOCATION));
    }

    @Override
    public BackpackEntityRenderState createRenderState() {
        return new BackpackEntityRenderState();
    }

    @Override
    public void updateRenderState(BackpackEntity entity, BackpackEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        ItemStack stack = entity.getBackpackStack();
        state.backpackStack = stack;
        state.yaw = entity.getYaw();

        if (stack.getItem() instanceof BackpackItem bp && bp.getTier().hasQuiver) {
            DefaultedList<ItemStack> arrows = bp.getStoredQuiverArrows(stack);
            state.hasArrows = arrows.stream().anyMatch(s -> !s.isEmpty());
        } else if (stack.getItem() instanceof QuiverItem) {
            state.hasArrows = QuiverItem.getTotalArrows(stack) > 0;
        } else {
            state.hasArrows = false;
        }
    }

    @Override
    public void render(BackpackEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        ItemStack stack = state.backpackStack;
        if (stack.isEmpty()) return;

        matrices.push();
        // Place on block and orient
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - state.yaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
        matrices.translate(0.0, -0.75, 0.0);

        if (stack.getItem() instanceof BackpackItem backpackItem) {
            switch (backpackItem.getTier()) {
                case LEATHER -> {
                    VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(LEATHER_TEXTURE));
                    this.leatherModel.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);
                }
                case IRON -> {
                    VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(IRON_TEXTURE));
                    this.ironNetheriteModel.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);
                }
                case IRON_WITH_QUIVER -> {
                    VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(IRON_QUIVER_TEXTURE));
                    this.ironQuiverModel.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);
                    if (state.hasArrows) {
                        VertexConsumer arrowVc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(BACKPACK_ARROW_TEXTURE));
                        this.ironQuiverArrowsModel.render(matrices, arrowVc, light, OverlayTexture.DEFAULT_UV);
                    }
                }
                case NETHERITE -> {
                    VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(NETHERITE_TEXTURE));
                    this.ironNetheriteModel.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);
                }
                case ENDER -> {
                    VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(ENDER_TEXTURE));
                    this.enderModel.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);
                }
            }
        } else if (stack.getItem() instanceof QuiverItem) {
            Identifier texture = state.hasArrows ? QUIVER_ARROWS_TEXTURE : QUIVER_EMPTY_TEXTURE;
            this.quiverModel.setArrowsVisible(state.hasArrows);
            VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(texture));
            this.quiverModel.render(matrices, vc, light, OverlayTexture.DEFAULT_UV);
        }

        matrices.pop();
        super.render(state, matrices, vertexConsumers, light);
    }
}
