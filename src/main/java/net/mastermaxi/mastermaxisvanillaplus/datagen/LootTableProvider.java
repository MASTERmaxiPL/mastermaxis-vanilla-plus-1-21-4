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
import net.minecraft.loot.condition.TableBonusLootCondition;
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

        addDrop(RED_SOIL);
        addDrop(RED_SOIL_STAIRS);
        addDrop(RED_SOIL_SLAB, slabDrops(RED_SOIL_SLAB));
        addDrop(GRASSY_RED_SOIL, drops(GRASSY_RED_SOIL, RED_SOIL));
        addDrop(REDTHORN);

        addDrop(LATERITE);
        addDrop(LATERITE_STAIRS);
        addDrop(LATERITE_SLAB, slabDrops(LATERITE_SLAB));

        addDrop(MOUND);
        addDrop(INFECTED_MOUND, drops(INFECTED_MOUND, MOUND));
        addDrop(TERMITE_TRAIL, dropsNothing());

        addDrop(MUDSTONE_BLOCK);
        addDrop(MUDSTONE_STAIRS);
        addDrop(MUDSTONE_SLAB, slabDrops(MUDSTONE_SLAB));
        addDrop(MUDSTONE_WALL);
        addDrop(MUDSTONE_BUTTON);
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
        addDrop(MUDSTONE_COPPER_ORE, multipleOreDrop(MUDSTONE_COPPER_ORE, Items.RAW_COPPER, 2, 5));
        addDrop(SILTED_GRAVEL, gravelDrops(SILTED_GRAVEL));

        addDrop(BAOBAB_LOG);
        addDrop(STRIPPED_BAOBAB_LOG);
        addDrop(BAOBAB_WOOD);
        addDrop(STRIPPED_BAOBAB_WOOD);
        addDrop(BAOBAB_PLANKS);
        addDrop(BAOBAB_SLAB, slabDrops(BAOBAB_SLAB));
        addDrop(BAOBAB_STAIRS);
        addDrop(BAOBAB_FENCE);
        addDrop(BAOBAB_GATE);
        addDrop(BAOBAB_BUTTON);
        addDrop(BAOBAB_PRESSURE_PLATE);
        addDrop(BAOBAB_DOOR, doorDrops(BAOBAB_DOOR));
        addDrop(BAOBAB_TRAPDOOR);

        addDrop(BAOBAB_LEAVES, leavesDrops(BAOBAB_LEAVES, BAOBAB_SAPLING, 0.06F));
        addDrop(BAOBAB_LEAVES_FLOWERS, leavesDrops(BAOBAB_LEAVES_FLOWERS, BAOBAB_SAPLING, 0.06F));
        addDrop(BAOBAB_LEAVES_FRUIT, leavesDrops(BAOBAB_LEAVES_FRUIT, BAOBAB_SAPLING, 0.06F).pool(
                net.minecraft.loot.LootPool.builder()
                        .rolls(net.minecraft.loot.provider.number.ConstantLootNumberProvider.create(1.0F))
                        .with(net.minecraft.loot.entry.ItemEntry.builder(ItemInit.BAOBAB_FRUIT))
        ));
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

    public LootTable.Builder gravelDrops(Block drop) {
        RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);
        return this.dropsWithSilkTouch(
                drop,
                this.addSurvivesExplosionCondition(
                        drop,
                        ItemEntry.builder(Items.FLINT)
                                .conditionally(TableBonusLootCondition.builder(impl.getOrThrow(Enchantments.FORTUNE), 0.1F, 0.14285715F, 0.25F, 1.0F))
                                .alternatively(ItemEntry.builder(drop))
                )
        );
    }
}
