package com.keeganator.keegsweapons.mixin.util;

import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin {

    @Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
    private void keegsweapons$preventFire(int spreadAttempts, CallbackInfo ci) {
        LightningBolt self = (LightningBolt) (Object) this;


        if (self.entityTags().contains("keegsweapons:no_fire")) {
            ci.cancel();
        }

    }
}