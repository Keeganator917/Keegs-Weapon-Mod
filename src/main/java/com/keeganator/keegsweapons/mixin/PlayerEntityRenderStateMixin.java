package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.util.TwoHandedPoseRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public class PlayerEntityRenderStateMixin implements TwoHandedPoseRenderState {

    @Unique
    private boolean keegsweapons$twoHanded;

    @Override
    public boolean keegsweapons$isTwoHanded() {
        return keegsweapons$twoHanded;
    }

    @Override
    public void keegsweapons$setTwoHanded(boolean value) {
        this.keegsweapons$twoHanded = value;
    }
}