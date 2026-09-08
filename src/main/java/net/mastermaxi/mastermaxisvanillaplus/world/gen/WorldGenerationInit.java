package net.mastermaxi.mastermaxisvanillaplus.world.gen;

import net.mastermaxi.mastermaxisvanillaplus.world.tree.BaobabTrunkPlacer;

import java.util.Objects;

public class WorldGenerationInit {
    public static void initialize() {
        Objects.requireNonNull(BaobabTrunkPlacer.BAOBAB_TRUNK_PLACER);
    }
}
