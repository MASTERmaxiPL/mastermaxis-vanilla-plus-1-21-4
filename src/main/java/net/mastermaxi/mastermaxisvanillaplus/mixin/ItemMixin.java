package net.mastermaxi.mastermaxisvanillaplus.mixin;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import net.mastermaxi.mastermaxisvanillaplus.component.ModDataComponentTypes;
import net.mastermaxi.mastermaxisvanillaplus.item.BackpackItem;
import net.mastermaxi.mastermaxisvanillaplus.item.QuiverItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.mastermaxi.mastermaxisvanillaplus.item.PrismarineArrowRestItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"unused", "ConstantValue"})
@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "appendTooltip", at = @At("HEAD"))
    private void injectBowAttachmentTooltip(
            ItemStack stack,
            Item.TooltipContext context,
            java.util.List<Text> tooltip,
            TooltipType type,
            CallbackInfo ci
    ) {
        if (stack.getItem() instanceof BowItem && PrismarineArrowRestItem.hasAttachment(stack)) {
            tooltip.add(Text.translatable("tooltip.mastermaxisvanillaplus.attachment.prismarine_arrow_rest").formatted(Formatting.GRAY));
        }
    }

    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    private void modifyMaxCount(CallbackInfoReturnable<Integer> ci) {
        if ((Object) this == Items.SNOWBALL) {
            ci.setReturnValue(64);
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void equipElytraToBackSlot(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack held = player.getStackInHand(hand);
        if (!held.isOf(Items.ELYTRA)) {
            return;
        }
        AccessoriesCapability capability = AccessoriesCapability.get(player);
        if (capability == null) {
            return;
        }

        AccessoriesContainer back = capability.getContainers().get("back");
        if (back == null) {
            return;
        }

        ItemStack current = back.getAccessories().getStack(0);

        if (current.isEmpty()) {
            ItemStack toEquip = held.splitUnlessCreative(1, player);
            back.getAccessories().setStack(0, toEquip);
            back.markChanged();

            ItemStack newHand = player.isCreative() ? held : ItemStack.EMPTY;
            player.setStackInHand(hand, newHand);

            if (!world.isClient) {
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                player.incrementStat(Stats.USED.getOrCreateStat(held.getItem()));
                if (player instanceof ServerPlayerEntity serverPlayer) {
                    serverPlayer.playerScreenHandler.syncState();
                    serverPlayer.currentScreenHandler.syncState();
                }
            }
            cir.setReturnValue(ActionResult.SUCCESS.withNewHandStack(newHand));
        } else if (current.getItem() instanceof BackpackItem || current.getItem() instanceof QuiverItem || current.isOf(Items.ELYTRA)) {
            if (EnchantmentHelper.hasAnyEnchantmentsWith(current, EnchantmentEffectComponentTypes.PREVENT_ARMOR_CHANGE) && !player.isCreative()) {
                cir.setReturnValue(ActionResult.FAIL);
                return;
            }

            ItemStack old = current.copy();
            ItemStack toEquip = held.splitUnlessCreative(1, player);
            back.getAccessories().setStack(0, toEquip);
            back.markChanged();

            if (!world.isClient) {
                if (old.getItem() instanceof BackpackItem oldBackpack) {
                    oldBackpack.dropStoredItems(world, player, old);
                } else if (old.getItem() instanceof QuiverItem oldQuiver) {
                    oldQuiver.dropStoredArrows(world, player, old);
                }
            } else {
                old.remove(DataComponentTypes.CONTAINER);
                old.remove(ModDataComponentTypes.QUIVER);
            }

            ItemStack newHand;
            if (held.getCount() <= 1 && !player.isCreative()) {
                newHand = old.copy();
            } else {
                newHand = held;
            }
            player.setStackInHand(hand, newHand);

            if (!world.isClient) {
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                player.incrementStat(Stats.USED.getOrCreateStat(held.getItem()));

                if (held.getCount() > 1 || player.isCreative()) {
                    if (!player.getInventory().insertStack(old)) {
                        player.dropItem(old, false);
                    }
                }

                if (player instanceof ServerPlayerEntity serverPlayer) {
                    serverPlayer.playerScreenHandler.syncState();
                    serverPlayer.currentScreenHandler.syncState();
                }
            }
            cir.setReturnValue(ActionResult.SUCCESS.withNewHandStack(newHand));
        }
    }
}
