package com.keeganator.keegsweapons.mixin;

import net.minecraft.entity.LightningEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningEntity.class)
public abstract class LightningEntityMixin {

    @Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
    private void keegsweapons$preventFire(int spreadAttempts, CallbackInfo ci) {
        LightningEntity self = (LightningEntity) (Object) this;

        if (self.getCommandTags().contains("keegsweapons:no_fire")) {
            ci.cancel();
        }
    }
}