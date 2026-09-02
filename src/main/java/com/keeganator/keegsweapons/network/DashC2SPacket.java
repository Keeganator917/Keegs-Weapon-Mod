package com.keeganator.keegsweapons.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record DashC2SPacket() implements CustomPayload {

    public static final Identifier ID = Identifier.of("keegsweapons", "dash");
    public static final CustomPayload.Id<DashC2SPacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<RegistryByteBuf, DashC2SPacket> CODEC =
            PacketCodec.unit(new DashC2SPacket());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}