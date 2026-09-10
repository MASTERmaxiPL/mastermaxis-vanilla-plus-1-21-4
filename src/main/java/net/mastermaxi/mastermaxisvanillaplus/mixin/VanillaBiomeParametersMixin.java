package net.mastermaxi.mastermaxisvanillaplus.mixin;

import com.mojang.datafixers.util.Pair;
import net.mastermaxi.mastermaxisvanillaplus.world.biome.ModBiomes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Consumer;

@Mixin(VanillaBiomeParameters.class)
public abstract class VanillaBiomeParametersMixin {
    @ModifyVariable(method = "writeOverworldBiomeParameters", at = @At("HEAD"), argsOnly = true)
    private Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mastermaxisvanillaplus$injectRustySavanna(
            Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> originalConsumer
    ) {
        return pair -> {
            RegistryKey<Biome> biomeKey = pair.getSecond();
            MultiNoiseUtil.NoiseHypercube cube = pair.getFirst();

            // Intercept regular SAVANNA entries and split them by temperature macro-region
            // so Rusty Savanna forms its own large, distinct, contiguous biome instead of intermingling locally
            if (BiomeKeys.SAVANNA.equals(biomeKey)) {
                MultiNoiseUtil.ParameterRange temperature = cube.temperature();
                long min = temperature.min();
                long max = temperature.max();

                if (max - min > 1) {
                    long mid = (min + max) / 2;
                    MultiNoiseUtil.NoiseHypercube savannaCube = new MultiNoiseUtil.NoiseHypercube(
                            new MultiNoiseUtil.ParameterRange(min, mid),
                            cube.humidity(), cube.continentalness(),
                            cube.erosion(), cube.depth(),
                            cube.weirdness(),
                            cube.offset()
                    );
                    MultiNoiseUtil.NoiseHypercube rustyCube = new MultiNoiseUtil.NoiseHypercube(
                            new MultiNoiseUtil.ParameterRange(mid, max),
                            cube.humidity(), cube.continentalness(),
                            cube.erosion(), cube.depth(),
                            cube.weirdness(),
                            cube.offset()
                    );
                    originalConsumer.accept(Pair.of(savannaCube, BiomeKeys.SAVANNA));
                    originalConsumer.accept(Pair.of(rustyCube, ModBiomes.RUSTY_SAVANNA));
                    return;
                }
            }

            originalConsumer.accept(pair);
        };
    }
}
