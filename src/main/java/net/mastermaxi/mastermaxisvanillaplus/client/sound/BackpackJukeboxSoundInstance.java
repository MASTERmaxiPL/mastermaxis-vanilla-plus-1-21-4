package net.mastermaxi.mastermaxisvanillaplus.client.sound;

import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class BackpackJukeboxSoundInstance extends MovingSoundInstance {
    private final PlayerEntity player;

    public BackpackJukeboxSoundInstance(PlayerEntity player, SoundEvent soundEvent) {
        super(soundEvent, SoundCategory.RECORDS, SoundInstance.createRandom());
        this.player = player;
        this.repeat = false;
        this.repeatDelay = 0;
        this.volume = 4.0F;
        this.pitch = 1.0F;
        this.attenuationType = AttenuationType.LINEAR;
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    @Override
    public void tick() {
        if (this.player.isRemoved()) {
            this.stop();
            return;
        }

        this.x = this.player.getX();
        this.y = this.player.getY();
        this.z = this.player.getZ();
    }

    public void stop() {
        this.setDone();
    }
}
