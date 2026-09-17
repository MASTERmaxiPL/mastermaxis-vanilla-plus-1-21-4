package net.mastermaxi.mastermaxisvanillaplus.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemGroupsInit {
    public static final RegistryKey<ItemGroup> MOD_ITEM_GROUP_KEY = RegistryKey.of(
            Registries.ITEM_GROUP.getKey(),
            Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "item_group")
    );

    public static final RegistryKey<ItemGroup> COMBAT_ITEM_GROUP_KEY = RegistryKey.of(
            Registries.ITEM_GROUP.getKey(),
            Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "combat_item_group")
    );

    public static final ItemGroup MOD_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Blocks.MUDSTONE_BLOCK.asItem()))
            .displayName(Text.translatable("itemGroup.mod_item_group"))
            .build();

    public static final ItemGroup COMBAT_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ItemInit.IRON_CROSSBOW))
            .displayName(Text.translatable("itemGroup.mod_combat_group"))
            .build();

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Initializing Item Groups");
        Registry.register(Registries.ITEM_GROUP, MOD_ITEM_GROUP_KEY, MOD_ITEM_GROUP);
        Registry.register(Registries.ITEM_GROUP, COMBAT_ITEM_GROUP_KEY, COMBAT_ITEM_GROUP);
    }
}
