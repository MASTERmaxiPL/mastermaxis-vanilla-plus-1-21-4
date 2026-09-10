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

public class MudstoneDepositFeature extends Feature<DefaultFeatureConfig> {
    public MudstoneDepositFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();

        // Find ground level
        while (origin.getY() > world.getBottomY() && (world.isAir(origin) || !world.getFluidState(origin).isEmpty())) {
            origin = origin.down();
        }
        if (origin.getY() <= world.getBottomY() + 4) {
            return false;
        }

        // Base: 4x4 to 8x8 (radius 1.9 to 3.8), height up to 8 (5 to 8)
        int height = 5 + random.nextInt(4); // 5 to 8
        double baseRadius = 1.9 + (random.nextDouble() * 1.9); // 1.9 to 3.8

        // Termite colony nested strictly inside the internal core of the deposit (approx 30% chance)
        // Only if the pillar is thick enough to have a solid outer rock shell
        boolean hasTermiteNest = (random.nextFloat() < 0.30F) && (baseRadius >= 2.4);
        int nestMinY = 1;
        int nestMaxY = Math.max(1, height - 3);

        BlockState mudstone = Blocks.MUDSTONE_BLOCK.getDefaultState();
        BlockState coalOre = Blocks.MUDSTONE_COAL_ORE.getDefaultState();
        BlockState flintOre = Blocks.MUDSTONE_FLINT_ORE.getDefaultState();
        BlockState ironOre = Blocks.MUDSTONE_IRON_ORE.getDefaultState();
        BlockState copperOre = Blocks.MUDSTONE_COPPER_ORE.getDefaultState();
        BlockState mound = Blocks.MOUND.getDefaultState();
        BlockState infectedMound = Blocks.INFECTED_MOUND.getDefaultState();

        for (int dy = 0; dy < height; dy++) {
            double progress = (double) dy / (double) height;
            double radius = Math.max(0.8, baseRadius * (1.0 - (progress * 0.70)));
            double radiusSq = radius * radius;

            int rCeil = (int) Math.ceil(radius);

            for (int dx = -rCeil; dx <= rCeil; dx++) {
                for (int dz = -rCeil; dz <= rCeil; dz++) {
                    double distSq = (dx * dx) + (dz * dz);
                    double noise = (random.nextDouble() * 0.5) - 0.25;
                    if (distSq <= radiusSq + noise) {
                        BlockPos target = origin.add(dx, dy + 1, dz);

                        // Strictly inner core: must be at least 1.25 blocks away from the pillar's outer radius
                        // to guarantee that the outer shell is 100% solid stone/ore, never exposing termite blocks on the outside!
                        double distFromCenter = Math.sqrt(distSq);
                        boolean isInnerCore = hasTermiteNest
                                && (dy >= nestMinY && dy <= nestMaxY)
                                && (distFromCenter <= (radius - 1.25));

                        if (isInnerCore) {
                            boolean infected = random.nextFloat() < 0.25F;
                            world.setBlockState(target, infected ? infectedMound : mound, Block.NOTIFY_LISTENERS);
                        } else {
                            // Reduced ore rates: ~11.5% total ores
                            float oreRoll = random.nextFloat();
                            BlockState stoneState;
                            if (oreRoll < 0.045F) {
                                stoneState = coalOre; // ~4.5% coal
                            } else if (oreRoll < 0.075F) {
                                stoneState = copperOre; // ~3.0% copper
                            } else if (oreRoll < 0.100F) {
                                stoneState = flintOre; // ~2.5% flint
                            } else if (oreRoll < 0.115F) {
                                stoneState = ironOre; // ~1.5% iron
                            } else {
                                stoneState = mudstone; // 88.5% mudstone
                            }
                            world.setBlockState(target, stoneState, Block.NOTIFY_LISTENERS);
                        }

                        // Foundation on slopes
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
