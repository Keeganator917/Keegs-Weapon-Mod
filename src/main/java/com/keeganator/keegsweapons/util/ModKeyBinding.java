package com.keeganator.keegsweapons.util;

import com.keeganator.keegsweapons.network.MyModNetwork;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class ModKeyBinding {
    public static KeyMapping DASH_KEY;

    public static void register() {

        DASH_KEY = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.keegsweapons.dash",
                        InputConstants.Type.MOUSE, 1,
                        KeyMapping.Category.register(Identifier.parse("key.category.keegsweapons.keegsweapons"))
                )
        );

    }

    public static void registerTickHandler() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (DASH_KEY.consumeClick()) {
                MyModNetwork.sendDash();
            }
        });
    }
}