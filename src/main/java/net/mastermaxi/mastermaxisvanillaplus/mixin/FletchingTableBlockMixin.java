package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.block.FletchingTableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FletchingTableBlock.class)
public abstract class FletchingTableBlockMixin extends Block implements BlockEntityProvider {
    public FletchingTableBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FletchingTableBlockEntity(pos, state);
    }

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onUseFletchingTable(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (world.isClient) {
            cir.setReturnValue(ActionResult.SUCCESS);
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (!(blockEntity instanceof FletchingTableBlockEntity)) {
                blockEntity = new FletchingTableBlockEntity(pos, state);
                world.addBlockEntity(blockEntity);
            }
            player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
            cir.setReturnValue(ActionResult.CONSUME);
        }
    }
}
