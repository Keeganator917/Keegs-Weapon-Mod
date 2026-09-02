package com.keeganator.keegsweapons.effects;

import com.keeganator.keegsweapons.damagetypes.ModDamageTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.server.world.ServerWorld;

public class GroundedEffect extends StatusEffect {

    public GroundedEffect() {
        super(StatusEffectCategory.HARMFUL, 0xB838CF);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if (entity.isGliding() || entity instanceof PhantomEntity) {
            entity.damage(world, ModDamageTypes.groundedDamage(world), 2 + amplifier);
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 2 + amplifier, false, false));
        }

        return true;
    }
}