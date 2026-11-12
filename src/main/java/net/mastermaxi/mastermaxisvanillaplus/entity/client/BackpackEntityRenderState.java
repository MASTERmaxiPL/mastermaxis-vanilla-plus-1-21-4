package net.mastermaxi.mastermaxisvanillaplus.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class BackpackEntityRenderState extends EntityRenderState {
    public ItemStack backpackStack = ItemStack.EMPTY;
    public float yaw = 0.0F;
    public boolean hasArrows = false;
}
