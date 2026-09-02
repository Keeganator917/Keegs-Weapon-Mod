package com.keeganator.keegsweapons.weapon_forge_helpers;

import com.keeganator.keegsweapons.block.custom.WeaponForgeBlock;
import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponForgeRecipe;
import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponForgeRecipeInput;
import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponForgeResultSlot;
import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponGlobalState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WeaponForgeScreenHandler extends ScreenHandler {

    public static final int GRID_SIZE = 7;
    public static final int INPUT_SIZE = GRID_SIZE * GRID_SIZE;

    public final SimpleInventory input = new SimpleInventory(INPUT_SIZE) {
        @Override
        public void markDirty() {
            super.markDirty();
            WeaponForgeScreenHandler.this.onContentChanged(this);
        }
    };
    private final SimpleInventory result = new SimpleInventory(1);
    private final ScreenHandlerContext context;
    private final PlayerEntity player;
    private final WeaponType weaponType;

    public WeaponForgeScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, @Nullable WeaponType weaponType) {
        super(WeaponForgeRegistries.WEAPON_FORGE_SCREEN_HANDLER, syncId);
        this.context = context;
        this.player = playerInventory.player;
        this.weaponType = weaponType;

        // Crafting Grid
        for (int row = 0; row < 7; ++row) {
            for (int col = 0; col < 7; ++col) {
                this.addSlot(new Slot(this.input, col + row * 7, 31 + col * 18, 19 + row * 18));
            }
        }

        // Result Slot
        this.addSlot(new WeaponForgeResultSlot(player, this, this.result, 0, 194, 74) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                WeaponForgeScreenHandler.this.consumeInputs();

                // Mark it globally as crafted through WeaponGlobalState
                if (!player.getEntityWorld().isClient() && player.getEntityWorld() instanceof ServerWorld serverWorld) {
                    WeaponGlobalState state = WeaponGlobalState.getServerState(serverWorld);
                    String weaponId = net.minecraft.registry.Registries.ITEM.getId(stack.getItem()).toString();
                    state.setCrafted(weaponId);
                }
            }
        });

        // Inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 31 + col * 18, 164 + row * 18));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 31 + col * 18, 222));
        }

        var recipes = player.getEntityWorld().getRecipeManager().getSynchronizedRecipes().getAllOfType(WeaponForgeRegistries.WEAPON_FORGE_RECIPE_TYPE);

        System.out.println("SERVER WEAPON FORGE RECIPES: " + recipes.size());
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run((world, pos) -> updateResult(world));
    }

    public void consumeInputs() {
        for (int i = 0; i < input.size(); i++) {
            this.input.removeStack(i, 1);
        }
    }

    private void updateResult(World world) {
        if (world.isClient()) return;

        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(INPUT_SIZE, ItemStack.EMPTY);
        for(int i = 0; i < INPUT_SIZE; i++) {
            stacks.set(i, input.getStack(i));
        }
        WeaponForgeRecipeInput recipeInput = new WeaponForgeRecipeInput(GRID_SIZE, GRID_SIZE, stacks);


        var matches = world.getRecipeManager().getSynchronizedRecipes()
                .getAllMatches(WeaponForgeRegistries.WEAPON_FORGE_RECIPE_TYPE, recipeInput, world);

        Optional<RecipeEntry<WeaponForgeRecipe>> match = matches
                .filter(entry -> entry.value().getWeaponType() == this.weaponType)
                .findFirst();

        if (match.isPresent()) {
            ItemStack output = match.get().value().craft(recipeInput, world.getRegistryManager());

            if (world instanceof ServerWorld serverWorld) {
                WeaponGlobalState globalState = WeaponGlobalState.getServerState(serverWorld);
                String weaponId = net.minecraft.registry.Registries.ITEM.getId(output.getItem()).toString();

                if (globalState.hasBeenCrafted(weaponId)) {
                    this.result.setStack(0, ItemStack.EMPTY);
                    return;
                }
            }

            this.result.setStack(0, output);
        } else {
            this.result.setStack(0, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.context.get((world, pos) ->
                        world.getBlockState(pos).getBlock() instanceof WeaponForgeBlock).orElse(true);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();

            //Result slot
            if (slotIndex == 49) {
                slot.onTakeItem(player, itemStack2);
            }

            // Quick move from crafting grid
            if (slotIndex < 50) {
                if (!this.insertItem(itemStack2, 50, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            // Quick move from inventory
            else if (!this.insertItem(itemStack2, 0, 49, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return itemStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> {
            this.dropInventory(player, this.input);
        });
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }
}