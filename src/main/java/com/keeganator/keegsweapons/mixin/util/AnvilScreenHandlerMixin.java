package com.keeganator.keegsweapons.mixin.util;

import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilScreenHandlerMixin extends ItemCombinerMenu {

    public AnvilScreenHandlerMixin(@Nullable MenuType<?> type, int syncId, Inventory playerInventory, ContainerLevelAccess context, ItemCombinerMenuSlotDefinition forgingSlotsManager) {
        super(type, syncId, playerInventory, context, forgingSlotsManager);
    }

    @Inject(method = "createResult", at = @At("RETURN"))
    private void restrictLegendaryAnvilCombinations(CallbackInfo ci) {
        ItemStack result = this.resultSlots.getItem(0);
        if (result.isEmpty()) return;

        if (result.is(ModTags.Items.LEGENDARY_RARITY)) {
            ItemEnchantments enchantments = result.get(DataComponents.ENCHANTMENTS);

            if (enchantments != null && enchantments.size() > 1) {
                this.resultSlots.setItem(0, ItemStack.EMPTY);
                this.broadcastChanges();
            }
        }
    }
}