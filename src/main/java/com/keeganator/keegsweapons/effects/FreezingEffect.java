package com.keeganator.keegsweapons.effects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FreezingEffect extends MobEffect {

    public FreezingEffect() {
        super(MobEffectCategory.HARMFUL, 0x1E90FF);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        entity.setIsInPowderSnow(true);
        if (entity.canFreeze()) {
            entity.setTicksFrozen(Math.min(entity.getTicksRequiredToFreeze() + 120, entity.getTicksFrozen() + 1 + amplifier));
        }

        RandomSource random = level.getRandom();
        boolean isMoving = entity.xOld != entity.getX() || entity.zOld != entity.getZ();
        if (isMoving && random.nextBoolean()) {
            level.sendParticles(ParticleTypes.SNOWFLAKE, entity.getX(), entity.getOnPos().getY() + 1, entity.getZ(), 1,
                    Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F, 0.05, Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F, 0.0);
        }

        return true;
    }
}