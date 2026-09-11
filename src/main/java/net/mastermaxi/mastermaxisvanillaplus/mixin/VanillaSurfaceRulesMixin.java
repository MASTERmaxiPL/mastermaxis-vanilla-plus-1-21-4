package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VanillaSurfaceRules.class)
public abstract class VanillaSurfaceRulesMixin {
    @Inject(method = "createDefaultRule", at = @At("RETURN"), cancellable = true)
    private static void mastermaxisvanillaplus$injectSavannaMudstone(
            boolean surface,
            boolean bedrockRoof,
            boolean bedrockFloor,
            CallbackInfoReturnable<MaterialRules.MaterialRule> cir) {
        if (!surface) {
            return;
        }

        MaterialRules.MaterialRule original = cir.getReturnValue();
        MaterialRules.MaterialRule mudstone = MaterialRules.block(Blocks.MUDSTONE_BLOCK.getDefaultState());

        // Steep cliff faces in Savanna, Savanna Plateau, and Windswept Savanna generate mudstone instead of stone
        MaterialRules.MaterialRule savannaSteepRule = MaterialRules.condition(
                MaterialRules.biome(BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU, BiomeKeys.WINDSWEPT_SAVANNA),
                MaterialRules.condition(MaterialRules.steepSlope(), mudstone)
        );

        // Subsurface rock layer in Savanna biomes:
        // Replaces stone under the dirt floor (within 30 blocks of surface or above Y=60 sea level)
        // and on cave ceilings / overhangs.
        // NOTE: We do NOT wrap this in MaterialRules.surface() because surface() only matches blocks at or above surface height!
        MaterialRules.MaterialRule savannaSubsurfaceRule = MaterialRules.condition(
                MaterialRules.biome(BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU, BiomeKeys.WINDSWEPT_SAVANNA),
                MaterialRules.sequence(
                        MaterialRules.condition(MaterialRules.STONE_DEPTH_CEILING_WITH_SURFACE_DEPTH, mudstone),
                        MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH_RANGE_30, mudstone),
                        MaterialRules.condition(MaterialRules.aboveY(YOffset.fixed(60), 0), mudstone)
                )
        );

        // Sequence:
        // 1. Check steep cliffs first (replaces exposed stone on cliffs with mudstone)
        // 2. Run original rules (places grass & dirt floor at the surface)
        // 3. For all remaining subsurface rock in savanna, replace stone with mudstone
        cir.setReturnValue(MaterialRules.sequence(savannaSteepRule, original, savannaSubsurfaceRule));
    }
}
