package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.entity.client.MuddyPigRenderStateAccess;
import net.minecraft.client.render.entity.state.PigEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PigEntityRenderState.class)
public class PigEntityRenderStateMixin implements MuddyPigRenderStateAccess {
    @Unique
    private int mastermaxisvanillaplus$mudVariant = 0;
    @Unique
    private boolean mastermaxisvanillaplus$isDigging = false;
    @Unique
    private boolean mastermaxisvanillaplus$isWallowing = false;

    @Override
    public int mastermaxisvanillaplus$getMudVariant() {
        return this.mastermaxisvanillaplus$mudVariant;
    }

    @Override
    public void mastermaxisvanillaplus$setMudVariant(int variant) {
        this.mastermaxisvanillaplus$mudVariant = variant;
    }

    @Override
    public boolean mastermaxisvanillaplus$isDigging() {
        return this.mastermaxisvanillaplus$isDigging;
    }

    @Override
    public void mastermaxisvanillaplus$setDigging(boolean digging) {
        this.mastermaxisvanillaplus$isDigging = digging;
    }

    @Override
    public boolean mastermaxisvanillaplus$isWallowing() {
        return this.mastermaxisvanillaplus$isWallowing;
    }

    @Override
    public void mastermaxisvanillaplus$setWallowing(boolean wallowing) {
        this.mastermaxisvanillaplus$isWallowing = wallowing;
    }
}
