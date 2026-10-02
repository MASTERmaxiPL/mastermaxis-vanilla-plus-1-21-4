package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.item.BoltItem;
import net.mastermaxi.mastermaxisvanillaplus.item.PyrotechnicTubeItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Mixin to ensure all crossbows (vanilla and modded) only accept bolts by default,
 * require the Pyrotechnic Tube attachment to use fireworks,
 * and display attachment tooltip.
 */
@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Inject(method = "getHeldProjectiles", at = @At("HEAD"), cancellable = true)
    private void injectHeldProjectilesToBolts(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        cir.setReturnValue(stack -> stack.getItem() instanceof BoltItem);
    }

    @Inject(method = "getProjectiles", at = @At("HEAD"), cancellable = true)
    private void injectProjectilesToBolts(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        cir.setReturnValue(stack -> stack.getItem() instanceof BoltItem);
    }

    @Inject(method = "shootAll", at = @At("HEAD"), cancellable = true)
    private void preventFireworksWithoutAttachment(
            World world,
            LivingEntity shooter,
            Hand hand,
            ItemStack stack,
            float speed,
            float divergence,
            LivingEntity target,
            CallbackInfo ci
    ) {
        if (!PyrotechnicTubeItem.hasAttachment(stack)) {
            ChargedProjectilesComponent charged = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
            if (charged != null) {
                for (ItemStack projectile : charged.getProjectiles()) {
                    if (projectile.isOf(Items.FIREWORK_ROCKET)) {
                        ci.cancel();
                        return;
                    }
                }
            }
        }
    }

    @Inject(method = "appendTooltip", at = @At("HEAD"))
    private void injectAttachmentTooltip(
            ItemStack stack,
            Item.TooltipContext context,
            List<Text> tooltip,
            TooltipType type,
            CallbackInfo ci
    ) {
        if (net.mastermaxi.mastermaxisvanillaplus.screen.FletchingTableScreenHandler.isViewfinderCrossbow(stack)) {
            tooltip.add(Text.translatable("tooltip.mastermaxisvanillaplus.attachment.viewfinder").formatted(Formatting.GRAY));
        }
        if (PyrotechnicTubeItem.hasAttachment(stack)) {
            tooltip.add(Text.translatable("tooltip.mastermaxisvanillaplus.attachment.pyrotechnic_tube").formatted(Formatting.GRAY));
        }
    }
}
