package net.mastermaxi.mastermaxisvanillaplus.damageTypes;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class DamageTypeInit {
    public static final RegistryKey<DamageType> MUD_BALL_DAMAGE = RegistryKey.of(
            RegistryKeys.DAMAGE_TYPE,
            MASTERmaxisVanillaPlus.id("mud_ball")
    );
}
