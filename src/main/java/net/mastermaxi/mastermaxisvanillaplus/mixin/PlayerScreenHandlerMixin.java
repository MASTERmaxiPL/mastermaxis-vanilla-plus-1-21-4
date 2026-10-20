package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.screen.BackSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends ScreenHandler {

    protected PlayerScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addBackSlot(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
        this.addSlot(new BackSlot(owner, 77, 44));
    }

    @Inject(method = "quickMove", at = @At("HEAD"), cancellable = true)
    private void handleBackSlotQuickMove(PlayerEntity player, int slotIndex, CallbackInfoReturnable<ItemStack> cir) {
        if (slotIndex == 46) {
            Slot slot = this.slots.get(slotIndex);
            if (slot.hasStack()) {
                ItemStack original = slot.getStack();
                if (!player.getWorld().isClient) {
                    if (original.getItem() instanceof net.mastermaxi.mastermaxisvanillaplus.item.BackpackItem oldBackpack) {
                        oldBackpack.dropStoredItems(player.getWorld(), player, original);
                    } else if (original.getItem() instanceof net.mastermaxi.mastermaxisvanillaplus.item.QuiverItem oldQuiver) {
                        oldQuiver.dropStoredArrows(player.getWorld(), player, original);
                    }
                }
                ItemStack copy = original.copy();
                if (!this.insertItem(original, 9, 45, false)) {
                    cir.setReturnValue(ItemStack.EMPTY);
                    return;
                }
                if (original.isEmpty()) {
                    slot.setStack(ItemStack.EMPTY);
                } else {
                    slot.markDirty();
                }
                if (player instanceof net.minecraft.server.network.ServerPlayerEntity serverPlayer) {
                    serverPlayer.playerScreenHandler.syncState();
                    serverPlayer.currentScreenHandler.syncState();
                }
                cir.setReturnValue(copy);
            }
        } else if (slotIndex >= 9 && slotIndex < 45) {
            Slot slot = this.slots.get(slotIndex);
            if (slot.hasStack()) {
                ItemStack original = slot.getStack();
                if (original.isIn(BackSlot.BACK_TAG) && !this.slots.get(46).hasStack()) {
                    ItemStack copy = original.copy();
                    if (this.insertItem(original, 46, 47, false)) {
                        if (original.isEmpty()) {
                            slot.setStack(ItemStack.EMPTY);
                        } else {
                            slot.markDirty();
                        }
                        if (player instanceof net.minecraft.server.network.ServerPlayerEntity serverPlayer) {
                            serverPlayer.playerScreenHandler.syncState();
                            serverPlayer.currentScreenHandler.syncState();
                        }
                        cir.setReturnValue(copy);
                    }
                }
            }
        }
    }
}
