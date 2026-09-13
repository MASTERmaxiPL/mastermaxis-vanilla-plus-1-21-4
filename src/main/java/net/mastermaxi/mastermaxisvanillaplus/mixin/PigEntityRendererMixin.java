package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.mastermaxi.mastermaxisvanillaplus.entity.client.MuddyPigRenderStateAccess;
import net.mastermaxi.mastermaxisvanillaplus.entity.client.PigMudFlowerFeatureRenderer;
import net.mastermaxi.mastermaxisvanillaplus.entity.client.PigMuddedTopEntityModel;
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.MuddyPigAccess;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.client.render.entity.state.PigEntityRenderState;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PigEntityRenderer.class)
public abstract class PigEntityRendererMixin extends MobEntityRenderer<PigEntity, PigEntityRenderState, PigEntityModel> {
    @Unique
    private static final Identifier MUD_BOTTOM_TEXTURE = Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "textures/entity/pig/pig_in_mud_bottom.png");
    @Unique
    private static final Identifier MUD_TOP_TEXTURE = Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "textures/entity/pig/pig_in_mud_top.png");

    public PigEntityRendererMixin(EntityRendererFactory.Context context, PigEntityModel entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void mastermaxisvanillaplus$init(EntityRendererFactory.Context context, CallbackInfo ci) {
        this.addFeature(new PigMudFlowerFeatureRenderer(this, context.getPart(PigMuddedTopEntityModel.LAYER_LOCATION)));
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/passive/PigEntity;Lnet/minecraft/client/render/entity/state/PigEntityRenderState;F)V", at = @At("TAIL"))
    private void mastermaxisvanillaplus$updateRenderState(PigEntity pig, PigEntityRenderState state, float f, CallbackInfo ci) {
        MuddyPigAccess access = (MuddyPigAccess) pig;
        MuddyPigRenderStateAccess stateAccess = (MuddyPigRenderStateAccess) state;
        stateAccess.mastermaxisvanillaplus$setMudVariant(access.mastermaxisvanillaplus$getMudVariant());
        stateAccess.mastermaxisvanillaplus$setDigging(access.mastermaxisvanillaplus$isDigging());
        stateAccess.mastermaxisvanillaplus$setWallowing(access.mastermaxisvanillaplus$isWallowing());
    }

    @Inject(method = "getTexture(Lnet/minecraft/client/render/entity/state/PigEntityRenderState;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
    private void mastermaxisvanillaplus$getTexture(PigEntityRenderState state, CallbackInfoReturnable<Identifier> cir) {
        MuddyPigRenderStateAccess stateAccess = (MuddyPigRenderStateAccess) state;
        int variant = stateAccess.mastermaxisvanillaplus$getMudVariant();
        if (variant == 1) {
            cir.setReturnValue(MUD_BOTTOM_TEXTURE);
        } else if (variant == 2) {
            cir.setReturnValue(MUD_TOP_TEXTURE);
        }
    }
}
