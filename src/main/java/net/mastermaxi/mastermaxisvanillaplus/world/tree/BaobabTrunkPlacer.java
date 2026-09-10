package net.mastermaxi.mastermaxisvanillaplus.world.tree;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
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

        // 1. Ensure solid dirt support under the main 2x2 trunk
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                setToDirt(world, replacer, random, startPos.add(x, -1, z), config);
            }
        }

        // 2. Generate the main 2x2 massive column
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

        // 3. Thick buttress base flares around the trunk
        int[][] flareOffsets = new int[][]{
                {-1, 0}, {-1, 1},
                {2, 0}, {2, 1},
                {0, -1}, {1, -1},
                {0, 2}, {1, 2},
                {-1, -1}, {2, 2}, {-1, 2}, {2, -1}
        };

        for (int[] offset : flareOffsets) {
            int flareHeight = random.nextInt(4) + 1; // 1 to 4 blocks
            BlockPos basePos = startPos.add(offset[0], 0, offset[1]);

            setToDirt(world, replacer, random, basePos.down(), config);

            for (int y = 0; y < flareHeight; y++) {
                BlockPos pos = basePos.up(y);
                if (TreeFeature.isAirOrLeaves(world, pos)) {
                    this.getAndSetState(world, replacer, random, pos, config);
                }
            }
        }

        int topY = startPos.getY() + height;

        // 4. Central canopy nodes at the top of the 2x2 trunk
        nodes.add(new FoliagePlacer.TreeNode(new BlockPos(startPos.getX(), topY, startPos.getZ()), 1, true));
        nodes.add(new FoliagePlacer.TreeNode(new BlockPos(startPos.getX() + 1, topY, startPos.getZ() + 1), 1, true));
        nodes.add(new FoliagePlacer.TreeNode(new BlockPos(startPos.getX(), topY + 1, startPos.getZ() + 1), 1, true));
        nodes.add(new FoliagePlacer.TreeNode(new BlockPos(startPos.getX() + 1, topY + 1, startPos.getZ()), 1, true));

        // 5. Expansive, broad branching crown in 8 directions (cardinal & diagonal)
        // Branches start from the upper portion of the trunk (topY - 2 to topY - 1)
        int[][] branchDirs = new int[][]{
                // Cardinal directions (dx, dz): length 3 to 5 blocks
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                // Diagonal directions: length 3 to 4 blocks
                {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
        };

        for (int[] dir : branchDirs) {
            boolean isDiagonal = (dir[0] != 0 && dir[1] != 0);
            int branchLength = isDiagonal ? (2 + random.nextInt(3)) : (3 + random.nextInt(3)); // 2-4 or 3-5

            // Root of branch originates from the matching 2x2 corner
            int startOffsetX = (dir[0] >= 0) ? 1 : 0;
            int startOffsetZ = (dir[1] >= 0) ? 1 : 0;
            int branchStartY = topY - 2 + random.nextInt(2); // topY - 2 or topY - 1

            BlockPos currentPos = new BlockPos(startPos.getX() + startOffsetX, branchStartY, startPos.getZ() + startOffsetZ);

            for (int b = 1; b <= branchLength; b++) {
                // Step outward, with upward steps to create majestic upward-sweeping boughs
                int dy = (b == 2 || b == 4) ? 1 : 0;
                currentPos = currentPos.add(dir[0], dy, dir[1]);

                if (TreeFeature.isAirOrLeaves(world, currentPos)) {
                    this.getAndSetState(world, replacer, random, currentPos, config);
                }

                // Intermediate foliage node on longer branches
                if (b == branchLength / 2 && branchLength >= 4) {
                    nodes.add(new FoliagePlacer.TreeNode(currentPos.up(), 1, false));
                }

                // Terminal foliage node at branch tip
                if (b == branchLength) {
                    nodes.add(new FoliagePlacer.TreeNode(currentPos.up(), 1, false));

                    // Secondary twig fork at the branch tip for even broader foliage coverage
                    int forkDx = (dir[1] != 0) ? (random.nextBoolean() ? 1 : -1) : 0;
                    int forkDz = (dir[0] != 0) ? (random.nextBoolean() ? 1 : -1) : 0;
                    BlockPos twigPos = currentPos.add(forkDx, 1, forkDz);
                    if (TreeFeature.isAirOrLeaves(world, twigPos)) {
                        this.getAndSetState(world, replacer, random, twigPos, config);
                        nodes.add(new FoliagePlacer.TreeNode(twigPos.up(), 0, false));
                    }
                }
            }
        }

        return nodes;
    }
}