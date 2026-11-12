package net.mastermaxi.mastermaxisvanillaplus.client.sound;

import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

import java.util.HashMap;
import java.util.Map;

public class BackpackJukeboxSoundManager {
    private static final Map<Integer, BackpackJukeboxSoundInstance> ACTIVE_SOUNDS = new HashMap<>();

    public static void play(PlayerEntity player, RegistryEntry<JukeboxSong> song) {
        stop(player.getId());

        SoundEvent soundEvent = song.value().soundEvent().value();
        BackpackJukeboxSoundInstance soundInstance = new BackpackJukeboxSoundInstance(player, soundEvent);
        ACTIVE_SOUNDS.put(player.getId(), soundInstance);
        MinecraftClient.getInstance().getSoundManager().play(soundInstance);

        if (player == MinecraftClient.getInstance().player) {
            MinecraftClient.getInstance().inGameHud.setRecordPlayingOverlay(song.value().description());
        }
    }

    public static void stop(int entityId) {
        BackpackJukeboxSoundInstance soundInstance = ACTIVE_SOUNDS.remove(entityId);
        if (soundInstance != null) {
            soundInstance.stop();
        }
    }

    public static void stopAll() {
        for (BackpackJukeboxSoundInstance soundInstance : ACTIVE_SOUNDS.values()) {
            soundInstance.stop();
        }
        ACTIVE_SOUNDS.clear();
    }
}
