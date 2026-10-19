package net.mastermaxi.mastermaxisvanillaplus.model;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class Iron_Netherite_Backpack extends Model {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(MASTERmaxisVanillaPlus.id("iron_netherite_backpack"), "main");
    private final ModelPart backpack;

    public Iron_Netherite_Backpack(ModelPart root) {
        super(root, RenderLayer::getEntityCutoutNoCull);
        this.backpack = root.getChild("backpack");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData partDefinition = modelData.getRoot();

        partDefinition.addChild("backpack",
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-3.0F, -12.0F, 2.0F, 6.0F, 12.0F, 2.0F, new Dilation(0.0F))
                        .uv(0, 14).cuboid(-3.0F, -7.0F, 4.0F, 6.0F, 7.0F, 2.0F, new Dilation(0.0F))
                        .uv(16, 0).cuboid(3.0F, -6.0F, 2.0F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
                        .uv(16, 9).cuboid(-5.0F, -6.0F, 2.0F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 12.0F, 0.0F)
        );

        return TexturedModelData.of(modelData, 32, 32);
    }

    public ModelPart getBackpack() {
        return this.backpack;
    }
}