package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.util.ModTags;
import com.keeganator.keegsweapons.util.TwoHandedPoseRenderState;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class PlayerEntityRendererMixin {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void keegsweapons$setTwoHandedFlag(Avatar player, AvatarRenderState state, float tickDelta, CallbackInfo ci) {
        ItemStack stack = player.getMainHandItem();

        boolean twoHanded = stack.is(ModTags.Items.GREATSWORD) || stack.is(ModTags.Items.KATANA);

        ((TwoHandedPoseRenderState) state).keegsweapons$setTwoHanded(twoHanded);
    }
}