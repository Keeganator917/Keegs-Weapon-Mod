package com.keeganator.keegsweapons.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record DashCooldownS2CPacket(int cooldown)
        implements CustomPayload {

    public static final Identifier ID =
            Identifier.of("keegsweapons", "dash_cooldown");

    public static final CustomPayload.Id<DashCooldownS2CPacket> PACKET_ID =
            new CustomPayload.Id<>(ID);

    public static final PacketCodec<ByteBuf, DashCooldownS2CPacket> CODEC =
            PacketCodecs.INTEGER
                    .xmap(DashCooldownS2CPacket::new, DashCooldownS2CPacket::cooldown);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}