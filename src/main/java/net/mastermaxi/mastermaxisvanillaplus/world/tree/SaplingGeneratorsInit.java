package net.mastermaxi.mastermaxisvanillaplus.world.tree;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.world.ConfiguredFeaturesInit;
import net.minecraft.block.SaplingGenerator;

import java.util.Optional;

public class SaplingGeneratorsInit {
    public static final SaplingGenerator BAOBAB = new SaplingGenerator(
            MASTERmaxisVanillaPlus.MOD_ID + ":baobab_tree",
            Optional.of(ConfiguredFeaturesInit.BAOBAB_TREE),
            Optional.empty(),
            Optional.empty());
}
