package com.keeganator.keegsweapons.util;

import com.keeganator.keegsweapons.network.MyModNetwork;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;

import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class ModKeyBinding {
    public static KeyBinding DASH_KEY;

    public static void register() {
        DASH_KEY = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.keegsweapons.dash",
                        InputUtil.Type.MOUSE, 1,
                        KeyBinding.Category.create(Identifier.of("key.category.keegsweapons.keegsweapons"))
                )
        );
    }

    public static void registerTickHandler() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (DASH_KEY.wasPressed()) {
                MyModNetwork.sendDash();
            }
        });
    }
}