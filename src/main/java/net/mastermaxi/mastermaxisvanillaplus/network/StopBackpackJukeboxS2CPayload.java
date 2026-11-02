package net.mastermaxi.mastermaxisvanillaplus.network;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record StopBackpackJukeboxS2CPayload(int entityId) implements CustomPayload {
    public static final Id<StopBackpackJukeboxS2CPayload> ID = new Id<>(Identifier.of(MASTERmaxisVanillaPlus.MOD_ID, "stop_backpack_jukebox"));
    public static final PacketCodec<RegistryByteBuf, StopBackpackJukeboxS2CPayload> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, StopBackpackJukeboxS2CPayload::entityId,
            StopBackpackJukeboxS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
