package com.keeganator.keegsweapons.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record DashC2SPacket() implements CustomPacketPayload {

    public static final Identifier ID = Identifier.fromNamespaceAndPath("keegsweapons", "dash");
    public static final CustomPacketPayload.Type<DashC2SPacket> PACKET_ID = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, DashC2SPacket> CODEC =
            StreamCodec.unit(new DashC2SPacket());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}