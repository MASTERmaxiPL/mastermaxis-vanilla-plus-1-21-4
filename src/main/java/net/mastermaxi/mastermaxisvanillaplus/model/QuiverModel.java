package net.mastermaxi.mastermaxisvanillaplus.model;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class QuiverModel extends Model {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(MASTERmaxisVanillaPlus.id("quiver"), "main");
    private final ModelPart quiver;
    private final ModelPart arrows;

    public QuiverModel(ModelPart root) {
        super(root, RenderLayer::getEntityCutoutNoCull);
        this.quiver = root.getChild("quiver");
        this.arrows = this.quiver.getChild("arrows");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // Quiver base angled -30 degrees (-0.5235987756f radians) on Z axis
        ModelPartData quiver = root.addChild("quiver",
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-1.5F, -5.0F, 2.0F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)),
                ModelTransform.of(0.0F, 5.0F, 0.0F, 0.0F, 0.0F, -0.5235988F)
        );

        // Arrows protruding from top of quiver
        quiver.addChild("arrows",
                ModelPartBuilder.create()
                        .uv(0, 3).cuboid(-1.0F, -7.0F, 2.5F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                        .uv(6, 4).cuboid(-0.5F, -8.0F, 3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)),
                ModelTransform.NONE
        );

        return TexturedModelData.of(modelData, 16, 16);
    }

    public void setArrowsVisible(boolean visible) {
        this.arrows.visible = visible;
    }

    public ModelPart getQuiver() {
        return this.quiver;
    }
}
