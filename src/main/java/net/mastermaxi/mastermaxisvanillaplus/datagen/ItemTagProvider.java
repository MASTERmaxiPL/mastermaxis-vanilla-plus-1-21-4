package net.mastermaxi.mastermaxisvanillaplus.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

import static net.mastermaxi.mastermaxisvanillaplus.block.Blocks.*;
import static net.mastermaxi.mastermaxisvanillaplus.block.Blocks.STRIPPED_BAOBAB_WOOD;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
                .add(BAOBAB_LOG.asItem())
                .add(STRIPPED_BAOBAB_LOG.asItem())
                .add(BAOBAB_WOOD.asItem())
                .add(STRIPPED_BAOBAB_WOOD.asItem());

        getOrCreateTagBuilder(ItemTags.PLANKS)
                .add(BAOBAB_PLANKS.asItem());
    }
}
