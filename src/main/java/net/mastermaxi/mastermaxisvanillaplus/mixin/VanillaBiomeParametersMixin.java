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
            // Partition the savanna parameter space across temperature and humidity:
            // 1. Cooler range [minT, savannaEnd] (~40% of temperature): Regular SAVANNA.
            //    Buffers against temperate biomes (Plains, Forest).
            // 2. Warmer range [savannaEnd, maxT] (~60% of temperature): Hot savanna climate,
            //    approaching the boundary with Desert and Badlands.
            //    Partitioned across humidity:
            //    - Wetter side [hSplit, maxH] (borders Jungle): 100% RUSTY_SAVANNA.
            //      Acts as an impassable buffer ensuring Scorched Savanna NEVER touches Jungle.
            //    - Drier side [minH, hSplit] (borders Desert, Badlands, Wooded/Eroded Badlands, Coasts):
            //      * Flat terrain (erosion >= 0.05F / 500L): SCORCHED_SAVANNA (~12-16 chunks, flat terrain).
            //      * Elevated/hilly terrain (erosion < 0.05F / 500L): RUSTY_SAVANNA.
            if (BiomeKeys.SAVANNA.equals(biomeKey)) {
                MultiNoiseUtil.ParameterRange temperature = cube.temperature();
                long minT = temperature.min();
                long maxT = temperature.max();
                long tSpan = maxT - minT;

                if (tSpan <= 10) {
                    originalConsumer.accept(pair);
                    return;
                }

                long savannaEnd = minT + (long) (tSpan * 0.40);
                MultiNoiseUtil.ParameterRange savannaTemp = new MultiNoiseUtil.ParameterRange(minT, savannaEnd);
                MultiNoiseUtil.ParameterRange warmTemp = new MultiNoiseUtil.ParameterRange(savannaEnd, maxT);

                // 1. Regular Savanna: cooler portion of the savanna climate
                MultiNoiseUtil.NoiseHypercube savannaCube = new MultiNoiseUtil.NoiseHypercube(
                        savannaTemp, cube.humidity(), cube.continentalness(),
                        cube.erosion(), cube.depth(), cube.weirdness(), cube.offset()
                );
                originalConsumer.accept(Pair.of(savannaCube, BiomeKeys.SAVANNA));

                // 2. Warmer savanna range [savannaEnd, maxT]:
                MultiNoiseUtil.ParameterRange humidity = cube.humidity();
                long minH = humidity.min();
                long maxH = humidity.max();
                long hSpan = maxH - minH;

                if (hSpan <= 10) {
                    MultiNoiseUtil.NoiseHypercube fallbackCube = new MultiNoiseUtil.NoiseHypercube(
                            warmTemp, cube.humidity(), cube.continentalness(),
                            cube.erosion(), cube.depth(), cube.weirdness(), cube.offset()
                    );
                    originalConsumer.accept(Pair.of(fallbackCube, ModBiomes.RUSTY_SAVANNA));
                    return;
                }

                // Split humidity: drier 50% vs wetter 50%
                long hSplit = minH + (hSpan / 2);
                MultiNoiseUtil.ParameterRange dryHumidity = new MultiNoiseUtil.ParameterRange(minH, hSplit);
                MultiNoiseUtil.ParameterRange wetHumidity = new MultiNoiseUtil.ParameterRange(hSplit, maxH);

                // Wetter half: borders Jungle -> always Rusty Savanna to buffer Scorched Savanna from Jungle
                MultiNoiseUtil.NoiseHypercube jungleBufferCube = new MultiNoiseUtil.NoiseHypercube(
                        warmTemp, wetHumidity, cube.continentalness(),
                        cube.erosion(), cube.depth(), cube.weirdness(), cube.offset()
                );
                originalConsumer.accept(Pair.of(jungleBufferCube, ModBiomes.RUSTY_SAVANNA));

                // Drier half: borders Desert / Badlands / Rusty Savanna / Coasts
                long flatErosionThreshold = 500L; // 0.05F in Minecraft noise scale
                MultiNoiseUtil.ParameterRange erosion = cube.erosion();

                if (erosion.min() >= flatErosionThreshold) {
                    // Completely flat terrain -> Scorched Savanna
                    MultiNoiseUtil.NoiseHypercube scorchedCube = new MultiNoiseUtil.NoiseHypercube(
                            warmTemp, dryHumidity, cube.continentalness(),
                            cube.erosion(), cube.depth(), cube.weirdness(), cube.offset()
                    );
                    originalConsumer.accept(Pair.of(scorchedCube, ModBiomes.SCORCHED_SAVANNA));
                } else if (erosion.max() <= flatErosionThreshold) {
                    // Elevated or hilly terrain -> Rusty Savanna
                    MultiNoiseUtil.NoiseHypercube deepRustyCube = new MultiNoiseUtil.NoiseHypercube(
                            warmTemp, dryHumidity, cube.continentalness(),
                            cube.erosion(), cube.depth(), cube.weirdness(), cube.offset()
                    );
                    originalConsumer.accept(Pair.of(deepRustyCube, ModBiomes.RUSTY_SAVANNA));
                } else {
                    // Split across erosion threshold into hilly (Rusty) and flat (Scorched)
                    MultiNoiseUtil.ParameterRange hillyErosion = new MultiNoiseUtil.ParameterRange(erosion.min(), flatErosionThreshold);
                    MultiNoiseUtil.ParameterRange flatErosion = new MultiNoiseUtil.ParameterRange(flatErosionThreshold, erosion.max());

                    MultiNoiseUtil.NoiseHypercube deepRustyCube = new MultiNoiseUtil.NoiseHypercube(
                            warmTemp, dryHumidity, cube.continentalness(),
                            hillyErosion, cube.depth(), cube.weirdness(), cube.offset()
                    );
                    MultiNoiseUtil.NoiseHypercube scorchedCube = new MultiNoiseUtil.NoiseHypercube(
                            warmTemp, dryHumidity, cube.continentalness(),
                            flatErosion, cube.depth(), cube.weirdness(), cube.offset()
                    );
                    originalConsumer.accept(Pair.of(deepRustyCube, ModBiomes.RUSTY_SAVANNA));
                    originalConsumer.accept(Pair.of(scorchedCube, ModBiomes.SCORCHED_SAVANNA));
                }
                return;
            }

            originalConsumer.accept(pair);
        };
    }
}
