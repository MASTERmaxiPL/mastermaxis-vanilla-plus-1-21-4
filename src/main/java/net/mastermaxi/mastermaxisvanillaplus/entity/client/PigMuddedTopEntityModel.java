package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class PigMuddedTopEntityModel extends PigEntityModel {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(MASTERmaxisVanillaPlus.id("pig_mudded_top"), "main");
    private final ModelPart flower;

    public PigMuddedTopEntityModel(ModelPart root) {
        super(root);
        this.flower = this.head.getChild("flower");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        root.addChild("body", ModelPartBuilder.create().uv(28, 8).cuboid(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 8.0F), ModelTransform.of(0.0F, 11.0F, 2.0F, 1.5708F, 0.0F, 0.0F));
        ModelPartData head = root.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F)
                .uv(16, 16).cuboid(-2.0F, 0.0F, -9.0F, 4.0F, 3.0F, 1.0F), ModelTransform.pivot(0.0F, 12.0F, -6.0F));
        head.addChild("flower", ModelPartBuilder.create().uv(0, 2).cuboid(-4.0F, -10.0F, -5.0F, 4.0F, 6.0F, 0.0F), ModelTransform.NONE);
        root.addChild("right_hind_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F), ModelTransform.pivot(-3.0F, 18.0F, 7.0F));
        root.addChild("left_hind_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F), ModelTransform.pivot(3.0F, 18.0F, 7.0F));
        root.addChild("right_front_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F), ModelTransform.pivot(-3.0F, 18.0F, -5.0F));
        root.addChild("left_front_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F), ModelTransform.pivot(3.0F, 18.0F, -5.0F));

        return TexturedModelData.of(modelData, 64, 32);
    }

    public void renderFlower(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay) {
        matrices.push();
        this.head.rotate(matrices);
        this.flower.render(matrices, vertexConsumer, light, overlay);
        matrices.pop();
    }
}
