package net.mastermaxi.mastermaxisvanillaplus.world.feature;

import com.mojang.serialization.Codec;
import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class ScorchedMudstoneDepositFeature extends Feature<DefaultFeatureConfig> {
    public ScorchedMudstoneDepositFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();

        // Search downwards to find solid ground
        while (origin.getY() > world.getBottomY() && (world.isAir(origin) || !world.getFluidState(origin).isEmpty())) {
            origin = origin.down();
        }
        if (origin.getY() <= world.getBottomY() + 4) {
            return false;
        }

        // Base dimensions: 3x3 to 6x6, height 4 to 7 blocks
        int height = 4 + random.nextInt(4); // 4 to 7
        double baseRadius = 1.7 + (random.nextDouble() * 1.5); // 1.7 to 3.2

        // Internal core behavior (only for formations thick enough to enclose safely):
        // Decreased termite chance (~7%), rare lava pocket inside (~18%)
        boolean hasLavaCore = false;
        boolean hasTermiteNest = false;

        if (baseRadius >= 2.2) {
            float innerRoll = random.nextFloat();
            if (innerRoll < 0.18F) {
                hasLavaCore = true; // 18% chance: Molten lava core sealed inside
            } else if (innerRoll < 0.25F) {
                hasTermiteNest = true; // 7% chance: Small surviving termite nest
            }
        }

        BlockState mudstone = Blocks.MUDSTONE_BLOCK.getDefaultState();
        BlockState coalOre = Blocks.MUDSTONE_COAL_ORE.getDefaultState();
        BlockState flintOre = Blocks.MUDSTONE_FLINT_ORE.getDefaultState();
        BlockState ironOre = Blocks.MUDSTONE_IRON_ORE.getDefaultState();
        BlockState copperOre = Blocks.MUDSTONE_COPPER_ORE.getDefaultState();
        BlockState lava = net.minecraft.block.Blocks.LAVA.getDefaultState();
        BlockState mound = Blocks.MOUND.getDefaultState();
        BlockState infectedMound = Blocks.INFECTED_MOUND.getDefaultState();

        for (int dy = 0; dy < height; dy++) {
            double progress = (double) dy / (double) height;
            double radius = Math.max(0.7, baseRadius * (1.0 - (progress * 0.70)));
            double radiusSq = radius * radius;

            int rCeil = (int) Math.ceil(radius);

            for (int dx = -rCeil; dx <= rCeil; dx++) {
                for (int dz = -rCeil; dz <= rCeil; dz++) {
                    double distSq = (dx * dx) + (dz * dz);
                    double noise = (random.nextDouble() * 0.5) - 0.25;
                    if (distSq <= radiusSq + noise) {
                        BlockPos target = origin.add(dx, dy + 1, dz);

                        // Strictly inner core:
                        // Must be at least 1.35 blocks inside outer wall, and between dy 1 and height - 2,
                        // guaranteeing solid rock on the floor, top, and outer boundary so lava never leaks!
                        double distFromCenter = Math.sqrt(distSq);
                        boolean isInnerCore = (hasLavaCore || hasTermiteNest)
                                && (dy >= 1 && dy <= height - 2)
                                && (distFromCenter <= (radius - 1.35));

                        if (isInnerCore) {
                            if (hasLavaCore) {
                                world.setBlockState(target, lava, Block.NOTIFY_LISTENERS);
                            } else {
                                boolean infected = random.nextFloat() < 0.20F;
                                world.setBlockState(target, infected ? infectedMound : mound, Block.NOTIFY_LISTENERS);
                            }
                        } else {
                            // Substantially increased iron ore concentration (~15% iron)
                            float oreRoll = random.nextFloat();
                            BlockState stoneState;
                            if (oreRoll < 0.15F) {
                                stoneState = ironOre; // ~15.0% iron ore (high richness)
                            } else if (oreRoll < 0.19F) {
                                stoneState = coalOre; // ~4.0% coal
                            } else if (oreRoll < 0.22F) {
                                stoneState = copperOre; // ~3.0% copper
                            } else if (oreRoll < 0.24F) {
                                stoneState = flintOre; // ~2.0% flint
                            } else {
                                stoneState = mudstone; // 76.0% mudstone
                            }
                            world.setBlockState(target, stoneState, Block.NOTIFY_LISTENERS);
                        }

                        // Ensure solid foundation underneath on steep slopes or ravines
                        if (dy == 0) {
                            BlockPos under = target.down();
                            for (int underDy = 0; underDy < 4; underDy++) {
                                BlockPos underPos = under.down(underDy);
                                if (world.isAir(underPos) || !world.getFluidState(underPos).isEmpty()) {
                                    world.setBlockState(underPos, mudstone, Block.NOTIFY_LISTENERS);
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
