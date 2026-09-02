package com.keeganator.keegsweapons.weapon_forge_helpers.recipes;

import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record WeaponForgeRecipeInput(int width, int height, NonNullList<ItemStack> stacks) implements RecipeInput {

    public static final WeaponForgeRecipeInput EMPTY = new WeaponForgeRecipeInput(0, 0, NonNullList.create());

    public static Positioned ofPositioned(int gridWidth, int gridHeight, NonNullList<ItemStack> items) {
        if (gridWidth == 0 || gridHeight == 0) {
            return Positioned.EMPTY;
        }

        int left = gridWidth - 1;
        int right = 0;
        int top = gridHeight - 1;
        int bottom = 0;

        for (int y = 0; y < gridHeight; ++y) {
            boolean rowEmpty = true;
            for (int x = 0; x < gridWidth; ++x) {
                ItemStack stack = items.get(x + y * gridWidth);
                if (!stack.isEmpty()) {
                    left = Math.min(left, x);
                    right = Math.max(right, x);
                    rowEmpty = false;
                }
            }
            if (!rowEmpty) {
                top = Math.min(top, y);
                bottom = Math.max(bottom, y);
            }
        }

        int newWidth = right - left + 1;
        int newHeight = bottom - top + 1;

        if (newWidth <= 0 || newHeight <= 0) {
            return Positioned.EMPTY;
        }

        if (newWidth == gridWidth && newHeight == gridHeight) {
            return new Positioned(new WeaponForgeRecipeInput(gridWidth, gridHeight, items), left, top);
        }

        NonNullList<ItemStack> trimmed = NonNullList.withSize(newWidth * newHeight, ItemStack.EMPTY);
        for (int y = 0; y < newHeight; ++y) {
            for (int x = 0; x < newWidth; ++x) {
                trimmed.set(x + y * newWidth, items.get((x + left) + (y + top) * gridWidth));
            }
        }

        return new Positioned(new WeaponForgeRecipeInput(newWidth, newHeight, trimmed), left, top);
    }

    public static WeaponForgeRecipeInput of(int gridWidth, int gridHeight, NonNullList<ItemStack> items) {
        return ofPositioned(gridWidth, gridHeight, items).input();
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < 0 || slot >= stacks.size()) {
            return ItemStack.EMPTY;
        }
        return stacks.get(slot);
    }

    public ItemStack getItem(int x, int y) {
        return getItem(x + y * width);
    }

    @Override
    public int size() {
        return stacks.size();
    }

    @Override
    public boolean isEmpty() {
        return stacks.stream().allMatch(ItemStack::isEmpty);
    }

    public record Positioned(WeaponForgeRecipeInput input, int left, int top) {
        public static final Positioned EMPTY = new Positioned(WeaponForgeRecipeInput.EMPTY, 0, 0);
    }
}