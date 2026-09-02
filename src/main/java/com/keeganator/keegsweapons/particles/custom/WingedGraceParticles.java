package com.keeganator.keegsweapons.particles.custom;

import net.minecraft.client.particle.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;

public class WingedGraceParticles extends BillboardParticle {

    protected WingedGraceParticles(ClientWorld world, double x, double y, double z, double dx, double dy, double dz, Sprite sprites) {
        super(world, x, y, z, dx, dy, dz, sprites);

        this.velocityX *= 0.98;
        this.velocityZ *= 0.98;
        this.velocityY += 0.002;
        this.gravityStrength = 0.0F;
        this.scale *= 0.85F;
        this.maxAge = 60;

        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
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
            return new WingedGraceParticles(world, x, y, z, dx, dy, dz, sprites.getSprite(random));
        }
    }
}