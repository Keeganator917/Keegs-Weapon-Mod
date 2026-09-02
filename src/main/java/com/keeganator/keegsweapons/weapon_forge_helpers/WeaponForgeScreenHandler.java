package com.keeganator.keegsweapons.weapon_forge_helpers;

import com.keeganator.keegsweapons.block.custom.WeaponForgeBlock;
import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponForgeRecipe;
import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponForgeRecipeInput;
import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponForgeResultSlot;
import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponGlobalState;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

public class WeaponForgeScreenHandler extends AbstractContainerMenu {

    public static final int GRID_SIZE = 7;
    public static final int INPUT_SIZE = GRID_SIZE * GRID_SIZE;

    public final SimpleContainer input = new SimpleContainer(INPUT_SIZE) {
        @Override
        public void setChanged() {
            super.setChanged();
            WeaponForgeScreenHandler.this.slotsChanged(this);
        }
    };
    private final SimpleContainer result = new SimpleContainer(1);
    private final ContainerLevelAccess context;
    private final Player player;
    private final WeaponType weaponType;

    public WeaponForgeScreenHandler(int syncId, Inventory playerInventory, ContainerLevelAccess context, @Nullable WeaponType weaponType) {
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
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                WeaponForgeScreenHandler.this.consumeInputs();

                // Mark it globally as crafted through WeaponGlobalState
                if (!player.level().isClientSide() && player.level() instanceof net.minecraft.server.level.ServerLevel serverWorld) {
                    WeaponGlobalState state = WeaponGlobalState.getServerState(serverWorld);
                    String weaponId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
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
    }

    @Override
    public void slotsChanged(Container inventory) {
        this.context.execute((world, pos) -> updateResult(world));
    }

    public void consumeInputs() {
        for (int i = 0; i < input.getContainerSize(); i++) {
            this.input.removeItem(i, 1);
        }
    }

    private void updateResult(Level world) {
        if (world.isClientSide()) return;

        NonNullList<ItemStack> stacks = NonNullList.withSize(INPUT_SIZE, ItemStack.EMPTY);
        for(int i = 0; i < INPUT_SIZE; i++) {
            stacks.set(i, input.getItem(i));
        }
        WeaponForgeRecipeInput recipeInput = WeaponForgeRecipeInput.of(GRID_SIZE, GRID_SIZE, stacks);


        var matches = world.recipeAccess().getSynchronizedRecipes()
                .getAllMatches(WeaponForgeRegistries.WEAPON_FORGE_RECIPE_TYPE, recipeInput, world);

        Optional<RecipeHolder<WeaponForgeRecipe>> match = matches
                .filter(entry -> entry.value().getWeaponType() == this.weaponType)
                .findFirst();

        if (match.isPresent()) {
            ItemStack output = match.get().value().assemble(recipeInput);

            if (world instanceof ServerLevel serverWorld) {
                WeaponGlobalState globalState = WeaponGlobalState.getServerState(serverWorld);
                String weaponId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(output.getItem()).toString();

                if (globalState.hasBeenCrafted(weaponId)) {
                    this.result.setItem(0, ItemStack.EMPTY);
                    return;
                }
            }

            this.result.setItem(0, output);
        } else {
            this.result.setItem(0, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.context.evaluate((world, pos) ->
                        world.getBlockState(pos).getBlock() instanceof WeaponForgeBlock).orElse(true);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();

            //Result slot
            if (slotIndex == 49) {
                slot.onTake(player, itemStack2);
            }

            // Quick move from crafting grid
            if (slotIndex < 50) {
                if (!this.moveItemStackTo(itemStack2, 50, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            // Quick move from inventory
            else if (!this.moveItemStackTo(itemStack2, 0, 49, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemStack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.context.execute((world, pos) -> {
            this.clearContainer(player, this.input);
        });
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }
}