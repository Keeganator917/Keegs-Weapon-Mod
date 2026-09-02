package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.util.ModTags;
import com.keeganator.keegsweapons.util.TwoHandedPoseRenderState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @Inject(method = "updateRenderState", at = @At("TAIL"))
    private void keegsweapons$setTwoHandedFlag(PlayerLikeEntity player, PlayerEntityRenderState state, float tickDelta, CallbackInfo ci) {
        ItemStack stack = player.getMainHandStack();

        boolean twoHanded = stack.isIn(ModTags.Items.GREATSWORD) || stack.isIn(ModTags.Items.KATANA);

        ((TwoHandedPoseRenderState) state).keegsweapons$setTwoHanded(twoHanded);
    }
}