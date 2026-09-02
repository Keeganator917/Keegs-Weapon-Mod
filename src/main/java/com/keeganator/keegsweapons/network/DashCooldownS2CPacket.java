package com.keeganator.keegsweapons.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record DashCooldownS2CPacket(int cooldown)
        implements CustomPacketPayload {

    public static final Identifier ID =
            Identifier.fromNamespaceAndPath("keegsweapons", "dash_cooldown");

    public static final CustomPacketPayload.Type<DashCooldownS2CPacket> PACKET_ID =
            new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<ByteBuf, DashCooldownS2CPacket> CODEC =
            ByteBufCodecs.INT.map(DashCooldownS2CPacket::new, DashCooldownS2CPacket::cooldown);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}