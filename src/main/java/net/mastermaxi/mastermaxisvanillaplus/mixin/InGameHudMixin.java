package net.mastermaxi.mastermaxisvanillaplus.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mastermaxi.mastermaxisvanillaplus.fluid.MudWaterFluid;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @WrapOperation(
            method = "renderAirBubbles",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V"
            )
    )
    private void tintMudAirBubbles(
            DrawContext context,
            Function<Identifier, RenderLayer> renderLayers,
            Identifier sprite,
            int x,
            int y,
            int width,
            int height,
            Operation<Void> original,
            @Local(argsOnly = true) PlayerEntity player
    ) {
        if (mastermaxisvanillaplus$isInMudWater(player)) {
            context.drawGuiTexture(renderLayers, sprite, x, y, width, height, 0xFFC07030);
        } else {
            original.call(context, renderLayers, sprite, x, y, width, height);
        }
    }

    @Unique
    private static boolean mastermaxisvanillaplus$isInMudWater(PlayerEntity player) {
        if (player == null || player.getWorld() == null) {
            return false;
        }
        BlockPos eyePos = BlockPos.ofFloored(player.getX(), player.getEyeY(), player.getZ());
        if (player.getWorld().getFluidState(eyePos).getFluid() instanceof MudWaterFluid) {
            return true;
        }
        BlockPos pos = player.getBlockPos();
        return player.getWorld().getFluidState(pos).getFluid() instanceof MudWaterFluid;
    }
}
