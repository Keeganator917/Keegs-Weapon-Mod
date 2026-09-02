package com.keeganator.keegsweapons.network;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record DoubleJumpPayload() implements CustomPayload {

    public static final Id<DoubleJumpPayload> ID = new Id<>(Identifier.of("keegsweapons", "double_jump"));

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}