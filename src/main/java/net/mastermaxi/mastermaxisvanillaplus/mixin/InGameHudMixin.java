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
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.OstrichEntity;
import net.minecraft.entity.JumpingMount;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profilers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow @Final private static Identifier JUMP_BAR_BACKGROUND_TEXTURE;
    @Shadow @Final private static Identifier JUMP_BAR_COOLDOWN_TEXTURE;
    @Shadow @Final private static Identifier JUMP_BAR_PROGRESS_TEXTURE;

    @Inject(method = "renderMountJumpBar", at = @At("HEAD"), cancellable = true)
    private void renderOstrichJumpBar(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
        if (mount instanceof OstrichEntity ostrich) {
            ci.cancel();
            Profilers.get().push("jumpBar");
            int k = context.getScaledWindowHeight() - 32 + 3;
            context.drawGuiTexture(RenderLayer::getGuiTextured, JUMP_BAR_BACKGROUND_TEXTURE, x, k, 182, 5);

            if (ostrich.isOstrichSprinting()) {
                context.drawGuiTexture(RenderLayer::getGuiTextured, JUMP_BAR_PROGRESS_TEXTURE, 182, 5, 0, 0, x, k, 182, 5);
            } else if (ostrich.getCooldownTicks() > 0) {
                float ratio = (float) ostrich.getCooldownTicks() / 400.0F;
                int width = MathHelper.clamp((int)(ratio * 182.0F), 0, 182);
                if (width > 0) {
                    context.drawGuiTexture(RenderLayer::getGuiTextured, JUMP_BAR_COOLDOWN_TEXTURE, 182, 5, 0, 0, x, k, width, 5);
                }
            }
            Profilers.get().pop();
        }
    }
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
