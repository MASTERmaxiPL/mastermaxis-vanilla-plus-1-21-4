package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.ArrowEntityRenderState;

@Environment(EnvType.CLIENT)
public class PrismarineArrowRenderState extends ArrowEntityRenderState {
    public boolean heavy;
}
