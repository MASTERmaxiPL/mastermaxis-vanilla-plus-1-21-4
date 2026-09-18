package net.mastermaxi.mastermaxisvanillaplus.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
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

        getOrCreateTagBuilder(ItemTags.LEAVES)
                .add(BAOBAB_LEAVES.asItem())
                .add(BAOBAB_LEAVES_FLOWERS.asItem())
                .add(BAOBAB_LEAVES_FRUIT.asItem());

        getOrCreateTagBuilder(net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.EGGS)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.OSTRICH_EGG);

        getOrCreateTagBuilder(net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.FEATHERS)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.OSTRICH_FEATHER);

        // Heavy arrow is also an arrow (for bow compatibility)
        getOrCreateTagBuilder(ItemTags.ARROWS)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.HEAVY_ARROW);

        // Bolts tag - used by crossbows instead of arrows
        getOrCreateTagBuilder(net.minecraft.registry.tag.TagKey.of(
                net.minecraft.registry.RegistryKeys.ITEM,
                net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus.id("bolts")
        ))
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.BOLT);

        getOrCreateTagBuilder(ItemTags.BOW_ENCHANTABLE)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.HEAVY_BOW)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.HEAVY_HARDENED_BOW);

        getOrCreateTagBuilder(ItemTags.CROSSBOW_ENCHANTABLE)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.IRON_CROSSBOW)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.NETHER_CROSSBOW)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.CROSSBOW_VIEWFINDER)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.IRON_CROSSBOW_VIEWFINDER)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.NETHER_CROSSBOW_VIEWFINDER);

        getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.IRON_CROSSBOW)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.NETHER_CROSSBOW)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.CROSSBOW_VIEWFINDER)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.IRON_CROSSBOW_VIEWFINDER)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.NETHER_CROSSBOW_VIEWFINDER)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.HEAVY_BOW)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.HEAVY_HARDENED_BOW);

        getOrCreateTagBuilder(ItemTags.VANISHING_ENCHANTABLE)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.IRON_CROSSBOW)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.NETHER_CROSSBOW)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.CROSSBOW_VIEWFINDER)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.IRON_CROSSBOW_VIEWFINDER)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.NETHER_CROSSBOW_VIEWFINDER)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.HEAVY_BOW)
                .add(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.HEAVY_HARDENED_BOW);
    }
}
