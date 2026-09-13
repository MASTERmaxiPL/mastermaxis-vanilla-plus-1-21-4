package net.mastermaxi.mastermaxisvanillaplus.block;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.fluid.FluidInit;
import net.mastermaxi.mastermaxisvanillaplus.world.tree.SaplingGeneratorsInit;
import net.minecraft.block.*;
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

    // region Red Soil
    public static Block RED_SOIL = register(
            "red_soil",
            Block::new,
            Block.Settings.copy(net.minecraft.block.Blocks.DIRT)
    );
    public static final Block RED_SOIL_STAIRS = registerStairsBlock("red_soil_stairs", RED_SOIL);
    public static final Block RED_SOIL_SLAB = registerSlabBlock("red_soil_slab", RED_SOIL);
    public static Block GRASSY_RED_SOIL = register(
            "grassy_red_soil",
            GrassyRedSoilBlock::new,
            Block.Settings.copy(net.minecraft.block.Blocks.GRASS_BLOCK)
    );
    // endregion Red Soil

    // region Plants
    public static Block REDTHORN = register(
            "redthorn",
            RedthornBlock::new,
            Block.Settings.create()
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.SWEET_BERRY_BUSH)
                    .nonOpaque()
                    .pistonBehavior(net.minecraft.block.piston.PistonBehavior.DESTROY)
    );
    // endregion Plants

    // region Laterite
    public static Block LATERITE = register(
            "laterite",
            Block::new,
            Block.Settings.create()
                    .requiresTool()
                    .strength(1.5F, 6.0F)
                    .sounds(BlockSoundGroup.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
    );
    public static final Block LATERITE_STAIRS = registerStairsBlock("laterite_stairs", LATERITE);
    public static final Block LATERITE_SLAB = registerSlabBlock("laterite_slab", LATERITE);
    // endregion Laterite

    // region Mound & Termites
    public static Block MOUND = register(
            "mound",
            MoundBlock::new,
            Block.Settings.create()
                    .strength(0.8F)
                    .sounds(BlockSoundGroup.GRAVEL)
    );
    public static Block INFECTED_MOUND = register(
            "infected_mound",
            InfectedMoundBlock::new,
            Block.Settings.create()
                    .ticksRandomly()
                    .strength(0.8F)
                    .sounds(BlockSoundGroup.GRAVEL)
    );
    public static Block INFECTED_WOOD = register(
            "infected_wood",
            InfectedWoodBlock::new,
            Block.Settings.create()
                    .strength(2.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()
    );
    public static Block TERMITE_TRAIL = register(
            "termite_trail",
            TermiteTrailBlock::new,
            Block.Settings.create()
                    .noCollision()
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.MUD)
                    .nonOpaque()
                    .pistonBehavior(net.minecraft.block.piston.PistonBehavior.DESTROY)
    );
    // endregion Mound & Termites

    // region Other
    public static Block MUD_BLOCK = register(
            "mud_block",
            settings -> new MudBlock(settings, FluidInit.MUD_WATER_STILL),
            Block.Settings.create()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.MUD)
    );

    //endregion Other

    // region Mudstone
    public static final Identifier MUDSTONE_BLOCK_TOP_BOTTOM_TEXTURE = Identifier.of("mastermaxisvanillaplus", "block/mudstone_block_top_bottom");
    public static final Identifier MUDSTONE_BLOCK_SIDE_TEXTURE = Identifier.of("mastermaxisvanillaplus", "block/mudstone_block_simple");
    public static Block MUDSTONE_BLOCK = register(
            "mudstone_block",
            Block::new,
            Block.Settings.create()
                    .requiresTool()
                    .strength(2F, 6.0F)
                    .sounds(BlockSoundGroup.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
    );
    public static final Block MUDSTONE_BLOCK_SIMPLE = register(
            "mudstone_block_simple",
            Block::new,
            Block.Settings.create()
                    .requiresTool()
                    .strength(2F, 6.0F)
                    .sounds(BlockSoundGroup.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
    );
    public static final Block MUDSTONE_STAIRS = registerStairsBlock("mudstone_stairs", MUDSTONE_BLOCK);
    public static final Block MUDSTONE_SLAB = registerSlabBlock("mudstone_slab", MUDSTONE_BLOCK);
    public static final Block MUDSTONE_BUTTON = registerButtonBlock("mudstone_button", MUDSTONE_BLOCK, BlockSetType.STONE, 20);
    public static final Block MUDSTONE_WALL = registerWallBlock("mudstone_wall", MUDSTONE_BLOCK);
    public static final Block MUDSTONE_DOOR = registerDoorBlock("mudstone_door", MUDSTONE_BLOCK, BlockSetType.STONE);

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
    public static final Block MUDSTONE_BRICK_SLAB = registerSlabBlock("mudstone_brick_slab", MUDSTONE_BRICK);
    public static final Block MUDSTONE_BRICK_PRESSURE_PLATE = registerPressurePlateBlock("mudstone_brick_pressure_plate", MUDSTONE_BRICK, BlockSetType.STONE);
    public static final Block MUDSTONE_BRICK_WALL = registerWallBlock("mudstone_brick_wall", MUDSTONE_BRICK);

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
    public static final Block POLISHED_MUDSTONE_SLAB = registerSlabBlock("polished_mudstone_slab", POLISHED_MUDSTONE);
    public static final Block POLISHED_MUDSTONE_WALL = registerWallBlock("polished_mudstone_wall", POLISHED_MUDSTONE);

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
    public static Block MUDSTONE_COPPER_ORE = register(
            "mudstone_copper_ore",
            Block::new,
            Block.Settings.create()
                    .requiresTool()
                    .strength(3F, 3F)
                    .sounds(BlockSoundGroup.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
    );
    public static Block SILTED_GRAVEL = register(
            "silted_gravel",
            settings -> new ColoredFallingBlock(new net.minecraft.util.ColorCode(-6323630), settings),
            Block.Settings.create()
                    .strength(0.6F)
                    .sounds(BlockSoundGroup.GRAVEL)
                    .instrument(NoteBlockInstrument.SNARE)
    );
    // endregion Mudstone

    // region WoodType

    public static Block BAOBAB_LOG = register(
            "baobab_log",
            PillarBlock::new,
            Block.Settings.copy(net.minecraft.block.Blocks.OAK_LOG)
    );
    public static Block BAOBAB_WOOD = register(
            "baobab_wood",
            PillarBlock::new,
            Block.Settings.copy(net.minecraft.block.Blocks.OAK_WOOD)
    );
    public static Block STRIPPED_BAOBAB_LOG = register(
            "stripped_baobab_log",
            PillarBlock::new,
            Block.Settings.copy(net.minecraft.block.Blocks.STRIPPED_OAK_LOG)
    );
    public static Block STRIPPED_BAOBAB_WOOD = register(
            "stripped_baobab_wood",
            PillarBlock::new,
            Block.Settings.copy(net.minecraft.block.Blocks.STRIPPED_OAK_WOOD)
    );

    public static Block BAOBAB_PLANKS = register(
            "baobab_planks",
            PillarBlock::new,
            Block.Settings.copy(net.minecraft.block.Blocks.OAK_PLANKS)
    );

    public static final Block BAOBAB_STAIRS = registerStairsBlock("baobab_stairs", BAOBAB_PLANKS);
    public static final Block BAOBAB_SLAB = registerSlabBlock("baobab_slab", BAOBAB_PLANKS);
    public static final Block BAOBAB_FENCE = registerFenceBlock("baobab_fence", BAOBAB_PLANKS);
    public static final Block BAOBAB_GATE = registerFenceGateBlock("baobab_gate", BAOBAB_PLANKS, WoodType.OAK);
    public static final Block BAOBAB_DOOR = registerDoorBlock("baobab_door", BAOBAB_PLANKS, BlockSetType.OAK);
    public static final Block BAOBAB_TRAPDOOR = registerTrapDoorBlock("baobab_trapdoor", BAOBAB_PLANKS, BlockSetType.OAK);
    public static final Block BAOBAB_PRESSURE_PLATE = registerPressurePlateBlock("baobab_pressure_plate", BAOBAB_PLANKS, BlockSetType.OAK);
    public static final Block BAOBAB_BUTTON = registerButtonBlock("baobab_button", BAOBAB_PLANKS, BlockSetType.OAK, 20);

    public static Block BAOBAB_LEAVES = register(
            "baobab_leaves",
            settings -> new BaobabLeavesBlock(settings, 0),
            Block.Settings.copy(net.minecraft.block.Blocks.OAK_LEAVES)
    );
    public static Block BAOBAB_LEAVES_FLOWERS = register(
            "baobab_leaves_flowers",
            settings -> new BaobabLeavesBlock(settings, 1),
            Block.Settings.copy(net.minecraft.block.Blocks.OAK_LEAVES)
    );
    public static Block BAOBAB_LEAVES_FRUIT = register(
            "baobab_leaves_fruit",
            settings -> new BaobabLeavesBlock(settings, 2),
            Block.Settings.copy(net.minecraft.block.Blocks.OAK_LEAVES)
    );
    public static Block BAOBAB_SAPLING = register(
            "baobab_sapling",
            settings -> new SaplingBlock(SaplingGeneratorsInit.BAOBAB, settings),
            AbstractBlock.Settings.copy(net.minecraft.block.Blocks.DARK_OAK_SAPLING)
    );
    // endregion WoodType

    //endregion Savanna


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

    private static Block registerSlabBlock(String id, Block base) {
        return register(id, SlabBlock::new, AbstractBlock.Settings.copy(base));
    }

    @SuppressWarnings("SameParameterValue")
    private static Block registerButtonBlock(String id, Block base, BlockSetType type, int pressTicks) {
        return register(id, settings -> new ButtonBlock(type, pressTicks, settings), AbstractBlock.Settings.copy(base).noCollision());
    }

    private static Block registerPressurePlateBlock(String id, Block base, BlockSetType type) {
        return register(id, settings -> new PressurePlateBlock(type, settings), AbstractBlock.Settings.copy(base));
    }

    @SuppressWarnings("SameParameterValue")
    private static Block registerFenceBlock(String id, Block base) {
        return register(id, FenceBlock::new, AbstractBlock.Settings.copy(base));
    }

    @SuppressWarnings("SameParameterValue")
    private static Block registerFenceGateBlock(String id, Block base, WoodType type) {
        return register(id, settings -> new FenceGateBlock(type, settings), AbstractBlock.Settings.copy(base));
    }

    private static Block registerWallBlock(String id, Block base) {
        return register(id, WallBlock::new, AbstractBlock.Settings.copy(base));
    }

    private static Block registerDoorBlock(String id, Block base, BlockSetType type) {
        return register(id, settings -> new DoorBlock(type, settings), AbstractBlock.Settings.copy(base).nonOpaque());
    }

    @SuppressWarnings("SameParameterValue")
    private static Block registerTrapDoorBlock(String id, Block base, BlockSetType type) {
        return register(id, settings -> new TrapdoorBlock(type, settings), AbstractBlock.Settings.copy(base).nonOpaque());
    }

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Registering blocks for " + MASTERmaxisVanillaPlus.MOD_ID);
    }
}
