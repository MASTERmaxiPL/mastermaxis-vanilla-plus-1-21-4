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

                RED_SOIL,
                RED_SOIL_STAIRS,
                RED_SOIL_SLAB,
                GRASSY_RED_SOIL,
                REDTHORN,

                LATERITE,
                LATERITE_STAIRS,
                LATERITE_SLAB,

                MOUND,
                INFECTED_MOUND,
                INFECTED_WOOD,
                TERMITE_TRAIL,

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

                SILTED_GRAVEL,
                MUDSTONE_COAL_ORE,
                MUDSTONE_IRON_ORE,
                MUDSTONE_COPPER_ORE,
                MUDSTONE_FLINT_ORE,

                BAOBAB_LOG,
                STRIPPED_BAOBAB_LOG,
                BAOBAB_WOOD,
                STRIPPED_BAOBAB_WOOD,
                BAOBAB_LEAVES,
                BAOBAB_LEAVES_FLOWERS,
                BAOBAB_LEAVES_FRUIT,
                BAOBAB_SAPLING,
                BAOBAB_PLANKS,
                BAOBAB_STAIRS,
                BAOBAB_SLAB,
                BAOBAB_FENCE,
                BAOBAB_GATE,
                BAOBAB_BUTTON,
                BAOBAB_PRESSURE_PLATE,
                BAOBAB_DOOR,
                BAOBAB_TRAPDOOR
        );
        addBlock(entries, blocks);
    }

    private static void addBlock(FabricItemGroupEntries entries, List<Block> blocks) {
        for(Block block : blocks) {
            entries.add(block);
        }
    }
}