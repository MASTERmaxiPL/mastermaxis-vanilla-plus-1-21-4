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

public class TermiteMoundFeature extends Feature<DefaultFeatureConfig> {
    public TermiteMoundFeature(Codec<DefaultFeatureConfig> configCodec) {
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

        // Avoid generating on or directly adjacent to mudstone formations
        for (int cx = -3; cx <= 3; cx++) {
            for (int cz = -3; cz <= 3; cz++) {
                BlockState nearby = world.getBlockState(origin.add(cx, 0, cz));
                if (nearby.isOf(Blocks.MUDSTONE_BLOCK) || nearby.isOf(Blocks.MUDSTONE_COAL_ORE)
                        || nearby.isOf(Blocks.MUDSTONE_FLINT_ORE) || nearby.isOf(Blocks.MUDSTONE_IRON_ORE)) {
                    return false;
                }
            }
        }

        // Height: up to 6 blocks (3 to 6)
        int height = 3 + random.nextInt(4);
        // Base radius: 1.5 to 2.2 (diameter 3x3 to 5x5 max)
        double baseRadius = 1.4 + (random.nextDouble() * 0.9);

        BlockState moundState = Blocks.MOUND.getDefaultState();
        BlockState infectedMoundState = Blocks.INFECTED_MOUND.getDefaultState();

        int infectedPlaced = 0;

        for (int dy = 0; dy < height; dy++) {
            // Taper upwards
            double progress = (double) dy / (double) height;
            double radius = Math.max(0.6, baseRadius * (1.0 - (progress * 0.75)));
            double radiusSq = radius * radius;

            int rCeil = (int) Math.ceil(radius);

            for (int dx = -rCeil; dx <= rCeil; dx++) {
                for (int dz = -rCeil; dz <= rCeil; dz++) {
                    double distSq = (dx * dx) + (dz * dz);
                    // Add slight organic noise to shape
                    double noise = (random.nextDouble() * 0.4) - 0.2;
                    if (distSq <= radiusSq + noise) {
                        BlockPos target = origin.add(dx, dy + 1, dz);
                        // Decide between infected mound (approx 20%) and regular mound
                        boolean infected = random.nextFloat() < 0.20F;
                        if (infected) {
                            infectedPlaced++;
                        }
                        world.setBlockState(target, infected ? infectedMoundState : moundState, Block.NOTIFY_LISTENERS);

                        // Ensure solid foundation underneath if on uneven terrain
                        if (dy == 0) {
                            BlockPos under = target.down();
                            for (int underDy = 0; underDy < 3; underDy++) {
                                BlockPos underPos = under.down(underDy);
                                if (world.isAir(underPos) || !world.getFluidState(underPos).isEmpty()) {
                                    world.setBlockState(underPos, moundState, Block.NOTIFY_LISTENERS);
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        // Ensure at least one infected mound exists in the colony
        if (infectedPlaced == 0) {
            world.setBlockState(origin.up(), infectedMoundState, Block.NOTIFY_LISTENERS);
        }

        return true;
    }
}
