package com.keeganator.keegsweapons.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class FurnaceTracker {
    private static final Map<BlockPos, ServerPlayerEntity> furnaceUsers = new HashMap<>();

    public static void setPlayer(BlockPos pos, ServerPlayerEntity player) {
        furnaceUsers.put(pos, player);
    }

    public static ServerPlayerEntity getPlayer(BlockPos pos) {
        return furnaceUsers.get(pos);
    }

    public static void removePlayer(BlockPos pos) {
        furnaceUsers.remove(pos);
    }
}