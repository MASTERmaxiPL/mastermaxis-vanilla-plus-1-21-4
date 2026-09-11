package net.mastermaxi.mastermaxisvanillaplus.fluid;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemGroupsInit;
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

    private static Block registerFluidBlock(FlowableFluid fluid, AbstractBlock.Settings settings) {
        Identifier id = MASTERmaxisVanillaPlus.id("mud_water_block");
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, id);
        MASTERmaxisVanillaPlus.LOGGER.info("Registering FluidBlock with ID and Key: " + id + ", " + key);

        FluidBlock fluidBlock = new MudWaterFluidBlock(fluid, settings.registryKey(key));
        MASTERmaxisVanillaPlus.LOGGER.info("FluidBlock created: " + fluidBlock);

        return Registry.register(Registries.BLOCK, key, fluidBlock);
    }

    private static Item registerBucketItem(FlowableFluid fluid) {
        Identifier id = MASTERmaxisVanillaPlus.id("mud_water_bucket");
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
        Item.Settings settings = new Item.Settings()
                .registryKey(key)
                .recipeRemainder(Items.BUCKET)
                .maxCount(1);
        return Registry.register(Registries.ITEM, key, new BucketItem(fluid, settings));
    }

    public static void initialize() {
        MUD_WATER_STILL = registerFluid("mud_water", new MudWaterFluid.Still());
        MUD_WATER_FLOWING = registerFluid("mud_water_flowing", new MudWaterFluid.Flowing());

        MUD_WATER_BUCKET = registerBucketItem(MUD_WATER_STILL);

        MUD_WATER_BLOCK = registerFluidBlock(
                MUD_WATER_STILL,
                AbstractBlock.Settings.copy(Blocks.WATER)
        );

        ItemGroupEvents.modifyEntriesEvent(ItemGroupsInit.MOD_ITEM_GROUP_KEY).register((entries) -> entries.add(MUD_WATER_BUCKET));
    }
}
