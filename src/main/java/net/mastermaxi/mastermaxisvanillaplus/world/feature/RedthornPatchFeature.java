package net.mastermaxi.mastermaxisvanillaplus.world.feature;

import com.mojang.serialization.Codec;
import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.List;

public class RedthornPatchFeature extends Feature<DefaultFeatureConfig> {
    public RedthornPatchFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();

        // 1. Group size: 3 to 8 blocks in irregular cluster
        int targetCount = 3 + random.nextInt(6);

        // Find initial surface block at origin
        BlockPos startPos = findSurface(world, origin);
        if (startPos == null) {
            return false;
        }

        BlockState redthorn = Blocks.REDTHORN.getDefaultState();
        if (!redthorn.canPlaceAt(world, startPos)) {
            return false;
        }

        List<BlockPos> placed = new ArrayList<>();
        world.setBlockState(startPos, redthorn, 2);
        placed.add(startPos);

        // Grow cluster organically: select an existing block and step in a random direction
        int attempts = 0;
        while (placed.size() < targetCount && attempts < 40) {
            attempts++;
            BlockPos anchor = placed.get(random.nextInt(placed.size()));
            Direction dir = Direction.Type.HORIZONTAL.random(random);
            int stepDist = random.nextInt(3) == 0 ? 2 : 1; // 1 or 2 blocks step for irregular shape
            BlockPos nextColumn = anchor.offset(dir, stepDist);

            BlockPos candidate = findSurface(world, nextColumn);
            if (candidate != null && !placed.contains(candidate) && redthorn.canPlaceAt(world, candidate)) {
                world.setBlockState(candidate, redthorn, 2);
                placed.add(candidate);
            }
        }

        return !placed.isEmpty();
    }

    private BlockPos findSurface(StructureWorldAccess world, BlockPos pos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(pos.getX(), pos.getY() + 4, pos.getZ());
        while (mutable.getY() >= pos.getY() - 4 && (world.isAir(mutable) || (world.getBlockState(mutable).isReplaceable() && world.getFluidState(mutable).isEmpty()))) {
            mutable.move(Direction.DOWN);
        }

        // Abort if the ground or stop point is fluid (water, mud water, etc.)
        if (!world.getFluidState(mutable).isEmpty()) {
            return null;
        }

        BlockPos surface = mutable.up();
        // The surface block must be air and completely free of fluid
        if (!world.getFluidState(surface).isEmpty() || !world.isAir(surface)) {
            return null;
        }

        // Also ensure no horizontal neighboring fluid, preventing spawning submerged on shores/waterlines
        for (Direction dir : Direction.Type.HORIZONTAL) {
            if (!world.getFluidState(surface.offset(dir)).isEmpty()) {
                return null;
            }
        }

        return surface.toImmutable();
    }
}
