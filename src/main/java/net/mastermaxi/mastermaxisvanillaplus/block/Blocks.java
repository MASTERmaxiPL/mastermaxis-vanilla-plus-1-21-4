package net.mastermaxi.mastermaxisvanillaplus.block;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.fluid.FluidInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.function.Function;

public class Blocks {


    //region Miscellaneous

    public static Block FEATHER_BLOCK = register(
            "feather_block",
            Block::new,
            Block.Settings.create()
                    .strength(0.2f)
                    .sounds(BlockSoundGroup.WOOL)
    );

    //endregion
    //region Savanna

    public static Block MUD_BLOCK = register(
            "mud_block",
            settings -> new MudBlock(settings, FluidInit.MUD_WATER_STILL),
            Block.Settings.create()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.MUD)
    );
    public static Block MUDSTONE_BLOCK = register(
            "mudstone_block",
            Block::new,
            Block.Settings.create()
                    .requiresTool()
                    .strength(2F, 6.0F)
                    .sounds(BlockSoundGroup.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
    );
    public static final Block MUDSTONE_STAIRS = registerStairsBlock("mudstone_stairs", MUDSTONE_BLOCK);

    public static Block MUDSTONE_BRICK = register(
            "mudstone_brick",
            Block::new,
            Block.Settings.create()
                    .requiresTool()
                    .strength(2F, 6.0F)
                    .sounds(BlockSoundGroup.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
    );
    public static final Block MUDSTONE_BRICK_STAIRS = registerStairsBlock("mudstone_brick_stairs", MUDSTONE_BRICK);


    public static Block POLISHED_MUDSTONE = register(
            "polished_mudstone",
            Block::new,
            Block.Settings.create()
                    .requiresTool()
                    .strength(2F, 6.0F)
                    .sounds(BlockSoundGroup.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
    );
    public static final Block POLISHED_MUDSTONE_STAIRS = registerStairsBlock("polished_mudstone_stairs", POLISHED_MUDSTONE);

    public static Block MUDSTONE_COAL_ORE = register(
            "mudstone_coal_ore",
            settings -> new ExperienceDroppingBlock(UniformIntProvider.create(0, 2), settings),
            Block.Settings.create()
                    .requiresTool()
                    .strength(3F, 3F)
                    .sounds(BlockSoundGroup.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
    );
    public static Block MUDSTONE_IRON_ORE = register(
            "mudstone_iron_ore",
            settings -> new ExperienceDroppingBlock(UniformIntProvider.create(0, 2), settings),
            Block.Settings.create()
                    .requiresTool()
                    .strength(3F, 3F)
                    .sounds(BlockSoundGroup.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
    );
    public static Block MUDSTONE_FLINT_ORE = register(
            "mudstone_flint_ore",
            settings -> new ExperienceDroppingBlock(UniformIntProvider.create(0, 2), settings),
            Block.Settings.create()
                    .requiresTool()
                    .strength(3F, 3F)
                    .sounds(BlockSoundGroup.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
    );

    //endregion


    private static Block register(
            String name,
            Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings
    ) {
        final Identifier identifier = MASTERmaxisVanillaPlus.id(name);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final Block block = net.minecraft.block.Blocks.register(registryKey, factory, settings);
        Items.register(block);

        return block;
    }

    private static Block registerStairsBlock(String id, Block base) {
        return register(id, settings -> new StairsBlock(base.getDefaultState(), settings), AbstractBlock.Settings.copy(base));
    }

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Registering blocks for " + MASTERmaxisVanillaPlus.MOD_ID);
    }
}
