package net.mastermaxi.mastermaxisvanillaplus.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static net.mastermaxi.mastermaxisvanillaplus.block.Blocks.*;

public class LootTableProvider extends FabricBlockLootTableProvider {
    public LootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(FEATHER_BLOCK);

        addDrop(MUDSTONE_BLOCK);
        addDrop(MUDSTONE_STAIRS);
        addDrop(MUDSTONE_SLAB, slabDrops(MUDSTONE_SLAB));
        addDrop(MUDSTONE_WALL);
        addDrop(MUDSTONE_BUTTON);
        addDrop(MUDSTONE_PRESSURE_PLATE);
        addDrop(MUDSTONE_DOOR, doorDrops(MUDSTONE_DOOR));

        addDrop(MUDSTONE_BRICK);
        addDrop(MUDSTONE_BRICK_STAIRS);
        addDrop(MUDSTONE_BRICK_SLAB, slabDrops(MUDSTONE_BRICK_SLAB));
        addDrop(MUDSTONE_BRICK_WALL);
        addDrop(MUDSTONE_BRICK_PRESSURE_PLATE);

        addDrop(POLISHED_MUDSTONE);
        addDrop(POLISHED_MUDSTONE_STAIRS);
        addDrop(POLISHED_MUDSTONE_SLAB, slabDrops(POLISHED_MUDSTONE_SLAB));
        addDrop(POLISHED_MUDSTONE_WALL);

        addDrop(MUD_BLOCK, multipleOreDrop(MUD_BLOCK, ItemInit.MUD_BALL, 1, 3));
        addDrop(MUDSTONE_COAL_ORE, multipleOreDrop(MUDSTONE_COAL_ORE, Items.COAL, 1, 2));
        addDrop(MUDSTONE_IRON_ORE, oreDrops(MUDSTONE_IRON_ORE, Items.RAW_IRON));
        addDrop(MUDSTONE_FLINT_ORE, multipleOreDrop(MUDSTONE_FLINT_ORE, Items.FLINT, 1, 2));
    }

    public LootTable.Builder multipleOreDrop(Block drop, Item item, int min, int max) {
        RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);
        return this.dropsWithSilkTouch(
                drop,
                this.applyExplosionDecay(
                        drop, ((LeafEntry.Builder<?>)
                        ItemEntry.builder(item)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max))))
                                .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))
                ).apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE))));
    }
}
