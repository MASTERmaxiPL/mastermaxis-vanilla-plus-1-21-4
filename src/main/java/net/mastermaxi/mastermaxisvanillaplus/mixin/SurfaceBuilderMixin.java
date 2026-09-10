package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.YOffset;
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
                savannaPlateauRule,
                savannaSteepRule,
                originalRule,
                regularSavannaSubsurface
        );
    }
}