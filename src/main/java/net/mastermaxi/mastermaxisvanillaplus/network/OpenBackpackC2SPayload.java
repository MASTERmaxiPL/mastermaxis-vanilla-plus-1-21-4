package net.mastermaxi.mastermaxisvanillaplus.network;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record OpenBackpackC2SPayload() implements CustomPayload {
    public static final Id<OpenBackpackC2SPayload> ID = new Id<>(MASTERmaxisVanillaPlus.id("open_backpack_key"));
    public static final PacketCodec<PacketByteBuf, OpenBackpackC2SPayload> PACKET_CODEC = PacketCodec.unit(new OpenBackpackC2SPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
