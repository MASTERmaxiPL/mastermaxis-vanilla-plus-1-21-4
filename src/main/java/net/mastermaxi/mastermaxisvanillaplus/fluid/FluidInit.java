package net.mastermaxi.mastermaxisvanillaplus.fluid;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.block.*;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;


public class FluidInit {
    public static FlowableFluid MUD_WATER_STILL;
    public static FlowableFluid MUD_WATER_FLOWING;
    public static Block MUD_WATER_BLOCK;
    public static Item MUD_WATER_BUCKET;


    private static FlowableFluid registerFluid(String name, FlowableFluid fluid) {
        Identifier id = MASTERmaxisVanillaPlus.id(name);
        RegistryKey<Fluid> key = RegistryKey.of(RegistryKeys.FLUID, id);

        return Registry.register(Registries.FLUID, key, fluid);
    }

    private static Block registerFluidBlock(String name, AbstractBlock.Settings settings, FlowableFluid fluid) {
        Identifier id = MASTERmaxisVanillaPlus.id(name);
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, id);
        MASTERmaxisVanillaPlus.LOGGER.info("Registering FluidBlock with ID and Key: " + id + ", " + key);

        FluidBlock fluidBlock = new FluidBlock(fluid, settings.registryKey(key));
        MASTERmaxisVanillaPlus.LOGGER.info("FluidBlock created: " + fluidBlock);

        return Registry.register(Registries.BLOCK, key, fluidBlock);
    }

    @FunctionalInterface
    private interface BucketItemCreator {
        BucketItem createBucketItem(Item.Settings settings);
    }

    private static Item registerItem(String name, Item.Settings itemSettings, BucketItemCreator creator) {
        Identifier id = MASTERmaxisVanillaPlus.id(name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
        Item.Settings settings = itemSettings.registryKey(key);
        return Registry.register(Registries.ITEM, key, creator.createBucketItem(settings));
    }

    public static void initialize() {
        MUD_WATER_STILL = registerFluid("mud_water", new MudWaterFluid.Still());
        MUD_WATER_FLOWING = registerFluid("mud_water_flowing", new MudWaterFluid.Flowing());

        MUD_WATER_BUCKET = registerItem(
                "mud_water_bucket",
                new Item.Settings().maxCount(1),
                (settings) -> new BucketItem(
                        MUD_WATER_STILL,
                        settings.recipeRemainder(Items.BUCKET).maxCount(1)
                )
        );

        MUD_WATER_BLOCK = registerFluidBlock(
                "mud_water_block",
                AbstractBlock.Settings.copy(Blocks.WATER),
                MUD_WATER_STILL
        );
    }
}
