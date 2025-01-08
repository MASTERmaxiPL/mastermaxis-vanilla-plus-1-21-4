package net.mastermaxi.mastermaxisvanillaplus.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.fluid.FluidInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class BlockInit {

    public static Block FEATHER_BLOCK = register(
            "feather_block",
            Block::new,
            Block.Settings.create()
                   .strength(0.2f)
                   .sounds(BlockSoundGroup.WOOL)
    );
    public static Block MUD_BLOCK = register(
            "mud_block",
            settings -> new MudBlock(settings, FluidInit.MUD_WATER_STILL),
            Block.Settings.create()
                   .strength(0.5f)
                   .sounds(BlockSoundGroup.MUD)
    );

    private static Block register(
            String name,
            Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings
    ) {
        final Identifier identifier = MASTERmaxisVanillaPlus.id(name);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        Items.register(block);

        return block;
    }

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Registering blocks for " + MASTERmaxisVanillaPlus.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register((entries) -> {
            entries.add(FEATHER_BLOCK);
            entries.add(MUD_BLOCK);
        });
    }
}
