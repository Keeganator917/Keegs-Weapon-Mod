package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.util.TwoHandedPoseRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class PlayerModelMixin {

    @Shadow @Final public ModelPart rightSleeve;
    @Shadow @Final public ModelPart leftSleeve;

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
    private void keegsweapons$twoHandedIdlePose(AvatarRenderState state, CallbackInfo ci) {
        if (!((TwoHandedPoseRenderState) state).keegsweapons$isTwoHanded()) {
            return;
        }

        if (state.isUsingItem) return;

        PlayerModel model = (PlayerModel) (Object) this;
        setTwoHandedPose(model);
    }

    @Unique
    private void setTwoHandedPose(PlayerModel model) {
        // Arms
        model.rightArm.xRot = (float) Math.toRadians(-45);
        model.rightArm.yRot   = (float) Math.toRadians(-26);
        model.rightArm.zRot  = 0.0F;

        model.leftArm.xRot  = (float) Math.toRadians(-48);
        model.leftArm.yRot    = (float) Math.toRadians(38);
        model.leftArm.zRot   = (float) Math.toRadians(18);


        // Sleeves mirror arms
        rightSleeve.xRot = model.rightArm.xRot + (rightSleeve.xRot - model.rightArm.xRot);
        rightSleeve.yRot   = model.rightArm.yRot   + (rightSleeve.yRot   - model.rightArm.yRot);
        rightSleeve.zRot  = model.rightArm.zRot  + (rightSleeve.zRot  - model.rightArm.zRot);

        leftSleeve.xRot  = model.leftArm.xRot  + (leftSleeve.xRot  - model.leftArm.xRot);
        leftSleeve.yRot    = model.leftArm.yRot    + (leftSleeve.yRot    - model.leftArm.yRot);
        leftSleeve.zRot   = model.leftArm.zRot   + (leftSleeve.zRot   - model.leftArm.zRot);

    }
}