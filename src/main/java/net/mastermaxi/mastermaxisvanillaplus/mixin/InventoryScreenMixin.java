package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends HandledScreen<PlayerScreenHandler> {

    @Unique
    private static final Identifier INVENTORY_TEXTURE = Identifier.ofVanilla("textures/gui/container/inventory.png");
    @Unique
    private static final Identifier BACK_SLOT_ICON = MASTERmaxisVanillaPlus.id("textures/gui/slots/back_slot_icon.png");

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

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void renderBackSlot(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        int slotX = this.x + 76;
        int slotY = this.y + 43;

        // Draw 18x18 slot background frame
        context.drawTexture(RenderLayer::getGuiTextured, INVENTORY_TEXTURE, slotX, slotY, 76.0F, 61.0F, 18, 18, 256, 256);

        // If slot is empty, draw back slot icon
        if (this.handler.slots.size() > 46 && !this.handler.getSlot(46).hasStack()) {
            context.drawTexture(RenderLayer::getGuiTextured, BACK_SLOT_ICON, slotX + 1, slotY + 1, 0.0F, 0.0F, 16, 16, 16, 16);
        }
    }
}
