package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SaplingBlock.class)
public class SaplingBlockMixin {
    @Inject(method = "generate", at = @At("HEAD"), cancellable = true)
    private void witherNonAcacia(ServerWorld world, BlockPos pos, BlockState state, Random random, CallbackInfo ci) {
        if (world.getBlockState(pos.down()).isOf(Blocks.GRASSY_RED_SOIL)) {
            if (!state.isOf(net.minecraft.block.Blocks.ACACIA_SAPLING)) {
                world.setBlockState(pos, net.minecraft.block.Blocks.DEAD_BUSH.getDefaultState());
                world.playSound(null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.7F, 0.8F);
                world.spawnParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, 5, 0.1, 0.1, 0.1, 0.02);
                ci.cancel();
            }
        }
    }
}
