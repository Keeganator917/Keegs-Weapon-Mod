package com.keeganator.keegsweapons.compat.jei;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponType;
import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponForgeRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;

public class WeaponForgeRecipeCategory implements IRecipeCategory<RecipeHolder<WeaponForgeRecipe>> {

    private static final int SLOT_SIZE = 18;

    private final WeaponType weaponType;
    private final IRecipeType<RecipeHolder<WeaponForgeRecipe>> recipeType;
    private final IDrawable icon;

    @SuppressWarnings("unchecked")
    public WeaponForgeRecipeCategory(WeaponType weaponType, ItemLike catalyst, IGuiHelper guiHelper) {
        this.weaponType = weaponType;
        this.recipeType = IRecipeType.create(
                KeegsWeapons.MOD_ID,
                weaponType.id() + "_weapon_forge",
                (Class<RecipeHolder<WeaponForgeRecipe>>) (Class<?>) RecipeHolder.class
        );
        this.icon = guiHelper.createDrawableItemLike(catalyst);
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    @Override
    public IRecipeType<RecipeHolder<WeaponForgeRecipe>> getRecipeType() {
        return recipeType;
    }

    @Override
    public Component getTitle() {
        return Component.translatable( "category.keegsweapons." + weaponType.id() + "_weapon_forge");
    }

    @Override
    public int getWidth() {
        return 130;
    }

    @Override
    public int getHeight() {
        return 126;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<WeaponForgeRecipe> recipeHolder, IFocusGroup focuses) {
        WeaponForgeRecipe recipe = recipeHolder.value();
        int width = recipe.getWidth();
        int height = recipe.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Optional<Ingredient> ingredient = recipe.getIngredients().get(x + y * width);
                if (ingredient.isPresent()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, x * SLOT_SIZE, y * SLOT_SIZE)
                            .setStandardSlotBackground()
                            .add(ingredient.get());
                }
            }
        }

        builder.addOutputSlot(width * SLOT_SIZE + 12, (height * SLOT_SIZE) / 2 - 8)
                .setOutputSlotBackground()
                .add(recipe.getOutput());
    }
}