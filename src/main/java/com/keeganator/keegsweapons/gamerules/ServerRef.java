package com.keeganator.keegsweapons.gamerules;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerRef {
    private static MinecraftServer server;

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTING.register(s -> server = s);
        ServerLifecycleEvents.SERVER_STOPPED.register(s -> server = null);
    }

    public static boolean weaponForgeStructuresEnabled() {
        if (server == null) return true;
        return server.getOverworld().getGameRules().getValue(ModGamerules.DO_WEAPON_FORGE_STRUCTURES_SPAWN);
    }

    public static boolean weaponForgeRecipeBlockingEnabled() {
        if (server == null) return true;
        return server.getOverworld().getGameRules().getValue(ModGamerules.DO_WEAPON_FORGE_RECIPE_BLOCKING);
    }
}
