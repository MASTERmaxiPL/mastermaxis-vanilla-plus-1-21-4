package net.mastermaxi.mastermaxisvanillaplus.client.gui.screen;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.item.BackpackItem;
import net.mastermaxi.mastermaxisvanillaplus.screen.BackpackScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BackpackScreen extends HandledScreen<BackpackScreenHandler> {

    private static final Identifier LEATHER_TEXTURE = MASTERmaxisVanillaPlus.id("textures/gui/backpacks/leather_backpack.png");
    private static final Identifier IRON_TEXTURE = MASTERmaxisVanillaPlus.id("textures/gui/backpacks/iron_backpack.png");
    private static final Identifier IRON_QUIVER_TEXTURE = MASTERmaxisVanillaPlus.id("textures/gui/backpacks/backpack_with_quiver.png");
    private static final Identifier NETHERITE_TEXTURE = MASTERmaxisVanillaPlus.id("textures/gui/backpacks/netherite_backpack.png");
    private static final Identifier ENDER_TEXTURE = MASTERmaxisVanillaPlus.id("textures/gui/backpacks/ender_backpack.png");

    private final Identifier texture;

    public BackpackScreen(BackpackScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        BackpackItem.BackpackTier tier = handler.getTier();
        switch (tier) {
            case LEATHER -> {
                this.texture = LEATHER_TEXTURE;
                this.backgroundHeight = 132;
            }
            case IRON -> {
                this.texture = IRON_TEXTURE;
                this.backgroundHeight = 150;
            }
            case IRON_WITH_QUIVER -> {
                this.texture = IRON_QUIVER_TEXTURE;
                this.backgroundHeight = 174;
            }
            case NETHERITE -> {
                this.texture = NETHERITE_TEXTURE;
                this.backgroundHeight = 168;
            }
            case ENDER -> {
                this.texture = ENDER_TEXTURE;
                this.backgroundHeight = 171;
            }
            default -> {
                this.texture = LEATHER_TEXTURE;
                this.backgroundHeight = 132;
            }
        }
        this.backgroundWidth = 207;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = 12;
        this.titleY = 6;
        this.playerInventoryTitleX = 12;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderLayer::getGuiTextured, this.texture, x, y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
    }
}
