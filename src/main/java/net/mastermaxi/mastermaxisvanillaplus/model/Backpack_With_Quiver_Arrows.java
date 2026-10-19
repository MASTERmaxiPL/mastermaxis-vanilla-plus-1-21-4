package net.mastermaxi.mastermaxisvanillaplus.model;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class Backpack_With_Quiver_Arrows extends Model {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(MASTERmaxisVanillaPlus.id("backpack_with_quiver_arrows"), "main");
    private final ModelPart arrows;

    public Backpack_With_Quiver_Arrows(ModelPart root) {
        super(root, RenderLayer::getEntityCutoutNoCull);
        this.arrows = root.getChild("arrows");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData partDefinition = modelData.getRoot();

        ModelPartData arrows = partDefinition.addChild("arrows",
                ModelPartBuilder.create()
                        .uv(0, 3).cuboid(3.0F, -12.0F, 2.0F, 2.0F, 2.0F, 0.0F, new Dilation(0.0F))
                        .uv(4, 5).cuboid(2.0F, -11.0F, 2.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
                        .uv(2, 5).cuboid(4.0F, -11.0F, 3.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
                        .uv(0, 5).cuboid(3.0F, -12.0F, 3.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F))
                        .uv(2, 6).cuboid(2.0F, -11.0F, 3.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
                        .uv(4, 3).cuboid(2.0F, -12.0F, 4.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F))
                        .uv(6, 2).cuboid(3.0F, -11.0F, 4.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
                        .uv(0, 2).cuboid(2.0F, -11.0F, 5.0F, 3.0F, 1.0F, 0.0F, new Dilation(0.0F))
                        .uv(6, 3).cuboid(3.0F, -12.0F, 5.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 12.0F, 0.0F)
        );

        arrows.addChild("cube_r1",
                ModelPartBuilder.create()
                        .uv(6, 4).cuboid(4.0F, -12.0F, -4.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
                        .uv(0, 1).cuboid(1.0F, -11.0F, -4.0F, 5.0F, 1.0F, 0.0F, new Dilation(0.0F))
                        .uv(4, 6).cuboid(3.0F, -12.0F, -3.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
                        .uv(0, 0).cuboid(1.0F, -11.0F, -3.0F, 5.0F, 1.0F, 0.0F, new Dilation(0.0F)),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F)
        );

        return TexturedModelData.of(modelData, 16, 16);
    }

    public ModelPart getArrows() {
        return this.arrows;
    }
}