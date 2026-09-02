package com.keeganator.keegsweapons.particles.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class BackstabParticles extends SingleQuadParticle {

    protected BackstabParticles(ClientLevel world, double x, double y, double z, double dx, double dy, double dz, TextureAtlasSprite sprite) {
        super(world, x, y, z, dx, dy, dz, sprite);

        this.gravity = 0.8F;
        this.quadSize *= 0.85F;
        this.lifetime = 20;

        this.rCol = 1f;
        this.gCol = 0f;
        this.bCol = 0f;
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = 1.0f - ((float) age / lifetime);
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
            return new BackstabParticles(world, x, y, z, dx, dy, dz, sprites.get(random));
        }
    }
}