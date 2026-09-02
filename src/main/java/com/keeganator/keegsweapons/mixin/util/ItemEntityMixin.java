package com.keeganator.keegsweapons.mixin.util;

import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "tick", at = @At("HEAD"))
    private void keegsweapons$preventDespawn(CallbackInfo ci) {
        ItemEntity itemEntity = (ItemEntity) (Object) this;
        ItemStack stack = getItem();

        if (stack.is(ModTags.Items.LEGENDARY_RARITY)) {
            itemEntity.setUnlimitedLifetime();
        }
    }
}