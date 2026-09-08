package net.mastermaxi.mastermaxisvanillaplus.world.tree;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class BaobabTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<BaobabTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> fillTrunkPlacerFields(instance).apply(instance, BaobabTrunkPlacer::new)
    );

    public static final TrunkPlacerType<BaobabTrunkPlacer> BAOBAB_TRUNK_PLACER = Registry.register(
            Registries.TRUNK_PLACER_TYPE,
            MASTERmaxisVanillaPlus.id("baobab_trunk_placer"),
            new TrunkPlacerType<>(CODEC)
    );

    public BaobabTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return BAOBAB_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(
            TestableWorld world,
            BiConsumer<BlockPos, BlockState> replacer,
            Random random,
            int height,
            BlockPos startPos,
            TreeFeatureConfig config
    ) {
        List<FoliagePlacer.TreeNode> nodes = Lists.newArrayList();

        // Ensure dirt under the main 2x2 trunk
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                setToDirt(world, replacer, random, startPos.add(x, -1, z), config);
            }
        }

        // Generate main 2x2 trunk
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < 2; x++) {
                for (int z = 0; z < 2; z++) {
                    BlockPos pos = startPos.add(x, y, z);
                    if (TreeFeature.isAirOrLeaves(world, pos)) {
                        this.getAndSetState(world, replacer, random, pos, config);
                    }
                }
            }
        }

        // Generate irregular thicker base up to 3 blocks high around the 2x2 core
        int[][] flareOffsets = new int[][]{
                {-1, 0}, {-1, 1},
                {2, 0}, {2, 1},
                {0, -1}, {1, -1},
                {0, 2}, {1, 2}
        };

        for (int[] offset : flareOffsets) {
            int flareHeight = random.nextInt(3) + 1; // 1 to 3 blocks
            BlockPos basePos = startPos.add(offset[0], 0, offset[1]);

            // Ensure soil below to avoid floating wood
            setToDirt(world, replacer, random, basePos.down(), config);

            for (int y = 0; y < flareHeight; y++) {
                BlockPos pos = basePos.up(y);
                if (TreeFeature.isAirOrLeaves(world, pos)) {
                    this.getAndSetState(world, replacer, random, pos, config);
                }
            }
        }

        // Top canopy node at the center of the 2x2 trunk
        int topY = startPos.getY() + height;
        nodes.add(new FoliagePlacer.TreeNode(new BlockPos(startPos.getX(), topY, startPos.getZ()), 0, true));

        // Short branching at the top for wide baobab canopy
        for (Direction direction : Direction.Type.HORIZONTAL) {
            int branchLength = 1 + random.nextInt(2);
            BlockPos branchPos = new BlockPos(startPos.getX(), topY - 1, startPos.getZ());
            for (int b = 1; b <= branchLength; b++) {
                BlockPos pos = branchPos.offset(direction, b).up(b > 1 ? 1 : 0);
                this.getAndSetState(world, replacer, random, pos, config);
                if (b == branchLength) {
                    nodes.add(new FoliagePlacer.TreeNode(pos.up(), 0, false));
                }
            }
        }

        return nodes;
    }
}
