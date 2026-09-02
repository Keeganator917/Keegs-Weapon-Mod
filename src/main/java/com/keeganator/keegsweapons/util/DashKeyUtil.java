package com.keeganator.keegsweapons.util;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class DashKeyUtil {

    public static boolean isDashBoundToRightClick(KeyBinding dashKey) {
        return dashKey.getDefaultKey().equals(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_RIGHT));
    }
}