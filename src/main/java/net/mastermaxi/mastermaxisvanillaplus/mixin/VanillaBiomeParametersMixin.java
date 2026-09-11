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

            // Intercept regular SAVANNA entries:
            // 1. Split by temperature into two contiguous macro-regions:
            //    - [minT, midT] -> regular SAVANNA
            //    - [midT, maxT] -> RUSTY_SAVANNA
            // 2. Carve out SCORCHED_SAVANNA as a localized sub-biome pocket (top 20% of weirdness peaks)
            //    within these savanna regions so it:
            //    - is much smaller and contained
            //    - is ALWAYS directly adjacent to its parent savanna (never alone/isolated)
            if (BiomeKeys.SAVANNA.equals(biomeKey)) {
                MultiNoiseUtil.ParameterRange temperature = cube.temperature();
                long minT = temperature.min();
                long maxT = temperature.max();

                long midT = (minT + maxT) / 2;

                MultiNoiseUtil.ParameterRange savannaTemp = (maxT - minT > 1)
                        ? new MultiNoiseUtil.ParameterRange(minT, midT)
                        : temperature;
                MultiNoiseUtil.ParameterRange rustyTemp = (maxT - minT > 1)
                        ? new MultiNoiseUtil.ParameterRange(midT, maxT)
                        : temperature;

                MultiNoiseUtil.ParameterRange weirdness = cube.weirdness();
                long minW = weirdness.min();
                long maxW = weirdness.max();

                if (maxW - minW > 10) {
                    long wSplit = minW + (long) ((maxW - minW) * 0.80);
                    MultiNoiseUtil.ParameterRange parentWeirdness = new MultiNoiseUtil.ParameterRange(minW, wSplit);
                    MultiNoiseUtil.ParameterRange scorchedWeirdness = new MultiNoiseUtil.ParameterRange(wSplit, maxW);

                    // 1. Regular Savanna + adjacent Scorched Savanna pocket
                    MultiNoiseUtil.NoiseHypercube savannaCube = new MultiNoiseUtil.NoiseHypercube(
                            savannaTemp, cube.humidity(), cube.continentalness(),
                            cube.erosion(), cube.depth(), parentWeirdness, cube.offset()
                    );
                    MultiNoiseUtil.NoiseHypercube scorchedSavannaCube = new MultiNoiseUtil.NoiseHypercube(
                            savannaTemp, cube.humidity(), cube.continentalness(),
                            cube.erosion(), cube.depth(), scorchedWeirdness, cube.offset()
                    );

                    // 2. Rusty Savanna + adjacent Scorched Savanna pocket
                    MultiNoiseUtil.NoiseHypercube rustyCube = new MultiNoiseUtil.NoiseHypercube(
                            rustyTemp, cube.humidity(), cube.continentalness(),
                            cube.erosion(), cube.depth(), parentWeirdness, cube.offset()
                    );
                    MultiNoiseUtil.NoiseHypercube scorchedRustyCube = new MultiNoiseUtil.NoiseHypercube(
                            rustyTemp, cube.humidity(), cube.continentalness(),
                            cube.erosion(), cube.depth(), scorchedWeirdness, cube.offset()
                    );

                    originalConsumer.accept(Pair.of(savannaCube, BiomeKeys.SAVANNA));
                    originalConsumer.accept(Pair.of(scorchedSavannaCube, ModBiomes.SCORCHED_SAVANNA));

                    if (maxT - minT > 1) {
                        originalConsumer.accept(Pair.of(rustyCube, ModBiomes.RUSTY_SAVANNA));
                        originalConsumer.accept(Pair.of(scorchedRustyCube, ModBiomes.SCORCHED_SAVANNA));
                    }
                    return;
                } else {
                    if (maxT - minT > 1) {
                        MultiNoiseUtil.NoiseHypercube savannaCube = new MultiNoiseUtil.NoiseHypercube(
                                savannaTemp, cube.humidity(), cube.continentalness(),
                                cube.erosion(), cube.depth(), cube.weirdness(), cube.offset()
                        );
                        MultiNoiseUtil.NoiseHypercube rustyCube = new MultiNoiseUtil.NoiseHypercube(
                                rustyTemp, cube.humidity(), cube.continentalness(),
                                cube.erosion(), cube.depth(), cube.weirdness(), cube.offset()
                        );
                        originalConsumer.accept(Pair.of(savannaCube, BiomeKeys.SAVANNA));
                        originalConsumer.accept(Pair.of(rustyCube, ModBiomes.RUSTY_SAVANNA));
                        return;
                    }
                }
            }

            originalConsumer.accept(pair);
        };
    }
}
