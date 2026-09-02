package com.keeganator.keegsweapons.compat.jei;

import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponForgeRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;
import java.util.Optional;

public class WeaponForgeRecipeCategory implements IRecipeCategory<WeaponForgeRecipe> {

    private final RecipeType<WeaponForgeRecipe> recipeType;
    private final Text title;
    private final IDrawable icon;
    //private final IDrawable background;

    public WeaponForgeRecipeCategory(IGuiHelper guiHelper, RecipeType<WeaponForgeRecipe> recipeType, Text title, ItemStack iconStack) {
        this.recipeType = recipeType;
        this.title = title;
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, iconStack);
    }

    @Override
    public RecipeType<WeaponForgeRecipe> getRecipeType() {
        return recipeType;
    }

    @Override
    public Text getTitle() {
        return title;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return 180;
    }

    @Override
    public int getHeight() {
        return 134;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WeaponForgeRecipe recipe, IFocusGroup focuses) {
        int width = recipe.getWidth();
        int height = recipe.getHeight();

        // 7x7 display area (126x126 px)
        int startX = (7 - width) * 18 / 2;
        int startY = (7 - height) * 18 / 2;

        DefaultedList<Optional<Ingredient>> ingredients = recipe.getIngredients();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int index = col + row * width;

                if (index >= ingredients.size()) continue;

                Optional<Ingredient> optIngredient = ingredients.get(index);

                // Skip empty slots
                if (optIngredient.isEmpty()) continue;

                Ingredient ingredient = optIngredient.get();
                if (ingredient.isEmpty()) continue; // extra safety for Ingredient.EMPTY

                builder.addSlot(RecipeIngredientRole.INPUT,
                                startX + col * 18 + 1,
                                startY + row * 18 + 1)
                        .setStandardSlotBackground()
                        .addIngredients(ingredient);
            }
        }

        // Output slot (7*18=126px wide)
        builder.addSlot(RecipeIngredientRole.OUTPUT, 150, 58)
                .setOutputSlotBackground().addItemStack(recipe.getOutput());
    }
}