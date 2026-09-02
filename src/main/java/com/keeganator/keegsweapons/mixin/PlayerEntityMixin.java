package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    @Unique
    private static final EntityDataAccessor<Integer> DASH_TICK =
            SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.INT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void keegsweapons$registerDashTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DASH_TICK, 0);
    }

    // Gets rid of vanilla sweep for all legendary weapons
    @Redirect(method = "doSweepAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"))
    private int keegs_redirectSweepParticle(ServerLevel world, ParticleOptions particle, double x, double y, double z, int count, double dx, double dy, double dz, double speed) {
        Player player = (Player) (Object) this;
        ItemStack stack = player.getMainHandItem();

        if (stack.is(ModTags.Items.LEGENDARY_RARITY)) {
            return 0;
        }

        return world.sendParticles(particle, x, y, z, count, dx, dy, dz, speed);
    }

}