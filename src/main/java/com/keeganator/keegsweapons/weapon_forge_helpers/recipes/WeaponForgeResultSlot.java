package com.keeganator.keegsweapons.weapon_forge_helpers.recipes;

import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeScreenHandler;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WeaponForgeResultSlot extends Slot {

    private final WeaponForgeScreenHandler handler;
    private final Player player;

    public WeaponForgeResultSlot(Player player, WeaponForgeScreenHandler handler, Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
        this.handler = handler;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        super.onTake(player, stack);

        handler.consumeInputs();

        if (!player.level().isClientSide() && player.level() instanceof net.minecraft.server.level.ServerLevel serverWorld) {
            WeaponGlobalState state = WeaponGlobalState.getServerState(serverWorld);

            String weaponId = net.minecraft.core.registries.BuiltInRegistries.ITEM
                    .getKey(stack.getItem()).toString();

            state.setCrafted(weaponId);
        }

        handler.slotsChanged(handler.input);
    }
}