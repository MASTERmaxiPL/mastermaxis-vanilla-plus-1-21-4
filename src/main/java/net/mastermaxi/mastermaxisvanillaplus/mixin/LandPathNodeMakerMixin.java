package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LandPathNodeMaker.class)
public class LandPathNodeMakerMixin {
    @Inject(method = "getCommonNodeType", at = @At("HEAD"), cancellable = true)
    private static void mastermaxisvanillaplus$avoidRedthorn(BlockView world, BlockPos pos, CallbackInfoReturnable<PathNodeType> cir) {
        BlockState state = world.getBlockState(pos);
        if (state.isOf(Blocks.REDTHORN)) {
            cir.setReturnValue(PathNodeType.DAMAGE_OTHER);
        }
    }
}
