package com.keeganator.keegsweapons.particles.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.HugeExplosionParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public class ReapersScytheAbilityParticle extends HugeExplosionParticle {

    protected ReapersScytheAbilityParticle(ClientLevel world, double x, double y, double z,
                                           float scale, SpriteSet sprites) {
        super(world, x, y, z, 0, sprites);
        this.lifetime = 16;
        this.quadSize = scale;
        this.hasPhysics = false;
        this.setSprite(sprites.get(age, lifetime));
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, RandomSource random) {
            return new ReapersScytheAbilityParticle(world, x, y, z, (float) velocityX, sprites);
        }
    }
}