package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class PrismarineArrowEntityModel extends EntityModel<ProjectileEntityRenderState> {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(MASTERmaxisVanillaPlus.id("prismarine_arrow"), "main");

    public PrismarineArrowEntityModel(ModelPart root) {
        super(root, RenderLayer::getEntityCutout);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // Back fletching cap (x = -11.0F)
        root.addChild(
                "back",
                ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, -2.5F, -2.5F, 0.0F, 5.0F, 5.0F),
                ModelTransform.of(-11.0F, 0.0F, 0.0F, (float) (Math.PI / 4.0), 0.0F, 0.0F).withScale(0.8F)
        );

        // Streamlined crossed planes for shaft, binding, and sharp prismarine shard tip:
        // x = -12.0F to +4.0F (length 16.0F), height 4.0F (scaleY 0.8 to match 5px texture layout)
        ModelPartBuilder crossBuilder = ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-12.0F, -2.0F, 0.0F, 16.0F, 4.0F, 0.0F, Dilation.NONE, 1.0F, 0.8F);

        root.addChild("cross_1", crossBuilder, ModelTransform.rotation((float) (Math.PI / 4.0), 0.0F, 0.0F));
        root.addChild("cross_2", crossBuilder, ModelTransform.rotation((float) (Math.PI * 3.0 / 4.0), 0.0F, 0.0F));

        return TexturedModelData.of(modelData.transform(modelTransform -> modelTransform.scaled(0.9F)), 32, 32);
    }

    @Override
    public void setAngles(ProjectileEntityRenderState state) {
        super.setAngles(state);
        if (state.shake > 0.0F) {
            float f = -MathHelper.sin(state.shake * 3.0F) * state.shake;
            this.root.roll += f * (float) (Math.PI / 180.0);
        }
    }
}
