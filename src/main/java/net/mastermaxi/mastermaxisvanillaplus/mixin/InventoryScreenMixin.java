package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends HandledScreen<PlayerScreenHandler> {

    public InventoryScreenMixin(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void ensurePreviousScreenClosed(CallbackInfo ci) {
        if (this.client != null && this.client.player != null) {
            if (this.client.player.currentScreenHandler != this.client.player.playerScreenHandler) {
                int syncId = this.client.player.currentScreenHandler.syncId;
                if (this.client.getNetworkHandler() != null) {
                    this.client.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(syncId));
                }
                this.client.player.currentScreenHandler.onClosed(this.client.player);
                this.client.player.currentScreenHandler = this.client.player.playerScreenHandler;
            }
        }
    }
}
