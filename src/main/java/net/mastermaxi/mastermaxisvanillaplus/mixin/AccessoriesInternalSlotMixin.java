package net.mastermaxi.mastermaxisvanillaplus.mixin;

import io.wispforest.accessories.api.AccessoriesContainer;
import io.wispforest.accessories.api.menu.AccessoriesBasedSlot;
import io.wispforest.accessories.impl.ExpandedSimpleContainer;
import io.wispforest.accessories.menu.AccessoriesInternalSlot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(AccessoriesInternalSlot.class)
public abstract class AccessoriesInternalSlotMixin extends AccessoriesBasedSlot {

    @Shadow(remap = false) @Final public boolean isCosmetic;
    @Shadow(remap = false) public boolean useCosmeticIcon;

    protected AccessoriesInternalSlotMixin(AccessoriesContainer accessoriesContainer, ExpandedSimpleContainer container, int slot, int x, int y) {
        super(accessoriesContainer, container, slot, x, y);
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void mastermaxisvanillaplus$disableCosmeticIconForBack(AccessoriesContainer container, boolean isCosmetic, int slot, int x, int y, CallbackInfo ci) {
        if (isCosmetic && "back".equals(container.getSlotName())) {
            this.useCosmeticIcon = false;
        }
    }

    @Inject(method = "getBackgroundSprite", at = @At("HEAD"), cancellable = true)
    private void mastermaxisvanillaplus$backCosmeticBackground(CallbackInfoReturnable<Identifier> cir) {
        if (this.isCosmetic && "back".equals(this.slotName())) {
            cir.setReturnValue(super.getBackgroundSprite());
        }
    }
}
