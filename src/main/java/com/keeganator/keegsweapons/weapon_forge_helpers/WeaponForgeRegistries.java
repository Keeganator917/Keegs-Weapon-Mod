package com.keeganator.keegsweapons.weapon_forge_helpers;

import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponForgeRecipe;
import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponForgeRecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class WeaponForgeRegistries {
    public static final String MOD_ID = "keegsweapons";

    // Screen Handler
    public static final ScreenHandlerType<WeaponForgeScreenHandler> WEAPON_FORGE_SCREEN_HANDLER =
            new ScreenHandlerType<>((syncId, inv) -> new WeaponForgeScreenHandler(syncId, inv, ScreenHandlerContext.EMPTY, null), FeatureFlags.DEFAULT_ENABLED_FEATURES);

    // Recipe Serializer
    public static final WeaponForgeRecipeSerializer WEAPON_FORGE_SERIALIZER = new WeaponForgeRecipeSerializer();

    //Recipe Type
    public static final RecipeType<WeaponForgeRecipe> WEAPON_FORGE_RECIPE_TYPE = new RecipeType<>() {
        @Override public String toString() { return MOD_ID + ":weapon_forge"; }
    };

    public static void registerWeaponForge() {
        //Recipe Type
        Registry.register(Registries.RECIPE_TYPE, Identifier.of(MOD_ID, "weapon_forge"), WEAPON_FORGE_RECIPE_TYPE);

        // Register Serializer
        Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(MOD_ID, "weapon_forge"), WEAPON_FORGE_SERIALIZER);

        // Register Screen Handler
        Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MOD_ID, "weapon_forge"), WEAPON_FORGE_SCREEN_HANDLER);
    }
}