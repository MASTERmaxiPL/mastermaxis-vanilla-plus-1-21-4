package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.mastermaxi.mastermaxisvanillaplus.world.biome.ModBiomes;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SurfaceBuilder.class)
public abstract class SurfaceBuilderMixin {
    @ModifyVariable(method = "buildSurface", at = @At("HEAD"), argsOnly = true)
    private MaterialRules.MaterialRule mastermaxisvanillaplus(MaterialRules.MaterialRule originalRule) {
        MaterialRules.MaterialRule mudstone = MaterialRules.block(Blocks.MUDSTONE_BLOCK.getDefaultState());
        MaterialRules.MaterialRule redSoil = MaterialRules.block(Blocks.RED_SOIL.getDefaultState());
        MaterialRules.MaterialRule grassyRedSoil = MaterialRules.block(Blocks.GRASSY_RED_SOIL.getDefaultState());
        MaterialRules.MaterialRule grassBlock = MaterialRules.block(net.minecraft.block.Blocks.GRASS_BLOCK.getDefaultState());
        MaterialRules.MaterialRule coarseDirt = MaterialRules.block(net.minecraft.block.Blocks.COARSE_DIRT.getDefaultState());

        // 0. Rusty Savanna:
        // Surface floor: predominantly Grassy Red Soil (Red Soil Grass), with small patches of Grass Block
        // and sporadic patches of Coarse Dirt peppered naturally between both
        // Subsurface floor (depth 1 to 3): pure Red Soil
        // Rock layer (depth 4 to 6): Mudstone
        // Steep slopes: Mudstone
        MaterialRules.MaterialRule surfacePatches = MaterialRules.sequence(
                // Sporadic patches of coarse dirt appearing naturally between grassy red soil and regular grass
                MaterialRules.condition(MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE_SECONDARY, 1.6 / 8.25, Double.MAX_VALUE), coarseDirt),
                // Natural patches of regular grass block
                MaterialRules.condition(MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE, -0.25 / 8.25, 0.12 / 8.25), grassBlock),
                // Predominant surface: Grassy Red Soil (reverting to Red Soil under water)
                MaterialRules.condition(MaterialRules.water(0, 0), grassyRedSoil),
                redSoil
        );

        MaterialRules.MaterialRule rustySavannaRule = MaterialRules.condition(
                MaterialRules.biome(ModBiomes.RUSTY_SAVANNA),
                MaterialRules.sequence(
                        MaterialRules.condition(MaterialRules.steepSlope(), mudstone),
                        MaterialRules.condition(
                                MaterialRules.surface(),
                                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, surfacePatches)
                        ),
                        MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH, redSoil),
                        MaterialRules.condition(
                                MaterialRules.aboveY(YOffset.fixed(58), 0),
                                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH_RANGE_6, mudstone)
                        )
                )
        );

        // 1. Savanna Plateau (and Windswept Savanna):
        // The entire plateau above Y=60:
        // - Steep slopes -> mudstone
        // - Surface floor -> grass block, then 3 dirt blocks
        // - ALL other rock and subsurface on the plateau -> mudstone! (Never allows vanilla stone on these levels)
        MaterialRules.MaterialRule savannaPlateauRule = MaterialRules.condition(
                MaterialRules.biome(BiomeKeys.SAVANNA_PLATEAU, BiomeKeys.WINDSWEPT_SAVANNA),
                MaterialRules.condition(
                        MaterialRules.aboveY(YOffset.fixed(60), 0),
                        MaterialRules.sequence(
                                MaterialRules.condition(MaterialRules.steepSlope(), mudstone),
                                MaterialRules.condition(
                                        MaterialRules.surface(),
                                        MaterialRules.condition(
                                                MaterialRules.STONE_DEPTH_FLOOR,
                                                MaterialRules.sequence(
                                                        MaterialRules.condition(
                                                                MaterialRules.water(0, 0),
                                                                MaterialRules.block(net.minecraft.block.Blocks.GRASS_BLOCK.getDefaultState())
                                                        ),
                                                        MaterialRules.block(net.minecraft.block.Blocks.DIRT.getDefaultState())
                                                )
                                        )
                                ),
                                mudstone
                        )
                )
        );

        // 2. Steep slopes in regular Savanna generate mudstone instead of stone
        MaterialRules.MaterialRule savannaSteepRule = MaterialRules.condition(
                MaterialRules.biome(BiomeKeys.SAVANNA),
                MaterialRules.condition(MaterialRules.steepSlope(), mudstone)
        );

        // 3. Subsurface rock in regular Savanna:
        // Only generates ~4-6 levels of mudstone directly under the dirt floor,
        // leaving all deeper layers as regular stone!
        MaterialRules.MaterialRule regularSavannaSubsurface = MaterialRules.condition(
                MaterialRules.biome(BiomeKeys.SAVANNA),
                MaterialRules.condition(
                        MaterialRules.aboveY(YOffset.fixed(58), 0),
                        MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH_RANGE_6, mudstone)
                )
        );

        return MaterialRules.sequence(
                rustySavannaRule,
                savannaPlateauRule,
                savannaSteepRule,
                originalRule,
                regularSavannaSubsurface
        );
    }
}