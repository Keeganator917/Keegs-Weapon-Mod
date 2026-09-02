package com.keeganator.keegsweapons.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class DashKeyUtil {

    public static boolean isDashBoundToRightClick(KeyMapping dashKey) {
        return dashKey.getDefaultKey().equals(InputConstants.Type.MOUSE.getOrCreate(GLFW.GLFW_MOUSE_BUTTON_RIGHT));
    }
}