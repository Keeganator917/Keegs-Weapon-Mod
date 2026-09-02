package com.keeganator.keegsweapons.compat.jei;

import com.keeganator.keegsweapons.block.ModBlocks;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeRegistries;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeScreenHandler;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponType;
import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponForgeRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

@JeiPlugin
public class KeegsWeaponsJeiPlugin implements IModPlugin {

    public static final Identifier PLUGIN_ID = Identifier.of("keegsweapons", "jei_plugin");

    public static final RecipeType<WeaponForgeRecipe> SCYTHE_TYPE =
            new RecipeType<>(Identifier.of("keegsweapons", "scythe_weapon_forge"), WeaponForgeRecipe.class);

    public static final RecipeType<WeaponForgeRecipe> DAGGER_TYPE =
            new RecipeType<>(Identifier.of("keegsweapons", "dagger_weapon_forge"), WeaponForgeRecipe.class);

    public static final RecipeType<WeaponForgeRecipe> KATANA_TYPE =
            new RecipeType<>(Identifier.of("keegsweapons", "katana_weapon_forge"), WeaponForgeRecipe.class);

    public static final RecipeType<WeaponForgeRecipe> GREATSWORD_TYPE =
            new RecipeType<>(Identifier.of("keegsweapons", "greatsword_weapon_forge"), WeaponForgeRecipe.class);

    @Override
    public Identifier getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(
                new WeaponForgeRecipeCategory(guiHelper, SCYTHE_TYPE, Text.literal("Scythe Weapon Forge"), new ItemStack(ModBlocks.SCYTHE_WEAPON_FORGE)),
                new WeaponForgeRecipeCategory(guiHelper, DAGGER_TYPE, Text.literal("Dagger Weapon Forge"), new ItemStack(ModBlocks.DAGGER_WEAPON_FORGE)),
                new WeaponForgeRecipeCategory(guiHelper, KATANA_TYPE, Text.literal("Katana Weapon Forge"), new ItemStack(ModBlocks.KATANA_WEAPON_FORGE)),
                new WeaponForgeRecipeCategory(guiHelper, GREATSWORD_TYPE, Text.literal("Greatsword Weapon Forge"), new ItemStack(ModBlocks.GREATSWORD_WEAPON_FORGE))
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var client = MinecraftClient.getInstance();
        var world = client.world;

        assert world != null;
        RecipeManager recipeManager = world.getRecipeManager();

        List<WeaponForgeRecipe> allRecipes = recipeManager.getSynchronizedRecipes()
                .recipes()
                .stream()
                .map(RecipeEntry::value)
                .filter(r -> r instanceof WeaponForgeRecipe)
                .map(r -> (WeaponForgeRecipe) r)
                .toList();

        registration.addRecipes(SCYTHE_TYPE, allRecipes.stream()
                .filter(r -> r.getWeaponType() == WeaponType.SCYTHE).toList());
        registration.addRecipes(DAGGER_TYPE, allRecipes.stream()
                .filter(r -> r.getWeaponType() == WeaponType.DAGGER).toList());
        registration.addRecipes(KATANA_TYPE, allRecipes.stream()
                .filter(r -> r.getWeaponType() == WeaponType.KATANA).toList());
        registration.addRecipes(GREATSWORD_TYPE, allRecipes.stream()
                .filter(r -> r.getWeaponType() == WeaponType.GREATSWORD).toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SCYTHE_WEAPON_FORGE), SCYTHE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DAGGER_WEAPON_FORGE), DAGGER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.KATANA_WEAPON_FORGE), KATANA_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.GREATSWORD_WEAPON_FORGE), GREATSWORD_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        // Enable Auto-Complete
        registration.addRecipeTransferHandler(WeaponForgeScreenHandler.class, WeaponForgeRegistries.WEAPON_FORGE_SCREEN_HANDLER, SCYTHE_TYPE, 0, 49, 50, 36);
        registration.addRecipeTransferHandler(WeaponForgeScreenHandler.class, WeaponForgeRegistries.WEAPON_FORGE_SCREEN_HANDLER, DAGGER_TYPE, 0, 49, 50, 36);
        registration.addRecipeTransferHandler(WeaponForgeScreenHandler.class, WeaponForgeRegistries.WEAPON_FORGE_SCREEN_HANDLER, KATANA_TYPE, 0, 49, 50, 36);
        registration.addRecipeTransferHandler(WeaponForgeScreenHandler.class, WeaponForgeRegistries.WEAPON_FORGE_SCREEN_HANDLER, GREATSWORD_TYPE, 0, 49, 50, 36);
    }


}