package net.mastermaxi.mastermaxisvanillaplus.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.mastermaxi.mastermaxisvanillaplus.item.SchematicItem;
import net.mastermaxi.mastermaxisvanillaplus.screen.FletchingTableScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FletchingTableScreen extends HandledScreen<FletchingTableScreenHandler> {
    private static final Identifier TEXTURE = MASTERmaxisVanillaPlus.id("textures/gui/container/fletching_table.png");

    private static final Identifier WEAPON_SLOT_TEXTURE = MASTERmaxisVanillaPlus.id("textures/gui/slots/weapon_slot.png");
    private static final Identifier ATTACHMENT_SLOT_TEXTURE = MASTERmaxisVanillaPlus.id("textures/gui/slots/attachment_slot.png");
    private static final Identifier BLUEPRINT_SLOT_TEXTURE = MASTERmaxisVanillaPlus.id("textures/gui/slots/blueprint_slot.png");
    private static final Identifier FLETCHING_SLOT_TEXTURE = MASTERmaxisVanillaPlus.id("textures/gui/slots/fletching_slot.png");
    private static final Identifier SHAFT_SLOT_TEXTURE = MASTERmaxisVanillaPlus.id("textures/gui/slots/shaft_slot.png");
    private static final Identifier POINT_SLOT_TEXTURE = MASTERmaxisVanillaPlus.id("textures/gui/slots/point_slot.png");

    public FletchingTableScreen(FletchingTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
        this.titleX = 8;
        this.titleY = 6;
        this.playerInventoryTitleX = 8;
        this.playerInventoryTitleY = 72;
    }

    private static final int INGREDIENT_SLOT_Y = 46;

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int left = this.x;
        int top = this.y;
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, left, top, 0.0f, 0.0f, this.backgroundWidth, this.backgroundHeight, 256, 256);

        // Render slot icons when slots are empty
        // Weapon slot (Slot 0: 80, 18)
        if (!this.handler.getSlot(FletchingTableScreenHandler.WEAPON_SLOT_INDEX).hasStack()) {
            drawSlotIcon(context, WEAPON_SLOT_TEXTURE, 80, 18);
        }
        // Attachment slot (Slot 1: 22, 18)
        if (!this.handler.getSlot(FletchingTableScreenHandler.ATTACHMENT_SLOT_INDEX).hasStack()) {
            drawSlotIcon(context, ATTACHMENT_SLOT_TEXTURE, 22, 18);
        }
        // Blueprint / Schematic slot (Slot 6: 138, 18)
        if (!this.handler.getSlot(FletchingTableScreenHandler.SCHEMATIC_SLOT_INDEX).hasStack()) {
            drawSlotIcon(context, BLUEPRINT_SLOT_TEXTURE, 138, 18);
        }

        // Bottom ingredient slots (Slots 2, 3, 4 at y = 46, x = 22, 53, 84)
        SchematicItem activeScheme = this.handler.getActiveUpgradeScheme();
        if (activeScheme == ItemInit.IRON_CROSSBOW_SCHEME) {
            // Mode B: Ghost items for Iron Crossbow upgrade
            if (!this.handler.getSlot(FletchingTableScreenHandler.INGREDIENT_A_SLOT_INDEX).hasStack()) {
                drawGrayedOutGhostItem(context, new ItemStack(Items.IRON_INGOT), 22, 3);
            }
            if (!this.handler.getSlot(FletchingTableScreenHandler.INGREDIENT_B_SLOT_INDEX).hasStack()) {
                drawGrayedOutGhostItem(context, new ItemStack(Items.SLIME_BALL), 53, 1);
            }
            if (!this.handler.getSlot(FletchingTableScreenHandler.INGREDIENT_C_SLOT_INDEX).hasStack()) {
                drawGrayedOutGhostItem(context, new ItemStack(Items.STRING), 84, 2);
            }
        } else if (activeScheme == ItemInit.NETHER_CROSSBOW_SCHEME) {
            if (!this.handler.getSlot(FletchingTableScreenHandler.INGREDIENT_A_SLOT_INDEX).hasStack()) {
                drawGrayedOutGhostItem(context, new ItemStack(Items.GOLD_INGOT), 22, 3);
            }
            if (!this.handler.getSlot(FletchingTableScreenHandler.INGREDIENT_B_SLOT_INDEX).hasStack()) {
                drawGrayedOutGhostItem(context, new ItemStack(Items.COPPER_INGOT), 53, 5);
            }
            if (!this.handler.getSlot(FletchingTableScreenHandler.INGREDIENT_C_SLOT_INDEX).hasStack()) {
                drawGrayedOutGhostItem(context, new ItemStack(Items.NETHERITE_INGOT), 84, 1);
            }
        } else if (activeScheme == ItemInit.HEAVY_BOW_SCHEME) {
            if (!this.handler.getSlot(FletchingTableScreenHandler.INGREDIENT_A_SLOT_INDEX).hasStack()) {
                drawGrayedOutGhostItem(context, new ItemStack(Items.LEATHER), 22, 5);
            }
            if (!this.handler.getSlot(FletchingTableScreenHandler.INGREDIENT_B_SLOT_INDEX).hasStack()) {
                drawGrayedOutGhostItem(context, new ItemStack(Items.SLIME_BALL), 53, 3);
            }
            if (!this.handler.getSlot(FletchingTableScreenHandler.INGREDIENT_C_SLOT_INDEX).hasStack()) {
                drawGrayedOutGhostItem(context, new ItemStack(Items.STRING), 84, 6);
            }
        } else if (activeScheme == ItemInit.HEAVY_HARDENED_BOW_SCHEME) {
            if (!this.handler.getSlot(FletchingTableScreenHandler.INGREDIENT_A_SLOT_INDEX).hasStack()) {
                drawGrayedOutGhostItem(context, new ItemStack(Items.IRON_INGOT), 22, 6);
            }
            if (!this.handler.getSlot(FletchingTableScreenHandler.INGREDIENT_B_SLOT_INDEX).hasStack()) {
                drawGrayedOutGhostItem(context, new ItemStack(Items.FLINT_AND_STEEL), 53, 1);
            }
        } else {
            // Mode A: Projectile Crafting slot icons
            if (!this.handler.getSlot(FletchingTableScreenHandler.INGREDIENT_A_SLOT_INDEX).hasStack()) {
                drawSlotIcon(context, FLETCHING_SLOT_TEXTURE, 22, 46);
            }
            if (!this.handler.getSlot(FletchingTableScreenHandler.INGREDIENT_B_SLOT_INDEX).hasStack()) {
                drawSlotIcon(context, SHAFT_SLOT_TEXTURE, 53, 46);
            }
            if (!this.handler.getSlot(FletchingTableScreenHandler.INGREDIENT_C_SLOT_INDEX).hasStack()) {
                drawSlotIcon(context, POINT_SLOT_TEXTURE, 84, 46);
            }
        }
        // Result slot (Slot 5: 138, 46) is intentionally left completely empty by default,
        // and only displays an item when a valid craft/upgrade result is created.
    }

    private void drawSlotIcon(DrawContext context, Identifier texture, int slotX, int slotY) {
        context.drawTexture(RenderLayer::getGuiTextured, texture, this.x + slotX, this.y + slotY, 0.0f, 0.0f, 16, 16, 16, 16);
    }

    private void drawGrayedOutGhostItem(DrawContext context, ItemStack stack, int slotX, int count) {
        if (stack.isEmpty()) return;
        int px = this.x + slotX;
        int py = this.y + INGREDIENT_SLOT_Y;

        // Draw item
        context.drawItem(stack, px, py);

        // Grayed-out dark tint overlay to clearly differentiate ghost item from real inserted item
        context.fill(px, py, px + 16, py + 16, 0x95404040);

        // Count overlay drawn on top for readability
        if (count > 1) {
            context.drawStackOverlay(this.textRenderer, stack, px, py, String.valueOf(count));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);

        // Tooltips for empty slots or special states
        if (this.focusedSlot != null) {
            Slot slot = this.focusedSlot;

            if (slot.id == FletchingTableScreenHandler.ATTACHMENT_SLOT_INDEX && slot.hasStack()) {
                ItemStack weapon = this.handler.getSlot(FletchingTableScreenHandler.WEAPON_SLOT_INDEX).getStack();
                if (FletchingTableScreenHandler.hasCurseOfWelding(weapon)) {
                    context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.welded"), mouseX, mouseY);
                }
            } else if (!slot.hasStack()) {
                SchematicItem activeScheme = this.handler.getActiveUpgradeScheme();

                if (slot.id == FletchingTableScreenHandler.WEAPON_SLOT_INDEX) {
                    // Slot 0: Weapon
                    context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.weapon_slot"), mouseX, mouseY);
                } else if (slot.id == FletchingTableScreenHandler.ATTACHMENT_SLOT_INDEX) {
                    // Slot 1: Attachment
                    context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.attachment_slot"), mouseX, mouseY);
                } else if (slot.id == FletchingTableScreenHandler.SCHEMATIC_SLOT_INDEX) {
                    // Slot 6: Schematic
                    context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.scheme_slot"), mouseX, mouseY);
                } else if (slot.id == FletchingTableScreenHandler.INGREDIENT_A_SLOT_INDEX) {
                    if (activeScheme == ItemInit.IRON_CROSSBOW_SCHEME) {
                        context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.req_iron"), mouseX, mouseY);
                    } else if (activeScheme == ItemInit.NETHER_CROSSBOW_SCHEME) {
                        context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.req_gold"), mouseX, mouseY);
                    } else if (activeScheme == ItemInit.HEAVY_BOW_SCHEME) {
                        context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.req_leather"), mouseX, mouseY);
                    } else if (activeScheme == ItemInit.HEAVY_HARDENED_BOW_SCHEME) {
                        context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.req_iron_2"), mouseX, mouseY);
                    } else {
                        context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.tail_slot"), mouseX, mouseY);
                    }
                } else if (slot.id == FletchingTableScreenHandler.INGREDIENT_B_SLOT_INDEX) {
                    if (activeScheme == ItemInit.IRON_CROSSBOW_SCHEME) {
                        context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.req_slime"), mouseX, mouseY);
                    } else if (activeScheme == ItemInit.NETHER_CROSSBOW_SCHEME) {
                        context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.req_copper"), mouseX, mouseY);
                    } else if (activeScheme == ItemInit.HEAVY_BOW_SCHEME) {
                        context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.req_slime_2"), mouseX, mouseY);
                    } else if (activeScheme == ItemInit.HEAVY_HARDENED_BOW_SCHEME) {
                        context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.req_flint_and_steel"), mouseX, mouseY);
                    } else {
                        context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.shaft_slot"), mouseX, mouseY);
                    }
                } else if (slot.id == FletchingTableScreenHandler.INGREDIENT_C_SLOT_INDEX) {
                    if (activeScheme == ItemInit.IRON_CROSSBOW_SCHEME || activeScheme == ItemInit.HEAVY_BOW_SCHEME) {
                        context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.req_string"), mouseX, mouseY);
                    } else if (activeScheme == ItemInit.NETHER_CROSSBOW_SCHEME) {
                        context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.req_netherite"), mouseX, mouseY);
                    } else if (activeScheme == null) {
                        context.drawTooltip(this.textRenderer, Text.translatable("gui.mastermaxisvanillaplus.fletching_table.head_slot"), mouseX, mouseY);
                    }
                }
            }
        }
    }
}
