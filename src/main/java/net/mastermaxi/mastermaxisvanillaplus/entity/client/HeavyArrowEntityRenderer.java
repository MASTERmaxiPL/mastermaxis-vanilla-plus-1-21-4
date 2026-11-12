package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.ArrowEntityRenderState;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HeavyArrowEntityRenderer extends ArrowEntityRenderer {
    public static final Identifier TEXTURE = MASTERmaxisVanillaPlus.id("textures/entity/projectiles/heavy_arrow.png");

    public HeavyArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected Identifier getTexture(ArrowEntityRenderState arrowEntityRenderState) {
        return TEXTURE;
    }
}
