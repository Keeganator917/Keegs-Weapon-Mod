package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.ForgingSlotsManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ForgingSlotsManager forgingSlotsManager) {
        super(type, syncId, playerInventory, context, forgingSlotsManager);
    }

    @Inject(method = "updateResult", at = @At("RETURN"))
    private void restrictLegendaryAnvilCombinations(CallbackInfo ci) {
        ItemStack result = this.output.getStack(0);
        if (result.isEmpty()) return;

        if (result.isIn(ModTags.Items.LEGENDARY_RARITY)) {
            ItemEnchantmentsComponent enchantments = result.get(DataComponentTypes.ENCHANTMENTS);

            if (enchantments != null && enchantments.getSize() > 1) {
                this.output.setStack(0, ItemStack.EMPTY);
                this.sendContentUpdates();
            }
        }
    }
}