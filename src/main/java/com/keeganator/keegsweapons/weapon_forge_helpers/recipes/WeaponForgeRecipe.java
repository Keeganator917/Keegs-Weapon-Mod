package com.keeganator.keegsweapons.weapon_forge_helpers.recipes;

import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeRegistries;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class WeaponForgeRecipe implements Recipe<WeaponForgeRecipeInput> {
    final int width;
    final int height;
    final NonNullList<Optional<Ingredient>> ingredients;
    final ItemStackTemplate output;
    private final WeaponType weaponType;
    private final List<String> pattern;
    private final Map<String, Ingredient> keyMap;

    public WeaponForgeRecipe(WeaponType weaponType, int width, int height, NonNullList<Optional<Ingredient>> ingredients, ItemStackTemplate output, List<String> pattern, Map<String, Ingredient> keyMap) {
        this.weaponType = weaponType;
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        this.output = output;
        this.pattern = pattern;
        this.keyMap = keyMap;
    }

    @Override
    public boolean matches(WeaponForgeRecipeInput input, Level level) {
        if (input == null || input.isEmpty()) return false;
        if (input.width() != this.width || input.height() != this.height) return false;

        for (int i = 0; i < this.ingredients.size(); i++) {
            Optional<Ingredient> ingredient = this.ingredients.get(i);
            ItemStack stack = input.getItem(i);
            if (ingredient.isPresent()) {
                if (!ingredient.get().test(stack)) return false;
            } else if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(WeaponForgeRecipeInput input) {
        return this.output.create();
    }

    @Override
    public RecipeSerializer<? extends Recipe<WeaponForgeRecipeInput>> getSerializer() {
        return WeaponForgeRegistries.WEAPON_FORGE_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<WeaponForgeRecipeInput>> getType() {
        return WeaponForgeRegistries.WEAPON_FORGE_RECIPE_TYPE;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public NonNullList<Optional<Ingredient>> getIngredients() { return ingredients; }
    public ItemStackTemplate getOutput() { return output; }
    public WeaponType getWeaponType() {
        return weaponType;
    }
    public List<String> getPattern() { return pattern; }
    public Map<String, Ingredient> getKeyMap() { return keyMap; }

    @Override
    public PlacementInfo placementInfo() {
        List<Ingredient> actualIngredients = this.ingredients.stream()
                .flatMap(Optional::stream)
                .toList();
        return PlacementInfo.create(actualIngredients);
    }
    @Override
    public RecipeBookCategory recipeBookCategory() { return null; }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

}