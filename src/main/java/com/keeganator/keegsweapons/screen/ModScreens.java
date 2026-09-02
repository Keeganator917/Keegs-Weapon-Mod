package com.keeganator.keegsweapons.screen;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeRegistries;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.Identifier;

public class ModScreens {
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("keegsweapons", "textures/gui/weapon_forge.png");

    public static void register() {
        MenuScreens.register(WeaponForgeRegistries.WEAPON_FORGE_SCREEN_HANDLER, WeaponForgeScreen::new);
    }
}