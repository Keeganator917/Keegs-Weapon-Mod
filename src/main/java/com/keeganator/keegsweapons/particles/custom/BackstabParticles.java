package com.keeganator.keegsweapons.particles.custom;

import net.minecraft.client.particle.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;

public class BackstabParticles extends BillboardParticle {

    protected BackstabParticles(ClientWorld world, double x, double y, double z, double dx, double dy, double dz, Sprite sprite) {
        super(world, x, y, z, dx, dy, dz, sprite);

        this.gravityStrength = 0.8F;
        this.scale *= 0.85F;
        this.maxAge = 20;

        this.red = 1f;
        this.green = 0f;
        this.blue = 0f;
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = 1.0f - ((float) age / maxAge);
    }

    @Override
    protected RenderType getRenderType() {
        return RenderType.PARTICLE_ATLAS_TRANSLUCENT;
    }

    public static class Provider implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider sprites;

        public Provider(SpriteProvider sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double dx, double dy, double dz, Random random) {
            return new BackstabParticles(world, x, y, z, dx, dy, dz, sprites.getSprite(random));
        }
    }
}