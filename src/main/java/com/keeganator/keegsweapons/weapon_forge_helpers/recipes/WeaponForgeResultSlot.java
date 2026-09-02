package com.keeganator.keegsweapons.weapon_forge_helpers.recipes;

import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class WeaponForgeResultSlot extends Slot {

    private final WeaponForgeScreenHandler handler;
    private final PlayerEntity player;

    public WeaponForgeResultSlot(PlayerEntity player, WeaponForgeScreenHandler handler, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
        this.handler = handler;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        super.onTakeItem(player, stack);

        handler.consumeInputs();

        if (!player.getEntityWorld().isClient() && player.getEntityWorld() instanceof net.minecraft.server.world.ServerWorld serverWorld) {
            WeaponGlobalState state = WeaponGlobalState.getServerState(serverWorld);

            String weaponId = net.minecraft.registry.Registries.ITEM
                    .getId(stack.getItem()).toString();

            state.setCrafted(weaponId);
        }

        handler.onContentChanged(handler.input);
    }
}