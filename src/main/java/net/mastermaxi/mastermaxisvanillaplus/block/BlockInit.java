package net.mastermaxi.mastermaxisvanillaplus.block;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.item.ItemGroups;

import static net.mastermaxi.mastermaxisvanillaplus.block.Blocks.*;

public class BlockInit {


    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Initializing blocks for " + MASTERmaxisVanillaPlus.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(BlockInit::modifyEntries);
    }

    private static void modifyEntries(FabricItemGroupEntries entries) {
        entries.add(FEATHER_BLOCK);
        entries.add(MUD_BLOCK);
        entries.add(MUDSTONE_BLOCK);
        entries.add(MUDSTONE_BRICK);
        entries.add(POLISHED_MUDSTONE);
        entries.add(MUDSTONE_COAL_ORE);
        entries.add(MUDSTONE_IRON_ORE);
        entries.add(MUDSTONE_FLINT_ORE);
    }
}
