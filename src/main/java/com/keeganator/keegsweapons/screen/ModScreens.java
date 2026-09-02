package com.keeganator.keegsweapons.screen;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeRegistries;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.util.Identifier;

public class ModScreens {
    public static final Identifier TEXTURE = Identifier.of("keegsweapons", "textures/gui/weapon_forge.png");

    public static void register() {
        HandledScreens.register(
                WeaponForgeRegistries.WEAPON_FORGE_SCREEN_HANDLER,
                WeaponForgeScreen::new
        );
    }
}