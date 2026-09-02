package com.keeganator.keegsweapons.particles.custom;

import net.minecraft.client.particle.ExplosionLargeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;
import org.jspecify.annotations.Nullable;

public class ReapersScytheAbilityParticle extends ExplosionLargeParticle {

    protected ReapersScytheAbilityParticle(ClientWorld world, double x, double y, double z,
                                           float scale, SpriteProvider sprites) {
        super(world, x, y, z, 0, sprites);
        this.maxAge = 16;
        this.scale = scale;
        this.collidesWithWorld = false;
        this.setSprite(sprites.getSprite(age, maxAge));
    }

    public static class Provider implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider sprites;

        public Provider(SpriteProvider sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            return new ReapersScytheAbilityParticle(world, x, y, z, (float) velocityX, sprites);
        }
    }
}