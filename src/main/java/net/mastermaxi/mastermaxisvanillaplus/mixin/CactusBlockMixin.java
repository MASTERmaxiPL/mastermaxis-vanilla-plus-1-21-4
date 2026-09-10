package net.mastermaxi.mastermaxisvanillaplus.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CactusBlock.class)
public class CactusBlockMixin {
    @WrapOperation(
            method = "canPlaceAt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z")
    )
    private boolean allowCactusOnGrassyRedSoil(BlockState state, TagKey<Block> tag, Operation<Boolean> original) {
        return original.call(state, tag) || state.isOf(Blocks.GRASSY_RED_SOIL);
    }
}
