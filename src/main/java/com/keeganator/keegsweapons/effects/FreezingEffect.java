package com.keeganator.keegsweapons.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class FreezingEffect extends StatusEffect {

    public FreezingEffect() {
        super(StatusEffectCategory.HARMFUL, 0x1E90FF);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        entity.setInPowderSnow(true);
        if (entity.canFreeze()) {
            entity.setFrozenTicks(Math.min(entity.getMinFreezeDamageTicks() + 120, entity.getFrozenTicks() + 1 + amplifier));
        }

        Random random = world.getRandom();
        boolean bl2 = entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ();
        if (bl2 && random.nextBoolean()) {
            world.spawnParticles(ParticleTypes.SNOWFLAKE, entity.getX(), entity.getBlockPos().getY() + 1, entity.getZ(), 1,
                    (MathHelper.nextBetween(random, -1.0F, 1.0F) * 0.083333336F), 0.05F, MathHelper.nextBetween(random, -1.0F, 1.0F) * 0.083333336F, 0.0);
        }

        return true;
    }
}