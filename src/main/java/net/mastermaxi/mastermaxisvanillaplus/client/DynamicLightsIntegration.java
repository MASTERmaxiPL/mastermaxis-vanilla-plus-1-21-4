package net.mastermaxi.mastermaxisvanillaplus.client;

import dev.lambdaurora.lambdynlights.api.DynamicLightsContext;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import dev.lambdaurora.lambdynlights.api.entity.luminance.EntityLuminance;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSourceManager;
import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.item.BackpackUpgradeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class DynamicLightsIntegration implements DynamicLightsInitializer {
    public static final EntityLuminance BACKPACK_LANTERN_LUMINANCE = new EntityLuminance() {
        @Override
        public @NotNull Type type() {
            return BACKPACK_LANTERN_TYPE;
        }

        @Override
        public @Range(from = 0, to = 15) int getLuminance(@NotNull ItemLightSourceManager itemLightSourceManager, @NotNull Entity entity) {
            if (entity instanceof LivingEntity living) {
                return BackpackUpgradeHelper.getBackpackLanternLuminance(living);
            }
            return 0;
        }
    };

    public static final EntityLuminance.Type BACKPACK_LANTERN_TYPE = EntityLuminance.Type.registerSimple(
            Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "backpack_lantern"),
            BACKPACK_LANTERN_LUMINANCE
    );

    @Override
    public void onInitializeDynamicLights(DynamicLightsContext context) {
        context.entityLightSourceManager().onRegisterEvent().register(
                registerContext -> registerContext.register(EntityType.PLAYER, BACKPACK_LANTERN_LUMINANCE)
        );
    }
}
