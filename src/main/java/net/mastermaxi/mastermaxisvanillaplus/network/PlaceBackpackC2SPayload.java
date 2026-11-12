package net.mastermaxi.mastermaxisvanillaplus.network;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record PlaceBackpackC2SPayload() implements CustomPayload {
    public static final Id<PlaceBackpackC2SPayload> ID = new Id<>(MASTERmaxisVanillaPlus.id("place_backpack_key"));
    public static final PacketCodec<PacketByteBuf, PlaceBackpackC2SPayload> PACKET_CODEC = PacketCodec.unit(new PlaceBackpackC2SPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
