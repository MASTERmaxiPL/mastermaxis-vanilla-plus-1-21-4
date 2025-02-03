package net.mastermaxi.mastermaxisvanillaplus.block;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemGroupsInit;
import net.minecraft.block.Block;

import java.util.List;

import static net.mastermaxi.mastermaxisvanillaplus.block.Blocks.*;

public class BlockInit {


    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Initializing blocks for " + MASTERmaxisVanillaPlus.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroupsInit.MOD_ITEM_GROUP_KEY).register(BlockInit::modifyEntries);
    }

    private static void modifyEntries(FabricItemGroupEntries entries) {
        List<Block> blocks = List.of(
                FEATHER_BLOCK,
                MUD_BLOCK,

                MUDSTONE_BLOCK,
                MUDSTONE_STAIRS,
                MUDSTONE_SLAB,
                MUDSTONE_WALL,
                MUDSTONE_BUTTON,
                MUDSTONE_DOOR,

                MUDSTONE_BRICK,
                MUDSTONE_BRICK_STAIRS,
                MUDSTONE_BRICK_SLAB,
                MUDSTONE_BRICK_WALL,
                MUDSTONE_BRICK_PRESSURE_PLATE,

                POLISHED_MUDSTONE,
                POLISHED_MUDSTONE_STAIRS,
                POLISHED_MUDSTONE_SLAB,
                POLISHED_MUDSTONE_WALL,

                MUDSTONE_COAL_ORE,
                MUDSTONE_IRON_ORE,
                MUDSTONE_FLINT_ORE
        );
        addBlock(entries, blocks);
    }

    private static void addBlock(FabricItemGroupEntries entries, List<Block> blocks) {
        for(Block block : blocks) {
            entries.add(block);
        }
    }
}