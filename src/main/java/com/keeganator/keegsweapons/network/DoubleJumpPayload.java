package com.keeganator.keegsweapons.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record DoubleJumpPayload() implements CustomPacketPayload {

    public static final Type<DoubleJumpPayload> ID = new Type<>(Identifier.fromNamespaceAndPath("keegsweapons", "double_jump"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}