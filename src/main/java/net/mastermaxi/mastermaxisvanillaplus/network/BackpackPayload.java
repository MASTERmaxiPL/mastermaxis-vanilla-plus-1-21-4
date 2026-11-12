package net.mastermaxi.mastermaxisvanillaplus.network;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record BackpackPayload(int tierOrdinal, int entityId) implements CustomPayload {
    public static final Id<BackpackPayload> ID = new Id<>(MASTERmaxisVanillaPlus.id("backpack_open"));

    public static final PacketCodec<RegistryByteBuf, BackpackPayload> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, BackpackPayload::tierOrdinal,
            PacketCodecs.VAR_INT, BackpackPayload::entityId,
            BackpackPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
