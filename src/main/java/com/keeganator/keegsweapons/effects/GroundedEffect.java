package com.keeganator.keegsweapons.effects;

import com.keeganator.keegsweapons.damagetypes.ModDamageTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;

public class GroundedEffect extends MobEffect {

    public GroundedEffect() {
        super(MobEffectCategory.HARMFUL, 0xB838CF);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public boolean applyEffectTick(ServerLevel world, LivingEntity entity, int amplifier) {
        if (entity.isFallFlying() || entity instanceof Phantom) {
            entity.hurtServer(world, ModDamageTypes.groundedDamage(world), 2 + amplifier);
            entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 40, 2 + amplifier, false, false));
        }

        return true;
    }
}