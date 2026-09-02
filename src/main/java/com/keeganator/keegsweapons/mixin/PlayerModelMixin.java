package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.util.TwoHandedPoseRenderState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public class PlayerModelMixin {

    @Shadow @Final public ModelPart rightSleeve;
    @Shadow @Final public ModelPart leftSleeve;

    @Inject(method = "setAngles(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)V", at = @At("TAIL"))
    private void keegsweapons$twoHandedIdlePose(PlayerEntityRenderState state, CallbackInfo ci) {
        if (!((TwoHandedPoseRenderState) state).keegsweapons$isTwoHanded()) {
            return;
        }

        if (state.isUsingItem) return;

        PlayerEntityModel model = (PlayerEntityModel) (Object) this;
        setTwoHandedPose(model);
    }

    @Unique
    private void setTwoHandedPose(PlayerEntityModel model) {
        // Arms
        model.rightArm.pitch = (float) Math.toRadians(-45);
        model.rightArm.yaw   = (float) Math.toRadians(-26);
        model.rightArm.roll  = 0.0F;

        model.leftArm.pitch  = (float) Math.toRadians(-48);
        model.leftArm.yaw    = (float) Math.toRadians(38);
        model.leftArm.roll   = (float) Math.toRadians(18);


        // Sleeves mirror arms
        rightSleeve.pitch = model.rightArm.pitch + (rightSleeve.pitch - model.rightArm.pitch);
        rightSleeve.yaw   = model.rightArm.yaw   + (rightSleeve.yaw   - model.rightArm.yaw);
        rightSleeve.roll  = model.rightArm.roll  + (rightSleeve.roll  - model.rightArm.roll);

        leftSleeve.pitch  = model.leftArm.pitch  + (leftSleeve.pitch  - model.leftArm.pitch);
        leftSleeve.yaw    = model.leftArm.yaw    + (leftSleeve.yaw    - model.leftArm.yaw);
        leftSleeve.roll   = model.leftArm.roll   + (leftSleeve.roll   - model.leftArm.roll);
    }
}