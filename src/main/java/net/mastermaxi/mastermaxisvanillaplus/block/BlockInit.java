package net.mastermaxi.mastermaxisvanillaplus.block;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemGroupsInit;

import static net.mastermaxi.mastermaxisvanillaplus.block.Blocks.*;

public class BlockInit {


    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Initializing blocks for " + MASTERmaxisVanillaPlus.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroupsInit.MOD_ITEM_GROUP_KEY).register(BlockInit::modifyEntries);
    }

    private static void modifyEntries(FabricItemGroupEntries entries) {
        entries.add(FEATHER_BLOCK);

        entries.add(MUD_BLOCK);

        entries.add(MUDSTONE_BLOCK);
        entries.add(MUDSTONE_STAIRS);
        entries.add(MUDSTONE_SLAB);
        entries.add(MUDSTONE_WALL);
        entries.add(MUDSTONE_BUTTON);
        entries.add(MUDSTONE_PRESSURE_PLATE);
        entries.add(MUDSTONE_DOOR);

        entries.add(MUDSTONE_BRICK);
        entries.add(MUDSTONE_BRICK_STAIRS);
        entries.add(Blocks.MUDSTONE_BRICK_SLAB);
        entries.add(Blocks.MUDSTONE_BRICK_WALL);
        entries.add(Blocks.MUDSTONE_BRICK_PRESSURE_PLATE);

        entries.add(Blocks.POLISHED_MUDSTONE);
        entries.add(Blocks.POLISHED_MUDSTONE_STAIRS);
        entries.add(Blocks.POLISHED_MUDSTONE_SLAB);
        entries.add(Blocks.POLISHED_MUDSTONE_WALL);

        entries.add(MUDSTONE_COAL_ORE);
        entries.add(MUDSTONE_IRON_ORE);
        entries.add(MUDSTONE_FLINT_ORE);
    }
}
