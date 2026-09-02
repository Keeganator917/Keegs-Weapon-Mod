package com.keeganator.keegsweapons.compat.jei;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.block.ModBlocks;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeRegistries;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeScreenHandler;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponType;
import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponForgeRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JeiPlugin
public class KeegsWeaponsJeiPlugin implements IModPlugin {

    private final Map<WeaponType, WeaponForgeRecipeCategory> categories = new EnumMap<>(WeaponType.class);

    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();

        categories.put(WeaponType.SCYTHE, new WeaponForgeRecipeCategory(WeaponType.SCYTHE, ModBlocks.SCYTHE_WEAPON_FORGE, guiHelper));
        categories.put(WeaponType.DAGGER, new WeaponForgeRecipeCategory(WeaponType.DAGGER, ModBlocks.DAGGER_WEAPON_FORGE, guiHelper));
        categories.put(WeaponType.KATANA, new WeaponForgeRecipeCategory(WeaponType.KATANA, ModBlocks.KATANA_WEAPON_FORGE, guiHelper));
        categories.put(WeaponType.GREATSWORD, new WeaponForgeRecipeCategory(WeaponType.GREATSWORD, ModBlocks.GREATSWORD_WEAPON_FORGE, guiHelper));

        registration.addRecipeCategories(categories.values().toArray(WeaponForgeRecipeCategory[]::new));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(categories.get(WeaponType.SCYTHE).getRecipeType(), ModBlocks.SCYTHE_WEAPON_FORGE);
        registration.addCraftingStation(categories.get(WeaponType.DAGGER).getRecipeType(), ModBlocks.DAGGER_WEAPON_FORGE);
        registration.addCraftingStation(categories.get(WeaponType.KATANA).getRecipeType(), ModBlocks.KATANA_WEAPON_FORGE);
        registration.addCraftingStation(categories.get(WeaponType.GREATSWORD).getRecipeType(), ModBlocks.GREATSWORD_WEAPON_FORGE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;

        Collection<RecipeHolder<WeaponForgeRecipe>> allRecipes = level.recipeAccess()
                .getSynchronizedRecipes()
                .getAllOfType(WeaponForgeRegistries.WEAPON_FORGE_RECIPE_TYPE);

        for (WeaponType type : categories.keySet()) {
            List<RecipeHolder<WeaponForgeRecipe>> filtered = allRecipes.stream()
                    .filter(holder -> holder.value().getWeaponType() == type)
                    .collect(Collectors.toList());

            registration.addRecipes(categories.get(type).getRecipeType(), filtered);
        }
    }

    @Override
    public void registerRecipeTransferHandlers(
            IRecipeTransferRegistration registration) {

        registration.addRecipeTransferHandler(WeaponForgeScreenHandler.class, WeaponForgeRegistries.WEAPON_FORGE_SCREEN_HANDLER,
                categories.get(WeaponType.SCYTHE).getRecipeType(), 0, 49, 50, 36
        );

        registration.addRecipeTransferHandler(WeaponForgeScreenHandler.class, WeaponForgeRegistries.WEAPON_FORGE_SCREEN_HANDLER,
                categories.get(WeaponType.DAGGER).getRecipeType(), 0, 49, 50, 36
        );

        registration.addRecipeTransferHandler(WeaponForgeScreenHandler.class, WeaponForgeRegistries.WEAPON_FORGE_SCREEN_HANDLER,
                categories.get(WeaponType.KATANA).getRecipeType(), 0, 49, 50, 36
        );

        registration.addRecipeTransferHandler(WeaponForgeScreenHandler.class, WeaponForgeRegistries.WEAPON_FORGE_SCREEN_HANDLER,
                categories.get(WeaponType.GREATSWORD).getRecipeType(), 0, 49, 50, 36
        );
    }

}