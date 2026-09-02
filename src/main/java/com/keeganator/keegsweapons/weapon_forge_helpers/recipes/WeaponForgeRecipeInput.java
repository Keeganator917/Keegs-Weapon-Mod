package com.keeganator.keegsweapons.weapon_forge_helpers.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public record WeaponForgeRecipeInput(int width, int height, DefaultedList<ItemStack> stacks) implements RecipeInput {

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < 0 || slot >= stacks.size()) {
            return ItemStack.EMPTY;
        }
        return stacks.get(slot);
    }

    @Override
    public int size() {
        return 49;
    }

    @Override
    public boolean isEmpty() {
        return stacks.stream().allMatch(ItemStack::isEmpty);
    }
}