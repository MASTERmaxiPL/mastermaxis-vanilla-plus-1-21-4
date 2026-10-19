package net.mastermaxi.mastermaxisvanillaplus.model;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class Backpack_With_Quiver extends Model {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(MASTERmaxisVanillaPlus.id("backpack_with_quiver"), "main");
    private final ModelPart backpack;

    public Backpack_With_Quiver(ModelPart root) {
        super(root, RenderLayer::getEntityCutoutNoCull);
        this.backpack = root.getChild("backpack");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData partDefinition = modelData.getRoot();

        partDefinition.addChild("backpack",
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-4.0F, -11.0F, 2.0F, 6.0F, 10.0F, 2.0F, new Dilation(0.0F))
                        .uv(0, 12).cuboid(-4.0F, -7.0F, 4.0F, 6.0F, 6.0F, 2.0F, new Dilation(0.0F))
                        .uv(16, 0).cuboid(2.0F, -10.0F, 2.0F, 2.0F, 9.0F, 3.0F, new Dilation(0.0F))
                        .uv(16, 12).cuboid(-6.0F, -6.0F, 2.0F, 2.0F, 5.0F, 3.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 11.0F, 0.0F)
        );

        return TexturedModelData.of(modelData, 32, 32);
    }

    public ModelPart getBackpack() {
        return this.backpack;
    }
}
