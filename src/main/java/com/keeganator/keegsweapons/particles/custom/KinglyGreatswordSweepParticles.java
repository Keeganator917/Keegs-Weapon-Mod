package com.keeganator.keegsweapons.particles.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class KinglyGreatswordSweepParticles extends SingleQuadParticle {

    private final SpriteSet spriteProvider;

    protected KinglyGreatswordSweepParticles(ClientLevel world, double x, double y, double z, double scale, SpriteSet spriteProvider) {
        super(world, x, y, z, spriteProvider.get(0, 12));

        this.spriteProvider = spriteProvider;
        this.lifetime = 12;

        float f = this.random.nextFloat() * 0.6F + 0.4F;
        this.rCol = this.gCol = this.bCol = f;
        this.quadSize = 1.0F - (float) scale * 0.5F;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age >= this.lifetime) {
            this.remove();
        } else {
            this.setSprite(this.spriteProvider.get(this.age, this.lifetime));
            this.alpha = 1.0f - ((float) this.age / this.lifetime);
        }
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double dx, double dy, double dz, RandomSource random) {
            return new ReapersScytheSweepParticles(world, x, y, z, dx, this.sprites);
        }
    }
}
