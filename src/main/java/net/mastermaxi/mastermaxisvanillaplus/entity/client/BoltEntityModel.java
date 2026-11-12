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
public class BoltEntityModel extends EntityModel<ProjectileEntityRenderState> {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(MASTERmaxisVanillaPlus.id("bolt"), "main");

    public BoltEntityModel(ModelPart root) {
        super(root, RenderLayer::getEntityCutout);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // Crossbow bolt is shorter than a bow arrow (13.0F vs 16.0F).
        // Feathers are narrower and longer: length 6.0F, low profile.
        // Streamlined crossed planes without a thickened 3D box; iron quarrel tip is textured directly on the planes.
        ModelPartBuilder crossBuilder = ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-9.0F, -1.25F, 0.0F, 13.0F, 2.5F, 0.0F, Dilation.NONE, 1.0F, 1.0F);

        root.addChild("cross_1", crossBuilder, ModelTransform.rotation((float) (Math.PI / 4.0), 0.0F, 0.0F));
        root.addChild("cross_2", crossBuilder, ModelTransform.rotation((float) (Math.PI * 3.0 / 4.0), 0.0F, 0.0F));

        // Back cap of fletchings at tail
        root.addChild(
                "back",
                ModelPartBuilder.create().uv(0, 4).cuboid(0.0F, -1.25F, -1.25F, 0.0F, 2.5F, 2.5F),
                ModelTransform.of(-8.5F, 0.0F, 0.0F, (float) (Math.PI / 4.0), 0.0F, 0.0F).withScale(0.8F)
        );

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
