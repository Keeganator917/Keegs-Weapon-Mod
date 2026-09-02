package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.item.ModItems;
import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow
    public abstract ItemStack getStack();

    @Inject(method = "tick", at = @At("HEAD"))
    private void keegsweapons$preventDespawn(CallbackInfo ci) {
        ItemEntity itemEntity = (ItemEntity) (Object) this;
        ItemStack stack = getStack();

        if (stack.isIn(ModTags.Items.LEGENDARY_RARITY)) {
            itemEntity.setNeverDespawn();
        }
    }
}