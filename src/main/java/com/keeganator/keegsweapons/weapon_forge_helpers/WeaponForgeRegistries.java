package com.keeganator.keegsweapons.weapon_forge_helpers;

import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponForgeRecipe;
import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponForgeRecipeSerializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class WeaponForgeRegistries {
    public static final String MOD_ID = "keegsweapons";


    // Screen Handler
    public static final MenuType<WeaponForgeScreenHandler> WEAPON_FORGE_SCREEN_HANDLER =
            new MenuType<>((syncId, inv) -> new WeaponForgeScreenHandler(syncId, inv, ContainerLevelAccess.NULL, null), FeatureFlags.DEFAULT_FLAGS);

    // Recipe Serializer
    public static final RecipeSerializer<WeaponForgeRecipe> WEAPON_FORGE_SERIALIZER = WeaponForgeRecipeSerializer.INSTANCE;

    //Recipe Type
    public static final RecipeType<WeaponForgeRecipe> WEAPON_FORGE_RECIPE_TYPE = new RecipeType<>() {
        @Override public String toString() { return MOD_ID + ":weapon_forge"; }
    };


    public static void registerWeaponForge() {

        //Recipe Type
        Registry.register(BuiltInRegistries.RECIPE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "weapon_forge"), WEAPON_FORGE_RECIPE_TYPE);

        // Register Serializer
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(MOD_ID, "weapon_forge"), WEAPON_FORGE_SERIALIZER);

        // Register Screen Handler
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(MOD_ID, "weapon_forge"), WEAPON_FORGE_SCREEN_HANDLER);

    }
}