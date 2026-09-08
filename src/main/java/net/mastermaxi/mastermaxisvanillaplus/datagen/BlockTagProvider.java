package net.mastermaxi.mastermaxisvanillaplus.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

import static net.mastermaxi.mastermaxisvanillaplus.block.Blocks.*;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(MUDSTONE_BLOCK)
                .add(MUDSTONE_STAIRS)
                .add(MUDSTONE_SLAB)
                .add(MUDSTONE_WALL)
                .add(MUDSTONE_BUTTON)
                .add(MUDSTONE_DOOR)

                .add(MUDSTONE_BRICK)
                .add(MUDSTONE_BRICK_STAIRS)
                .add(MUDSTONE_BRICK_SLAB)
                .add(MUDSTONE_BRICK_WALL)
                .add(MUDSTONE_BRICK_PRESSURE_PLATE)

                .add(POLISHED_MUDSTONE)
                .add(POLISHED_MUDSTONE_STAIRS)
                .add(POLISHED_MUDSTONE_SLAB)
                .add(POLISHED_MUDSTONE_WALL)

                .add(MUDSTONE_COAL_ORE)
                .add(MUDSTONE_IRON_ORE)
                .add(MUDSTONE_FLINT_ORE);

        getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
                .add(MUD_BLOCK);

        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .add(BAOBAB_LOG)
                .add(STRIPPED_BAOBAB_LOG)
                .add(BAOBAB_WOOD)
                .add(STRIPPED_BAOBAB_WOOD)
                .add(BAOBAB_PLANKS)
                .add(BAOBAB_SLAB)
                .add(BAOBAB_STAIRS)
                .add(BAOBAB_FENCE)
                .add(BAOBAB_GATE)
                .add(BAOBAB_BUTTON)
                .add(BAOBAB_PRESSURE_PLATE)
                .add(BAOBAB_DOOR)
                .add(BAOBAB_TRAPDOOR);

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(MUDSTONE_BLOCK)
                .add(MUDSTONE_STAIRS)
                .add(MUDSTONE_SLAB)
                .add(MUDSTONE_WALL)
                .add(MUDSTONE_BUTTON)
                .add(MUDSTONE_DOOR)

                .add(MUDSTONE_BRICK)
                .add(MUDSTONE_BRICK_STAIRS)
                .add(MUDSTONE_BRICK_SLAB)
                .add(MUDSTONE_BRICK_WALL)
                .add(MUDSTONE_BRICK_PRESSURE_PLATE)

                .add(POLISHED_MUDSTONE)
                .add(POLISHED_MUDSTONE_STAIRS)
                .add(POLISHED_MUDSTONE_SLAB)
                .add(POLISHED_MUDSTONE_WALL)

                .add(MUDSTONE_COAL_ORE)
                .add(MUDSTONE_IRON_ORE)
                .add(MUDSTONE_FLINT_ORE);

        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(MUDSTONE_WALL)
                .add(MUDSTONE_BRICK_WALL)
                .add(POLISHED_MUDSTONE_WALL);

        getOrCreateTagBuilder(BlockTags.SLABS)
                .add(MUDSTONE_SLAB)
                .add(MUDSTONE_BRICK_SLAB)
                .add(POLISHED_MUDSTONE_SLAB)
                .add(BAOBAB_SLAB);

        getOrCreateTagBuilder(BlockTags.STAIRS)
                .add(MUDSTONE_STAIRS)
                .add(MUDSTONE_BRICK_STAIRS)
                .add(POLISHED_MUDSTONE_STAIRS)
                .add(BAOBAB_STAIRS);

        getOrCreateTagBuilder(BlockTags.BUTTONS)
                .add(MUDSTONE_BUTTON)
                .add(BAOBAB_BUTTON);

        getOrCreateTagBuilder(BlockTags.PRESSURE_PLATES)
                .add(MUDSTONE_BRICK_PRESSURE_PLATE)
                .add(BAOBAB_PRESSURE_PLATE);

        getOrCreateTagBuilder(BlockTags.DOORS)
                .add(MUDSTONE_DOOR)
                .add(BAOBAB_DOOR);

        getOrCreateTagBuilder(BlockTags.FENCES)
                .add(BAOBAB_FENCE);

        getOrCreateTagBuilder(BlockTags.FENCE_GATES)
                .add(BAOBAB_GATE);

        getOrCreateTagBuilder(BlockTags.TRAPDOORS)
                .add(BAOBAB_TRAPDOOR);

        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
                .add(BAOBAB_LOG)
                .add(STRIPPED_BAOBAB_LOG)
                .add(BAOBAB_WOOD)
                .add(STRIPPED_BAOBAB_WOOD);
    }
}
