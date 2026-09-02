package com.keeganator.keegsweapons.gamerules;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;

public class ModGamerules {

    public static void init() {
        //intentionally empty to force game to load class
    }

    public static final GameRule<Boolean> DO_WEAPON_FORGE_STRUCTURES_SPAWN =
            GameRuleBuilder.forBoolean(true).buildAndRegister(Identifier.fromNamespaceAndPath("keegsweapons", "do_weapon_forge_structures_spawn"));

    public static final GameRule<Boolean> DO_WEAPON_FORGE_RECIPE_BLOCKING =
            GameRuleBuilder.forBoolean(true).buildAndRegister(Identifier.fromNamespaceAndPath("keegsweapons", "do_weapon_forge_recipe_blocking"));

}
