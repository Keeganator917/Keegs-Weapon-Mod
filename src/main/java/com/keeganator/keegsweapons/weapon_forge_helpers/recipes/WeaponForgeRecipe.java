package com.keeganator.keegsweapons.weapon_forge_helpers.recipes;

import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeRegistries;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponType;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WeaponForgeRecipe implements Recipe<WeaponForgeRecipeInput> {
    final int width;
    final int height;
    final DefaultedList<Optional<Ingredient>> ingredients;
    final ItemStack output;
    private final WeaponType weaponType;
    private final List<String> pattern;
    private final Map<String, Ingredient> keyMap;

    public WeaponForgeRecipe(WeaponType weaponType, int width, int height, DefaultedList<Optional<Ingredient>> ingredients, ItemStack output, List<String> pattern, Map<String, Ingredient> keyMap) {
        this.weaponType = weaponType;
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        this.output = output;
        this.pattern = pattern;
        this.keyMap = keyMap;
    }

    @Override
    public boolean matches(WeaponForgeRecipeInput input, World world) {
        if (input == null || input.isEmpty()) return false;

        for (int i = 0; i <= 7 - this.width; ++i) {
            for (int j = 0; j <= 7 - this.height; ++j) {
                if (this.matchesPattern(input, i, j)) return true;
            }
        }
        return false;
    }

    private boolean matchesPattern(WeaponForgeRecipeInput inv, int offsetX, int offsetY) {
        for (int x = 0; x < 7; ++x) {
            for (int y = 0; y < 7; ++y) {
                int patternX = x - offsetX;
                int patternY = y - offsetY;

                Optional<Ingredient> ingredient = Optional.empty();
                if (patternX >= 0 && patternY >= 0 && patternX < this.width && patternY < this.height) {
                    ingredient = this.ingredients.get(patternX + patternY * this.width);
                }

                ItemStack stackInSlot = inv.getStackInSlot(x + y * 7);

                if (ingredient.isPresent()) {
                    if (!ingredient.get().test(stackInSlot)) return false;
                } else {
                    if (!stackInSlot.isEmpty()) return false;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(WeaponForgeRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return this.output.copy();
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
    public DefaultedList<Optional<Ingredient>> getIngredients() { return ingredients; }
    public ItemStack getOutput() { return output; }
    public WeaponType getWeaponType() {
        return weaponType;
    }
    public List<String> getPattern() { return pattern; }
    public Map<String, Ingredient> getKeyMap() { return keyMap; }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        List<Ingredient> actualIngredients = this.ingredients.stream()
                .flatMap(Optional::stream)
                .toList();
        return IngredientPlacement.forShapeless(actualIngredients);
    }
    @Override
    public RecipeBookCategory getRecipeBookCategory() { return null; }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

}