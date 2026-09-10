package net.mastermaxi.mastermaxisvanillaplus.world.feature;

import com.mojang.serialization.Codec;
import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.mastermaxi.mastermaxisvanillaplus.fluid.FluidInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MudPoolFeature extends Feature<DefaultFeatureConfig> {
    public MudPoolFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    private static class Lobe {
        final double cx;
        final double cz;
        final double rx;
        final double rz;
        final double cosA;
        final double sinA;

        Lobe(double cx, double cz, double rx, double rz, double angle) {
            this.cx = cx;
            this.cz = cz;
            this.rx = rx;
            this.rz = rz;
            this.cosA = Math.cos(angle);
            this.sinA = Math.sin(angle);
        }

        double evaluate(double x, double z) {
            double dx = x - cx;
            double dz = z - cz;
            double rotX = dx * cosA + dz * sinA;
            double rotZ = -dx * sinA + dz * cosA;
            double distSq = (rotX * rotX) / (rx * rx) + (rotZ * rotZ) / (rz * rz);
            if (distSq >= 1.0) {
                return 0.0;
            }
            double term = 1.0 - distSq;
            return term * term;
        }
    }

    private int getGroundY(StructureWorldAccess world, int x, int z, int approxY) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(x, approxY + 3, z);
        while (mutable.getY() >= approxY - 4 && (world.isAir(mutable) || world.getBlockState(mutable).isReplaceable() || !world.getFluidState(mutable).isEmpty())) {
            mutable.move(Direction.DOWN);
        }
        return mutable.getY();
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();

        // 1. Locate the solid ground surface
        int groundY = getGroundY(world, origin.getX(), origin.getZ(), origin.getY());
        if (groundY <= world.getBottomY() + 4) {
            return false;
        }

        // 2. Generate multi-lobe metaball structure for irregular organic contours (up to 10x10 footprint)
        List<Lobe> lobes = new ArrayList<>();

        // Central main lobe
        double r0x = 2.4 + (random.nextDouble() * 0.9);
        double r0z = 2.4 + (random.nextDouble() * 0.9);
        double a0 = random.nextDouble() * Math.PI;
        lobes.add(new Lobe(0.0, 0.0, r0x, r0z, a0));

        // 2 to 3 satellite lobes at irregular angles and distances
        int satelliteCount = 2 + random.nextInt(2);
        for (int i = 0; i < satelliteCount; i++) {
            double baseAngle = (i * 2.0 * Math.PI / satelliteCount) + ((random.nextDouble() * 0.8) - 0.4);
            double dist = 1.8 + (random.nextDouble() * 1.1); // 1.8 to 2.9 blocks out
            double cx = dist * Math.cos(baseAngle);
            double cz = dist * Math.sin(baseAngle);
            double rx = 1.7 + (random.nextDouble() * 0.9);
            double rz = 1.6 + (random.nextDouble() * 0.9);
            double angle = random.nextDouble() * Math.PI;
            lobes.add(new Lobe(cx, cz, rx, rz, angle));
        }

        // Harmonic noise parameters to break discrete grid symmetry and round out/indent edges organically
        double phase1 = random.nextDouble() * 2.0 * Math.PI;
        double phase2 = random.nextDouble() * 2.0 * Math.PI;

        Set<BlockPos> waterCols = new HashSet<>();
        Set<BlockPos> mudCols = new HashSet<>();

        // Sample across up to 10x10 space (-5 to +5)
        int scanRadius = 5;
        for (int dx = -scanRadius; dx <= scanRadius; dx++) {
            for (int dz = -scanRadius; dz <= scanRadius; dz++) {
                double totalLobe = 0.0;
                for (Lobe lobe : lobes) {
                    totalLobe += lobe.evaluate(dx, dz);
                }

                if (totalLobe <= 0.0) {
                    continue;
                }

                double angle = Math.atan2(dz, dx);
                double harmonic = 1.0 + (0.16 * Math.cos(3.0 * angle + phase1)) + (0.12 * Math.sin(2.0 * angle + phase2));
                double field = totalLobe * harmonic;

                int posX = origin.getX() + dx;
                int posZ = origin.getZ() + dz;
                BlockPos colPos = new BlockPos(posX, groundY, posZ);

                if (field >= 0.30) {
                    waterCols.add(colPos);
                } else if (field >= 0.07) {
                    mudCols.add(colPos);
                }
            }
        }

        if (waterCols.size() < 6) {
            return false;
        }

        // 3. Terrain flatness check across the entire proposed pool footprint
        for (BlockPos col : waterCols) {
            int ly = getGroundY(world, col.getX(), col.getZ(), groundY);
            if (Math.abs(ly - groundY) > 1 || !world.getFluidState(new BlockPos(col.getX(), ly, col.getZ())).isEmpty()) {
                return false;
            }
        }
        for (BlockPos col : mudCols) {
            int ly = getGroundY(world, col.getX(), col.getZ(), groundY);
            if (Math.abs(ly - groundY) > 1 || !world.getFluidState(new BlockPos(col.getX(), ly, col.getZ())).isEmpty()) {
                return false;
            }
        }

        // 4. Strict containment: every cardinal neighbor of water must be solid mud (leak-proof casing)
        for (BlockPos wCol : waterCols) {
            for (Direction dir : Direction.Type.HORIZONTAL) {
                BlockPos nCol = wCol.offset(dir);
                if (!waterCols.contains(nCol)) {
                    mudCols.add(nCol);
                }
            }
        }

        // 5. Water depth: max 1-2 blocks deep, surface flush with ground level
        int waterDepth = random.nextInt(2) + 1; // 1 or 2 blocks deep
        int waterBottomY = groundY - waterDepth + 1;

        BlockState mudState = Blocks.MUD_BLOCK.getDefaultState();
        BlockState mudWaterState = FluidInit.MUD_WATER_BLOCK.getDefaultState();
        BlockState airState = net.minecraft.block.Blocks.AIR.getDefaultState();

        // 6. Place mud casing and shoreline
        for (BlockPos col : mudCols) {
            if (waterCols.contains(col)) {
                continue;
            }
            int localY = getGroundY(world, col.getX(), col.getZ(), groundY);
            int topY = Math.max(groundY, localY);

            // Mud wall from below pool bed up to ground surface level (flush with terrain)
            for (int y = waterBottomY - 1; y <= topY; y++) {
                world.setBlockState(new BlockPos(col.getX(), y, col.getZ()), mudState, Block.NOTIFY_LISTENERS);
            }

            // Clear any foliage/plants directly above the mud surface
            BlockPos above1 = new BlockPos(col.getX(), topY + 1, col.getZ());
            if (world.getBlockState(above1).isReplaceable()) {
                world.setBlockState(above1, airState, Block.NOTIFY_LISTENERS);
            }
            BlockPos above2 = new BlockPos(col.getX(), topY + 2, col.getZ());
            if (world.getBlockState(above2).isReplaceable()) {
                world.setBlockState(above2, airState, Block.NOTIFY_LISTENERS);
            }

            // Ensure foundation under mud blocks down 1-2 blocks if air or liquid
            for (int under = 1; under <= 2; under++) {
                BlockPos underPos = new BlockPos(col.getX(), waterBottomY - 1 - under, col.getZ());
                if (world.isAir(underPos) || !world.getFluidState(underPos).isEmpty()) {
                    world.setBlockState(underPos, mudState, Block.NOTIFY_LISTENERS);
                } else {
                    break;
                }
            }
        }

        // 7. Place water and mud bed directly underneath
        for (BlockPos col : waterCols) {
            // Mud floor directly under water
            world.setBlockState(new BlockPos(col.getX(), waterBottomY - 1, col.getZ()), mudState, Block.NOTIFY_LISTENERS);

            // Mud water fluid columns
            for (int y = waterBottomY; y <= groundY; y++) {
                world.setBlockState(new BlockPos(col.getX(), y, col.getZ()), mudWaterState, Block.NOTIFY_LISTENERS);
            }

            // Clear foliage directly above the water surface
            BlockPos above1 = new BlockPos(col.getX(), groundY + 1, col.getZ());
            if (world.getBlockState(above1).isReplaceable()) {
                world.setBlockState(above1, airState, Block.NOTIFY_LISTENERS);
            }
            BlockPos above2 = new BlockPos(col.getX(), groundY + 2, col.getZ());
            if (world.getBlockState(above2).isReplaceable()) {
                world.setBlockState(above2, airState, Block.NOTIFY_LISTENERS);
            }

            // Ensure foundation under pool bed
            for (int under = 1; under <= 2; under++) {
                BlockPos underPos = new BlockPos(col.getX(), waterBottomY - 1 - under, col.getZ());
                if (world.isAir(underPos) || !world.getFluidState(underPos).isEmpty()) {
                    world.setBlockState(underPos, mudState, Block.NOTIFY_LISTENERS);
                } else {
                    break;
                }
            }
        }

        return true;
    }
}
