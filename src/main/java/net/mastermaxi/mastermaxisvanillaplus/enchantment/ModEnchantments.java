package net.mastermaxi.mastermaxisvanillaplus.enchantment;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> CURSE_OF_WELDING = RegistryKey.of(
            RegistryKeys.ENCHANTMENT,
            MASTERmaxisVanillaPlus.id("curse_of_welding")
    );

    public static void initialize() {
        MASTERmaxisVanillaPlus.LOGGER.info("Registering enchantments for " + MASTERmaxisVanillaPlus.MOD_ID);
    }
}
