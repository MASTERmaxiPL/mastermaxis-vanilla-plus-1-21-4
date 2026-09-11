package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.gen.feature.OreFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OreFeature.class)
public abstract class OreFeatureMixin {
    @Redirect(
            method = "generateVeinPart",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/ChunkSection;setBlockState(IIILnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;"
            )
    )
    private BlockState mastermaxisvanillaplus(
            ChunkSection section, int x, int y, int z, BlockState newState, boolean lock) {
        BlockState current = section.getBlockState(x, y, z);
        if (current.isOf(Blocks.MUDSTONE_BLOCK)) {
            if (newState.isOf(net.minecraft.block.Blocks.COAL_ORE)) {
                newState = Blocks.MUDSTONE_COAL_ORE.getDefaultState();
            } else if (newState.isOf(net.minecraft.block.Blocks.IRON_ORE)) {
                newState = Blocks.MUDSTONE_IRON_ORE.getDefaultState();
            } else if (newState.isOf(net.minecraft.block.Blocks.COPPER_ORE)) {
                newState = Blocks.MUDSTONE_COPPER_ORE.getDefaultState();
            } else if (newState.isOf(net.minecraft.block.Blocks.GRAVEL)) {
                newState = Blocks.SILTED_GRAVEL.getDefaultState();
            } else if (newState.isOf(net.minecraft.block.Blocks.GRANITE)
                    || newState.isOf(net.minecraft.block.Blocks.DIORITE)
                    || newState.isOf(net.minecraft.block.Blocks.ANDESITE)
                    || newState.isOf(net.minecraft.block.Blocks.STONE)
                    || newState.isOf(net.minecraft.block.Blocks.DIRT)) {
                // Prevent stone/granite/diorite/andesite/dirt veins from replacing mudstone and corrupting the landscape
                return current;
            }
        }
        return section.setBlockState(x, y, z, newState, lock);
    }
}