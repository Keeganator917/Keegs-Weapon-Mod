package com.keeganator.keegsweapons.events;

import com.keeganator.keegsweapons.network.MyModNetwork;
import com.keeganator.keegsweapons.util.DashKeyUtil;
import com.keeganator.keegsweapons.util.ModKeyBinding;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class DashEvent {

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            if (DashKeyUtil.isDashBoundToRightClick(ModKeyBinding.DASH_KEY)) {
                return;
            }

            while (ModKeyBinding.DASH_KEY.consumeClick()) {
                MyModNetwork.sendDash();
            }
        });
    }
}
