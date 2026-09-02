package com.keeganator.keegsweapons.particles.custom;

import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;

public class ShogunsKatanaSweepParticles extends BillboardParticle {

    private final SpriteProvider spriteProvider;

    protected ShogunsKatanaSweepParticles(ClientWorld world, double x, double y, double z, double scale, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider.getSprite(0, 8));

        this.spriteProvider = spriteProvider;
        this.maxAge = 8;

        float f = this.random.nextFloat() * 0.6F + 0.4F;
        this.red = this.green = this.blue = f;
        this.scale = 1.0F - (float) scale * 0.5F;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age >= this.maxAge) {
            this.markDead();
        } else {
            this.setSprite(this.spriteProvider.getSprite(this.age, this.maxAge));
            this.alpha = 1.0f - ((float) this.age / this.maxAge);
        }
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
            return new ReapersScytheSweepParticles(world, x, y, z, dx, this.sprites);
        }
    }
}
