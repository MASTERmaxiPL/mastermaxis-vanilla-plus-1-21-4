package net.mastermaxi.mastermaxisvanillaplus.entity.custom;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import net.mastermaxi.mastermaxisvanillaplus.item.BackpackItem;
import net.mastermaxi.mastermaxisvanillaplus.item.QuiverItem;
import net.mastermaxi.mastermaxisvanillaplus.network.BackpackPayload;
import net.mastermaxi.mastermaxisvanillaplus.screen.BackpackScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BackpackEntity extends Entity {

    private static final TrackedData<ItemStack> BACKPACK_STACK = DataTracker.registerData(
            BackpackEntity.class, TrackedDataHandlerRegistry.ITEM_STACK
    );

    public BackpackEntity(EntityType<? extends BackpackEntity> type, World world) {
        super(type, world);
        this.intersectionChecked = true;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(BACKPACK_STACK, ItemStack.EMPTY);
    }

    public ItemStack getBackpackStack() {
        return this.getDataTracker().get(BACKPACK_STACK);
    }

    public void setBackpackStack(ItemStack stack) {
        this.getDataTracker().set(BACKPACK_STACK, stack);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.hasNoGravity()) {
            this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
        }

        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.98, 0.98, 0.98));
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (this.getWorld().isClient) {
            return ActionResult.SUCCESS;
        }

        ItemStack entityStack = this.getBackpackStack();
        if (!(entityStack.getItem() instanceof BackpackItem backpackItem)) {
            return ActionResult.PASS;
        }

        if (player.isSneaking()) {
            // Shift + Right click: Pick up / Swap
            AccessoriesCapability capability = AccessoriesCapability.get(player);
            AccessoriesContainer back = capability != null ? capability.getContainers().get("back") : null;

            if (back != null) {
                ItemStack currentWorn = back.getAccessories().getStack(0);

                if (currentWorn.getItem() instanceof BackpackItem) {
                    // Swap worn backpack with the entity's backpack on this block
                    ItemStack wornCopy = currentWorn.copy();
                    back.getAccessories().setStack(0, entityStack.copy());
                    back.markChanged();
                    this.setBackpackStack(wornCopy);
                    this.getWorld().playSound(null, getX(), getY(), getZ(),
                            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                    return ActionResult.SUCCESS;
                } else if (currentWorn.getItem() instanceof QuiverItem || currentWorn.isOf(Items.ELYTRA)) {
                    // Try to find free slot in player inventory, else drop into world
                    ItemStack wornCopy = currentWorn.copy();
                    if (!player.getInventory().insertStack(wornCopy)) {
                        player.dropItem(wornCopy, true, false);
                    }
                    back.getAccessories().setStack(0, entityStack.copy());
                    back.markChanged();
                    this.discard();
                    this.getWorld().playSound(null, getX(), getY(), getZ(),
                            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                    return ActionResult.SUCCESS;
                } else {
                    // Back slot is empty or has another item
                    if (!currentWorn.isEmpty()) {
                        if (!player.getInventory().insertStack(currentWorn.copy())) {
                            player.dropItem(currentWorn.copy(), true, false);
                        }
                    }
                    back.getAccessories().setStack(0, entityStack.copy());
                    back.markChanged();
                    this.discard();
                    this.getWorld().playSound(null, getX(), getY(), getZ(),
                            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                    return ActionResult.SUCCESS;
                }
            }
        } else {
            // Normal Right Click: Open the backpack inventory
            if (player instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.openHandledScreen(new ExtendedScreenHandlerFactory<BackpackPayload>() {
                    @Override
                    public BackpackPayload getScreenOpeningData(ServerPlayerEntity player) {
                        return new BackpackPayload(backpackItem.getTier().ordinal(), getId());
                    }

                    @Override
                    public Text getDisplayName() {
                        return entityStack.getName();
                    }

                    @Override
                    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                        return new BackpackScreenHandler(syncId, playerInventory, entityStack, backpackItem.getTier(), getId());
                    }
                });
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (this.isRemoved()) return false;

        ItemStack stack = this.getBackpackStack();
        if (stack.getItem() instanceof BackpackItem backpackItem) {
            // Drop backpack item
            this.dropStack(world, stack.copy());

            // Drop stored items unless Ender Backpack
            if (backpackItem.getTier() != BackpackItem.BackpackTier.ENDER) {
                backpackItem.dropStoredItems(world, (source.getAttacker() instanceof PlayerEntity p) ? p : null, getX(), getY(), getZ(), stack);
            }
        }

        this.getWorld().playSound(null, getX(), getY(), getZ(),
                SoundEvents.ENTITY_ARMOR_STAND_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
        this.discard();
        return true;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("Backpack")) {
            ItemStack.CODEC.parse(this.getRegistryManager().getOps(NbtOps.INSTANCE), nbt.get("Backpack"))
                    .result()
                    .ifPresent(this::setBackpackStack);
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        ItemStack.CODEC.encodeStart(this.getRegistryManager().getOps(NbtOps.INSTANCE), this.getBackpackStack())
                .result()
                .ifPresent(tag -> nbt.put("Backpack", tag));
    }
}
