package net.mastermaxi.mastermaxisvanillaplus.network;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.mastermaxi.mastermaxisvanillaplus.entity.EntityInit;
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.BackpackEntity;
import net.mastermaxi.mastermaxisvanillaplus.item.BackpackItem;
import net.mastermaxi.mastermaxisvanillaplus.item.QuiverItem;
import net.mastermaxi.mastermaxisvanillaplus.screen.BackpackScreenHandler;
import net.mastermaxi.mastermaxisvanillaplus.screen.QuiverScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class ModNetworking {

    @SuppressWarnings("resource")
    public static void initialize() {
        // Register payloads
        PayloadTypeRegistry.playC2S().register(OpenBackpackC2SPayload.ID, OpenBackpackC2SPayload.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(PlaceBackpackC2SPayload.ID, PlaceBackpackC2SPayload.PACKET_CODEC);

        // Open Backpack Key Packet Handler
        ServerPlayNetworking.registerGlobalReceiver(OpenBackpackC2SPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            context.server().execute(() -> openEquippedBackpackOrQuiver(player));
        });

        // Place Backpack Key Packet Handler
        ServerPlayNetworking.registerGlobalReceiver(PlaceBackpackC2SPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            context.server().execute(() -> placeBackpackFromBackSlot(player));
        });

        // Shift + Right Click with empty hand on block to place backpack
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
            if (!player.isSneaking()) return ActionResult.PASS;
            if (!player.getMainHandStack().isEmpty()) return ActionResult.PASS;

            if (world.isClient) {
                AccessoriesCapability cap = AccessoriesCapability.get(player);
                if (cap != null) {
                    AccessoriesContainer back = cap.getContainers().get("back");
                    if (back != null && back.getAccessories().getStack(0).getItem() instanceof BackpackItem) {
                        return ActionResult.SUCCESS;
                    }
                }
                return ActionResult.PASS;
            }

            if (player instanceof ServerPlayerEntity serverPlayer) {
                if (placeBackpackAtBlock(serverPlayer, hitResult)) {
                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.PASS;
        });
    }

    public static void openEquippedBackpackOrQuiver(ServerPlayerEntity player) {
        AccessoriesCapability cap = AccessoriesCapability.get(player);
        if (cap == null) return;

        AccessoriesContainer back = cap.getContainers().get("back");
        if (back == null) return;

        ItemStack stack = back.getAccessories().getStack(0);
        if (stack.getItem() instanceof BackpackItem backpackItem) {
            player.openHandledScreen(new ExtendedScreenHandlerFactory<BackpackPayload>() {
                @Override
                public BackpackPayload getScreenOpeningData(ServerPlayerEntity player) {
                    return new BackpackPayload(backpackItem.getTier().ordinal(), -1);
                }

                @Override
                public Text getDisplayName() {
                    return stack.getName();
                }

                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                    return new BackpackScreenHandler(syncId, playerInventory, stack, backpackItem.getTier(), -1);
                }
            });
        } else if (stack.getItem() instanceof QuiverItem) {
            player.openHandledScreen(new NamedScreenHandlerFactory() {
                @Override
                public Text getDisplayName() {
                    return stack.getName();
                }

                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                    return new QuiverScreenHandler(syncId, playerInventory, stack);
                }
            });
        }
    }

    public static boolean placeBackpackAtBlock(ServerPlayerEntity player, BlockHitResult hitResult) {
        AccessoriesCapability cap = AccessoriesCapability.get(player);
        if (cap == null) return false;

        AccessoriesContainer back = cap.getContainers().get("back");
        if (back == null) return false;

        ItemStack stack = back.getAccessories().getStack(0);
        if (!(stack.getItem() instanceof BackpackItem)) return false;

        BlockPos placePos = hitResult.getBlockPos().offset(hitResult.getSide());
        World world = player.getWorld();

        BackpackEntity entity = new BackpackEntity(EntityInit.BACKPACK, world);
        entity.refreshPositionAndAngles(placePos.getX() + 0.5, placePos.getY(), placePos.getZ() + 0.5, player.getYaw() + 180.0F, 0.0F);
        entity.setBackpackStack(stack.copy());

        back.getAccessories().setStack(0, ItemStack.EMPTY);
        back.markChanged();

        world.spawnEntity(entity);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);

        return true;
    }

    public static void placeBackpackFromBackSlot(ServerPlayerEntity player) {
        Vec3d eyePos = player.getEyePos();
        Vec3d lookVec = player.getRotationVec(1.0F);
        Vec3d reachVec = eyePos.add(lookVec.multiply(4.5));

        BlockHitResult hitResult = player.getWorld().raycast(new RaycastContext(
                eyePos, reachVec, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player
        ));

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            placeBackpackAtBlock(player, hitResult);
        }
    }
}
