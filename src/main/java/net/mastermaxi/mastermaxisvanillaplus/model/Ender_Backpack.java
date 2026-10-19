package net.mastermaxi.mastermaxisvanillaplus.model;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class Ender_Backpack extends Model {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(MASTERmaxisVanillaPlus.id("ender_backpack"), "main");
    private final ModelPart backpack;

    public Ender_Backpack(ModelPart root) {
        super(root, RenderLayer::getEntityCutoutNoCull);
        this.backpack = root.getChild("backpack");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData partDefinition = modelData.getRoot();

        partDefinition.addChild("backpack",
                ModelPartBuilder.create()
                        .uv(0, 8).cuboid(-3.0F, -12.0F, 2.0F, 6.0F, 12.0F, 3.0F, new Dilation(0.0F))
                        .uv(0, 24).cuboid(-3.0F, -6.0F, 5.0F, 6.0F, 6.0F, 2.0F, new Dilation(0.0F))
                        .uv(22, 14).cuboid(3.0F, -6.0F, 2.0F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
                        .uv(22, 23).cuboid(-5.0F, -6.0F, 2.0F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 11.0F, 0.0F)
        );

        return TexturedModelData.of(modelData, 32, 32);
    }

    public ModelPart getBackpack() {
        return this.backpack;
    }
}