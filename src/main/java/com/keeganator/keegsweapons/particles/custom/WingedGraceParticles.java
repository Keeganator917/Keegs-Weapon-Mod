package com.keeganator.keegsweapons.particles.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class WingedGraceParticles extends SingleQuadParticle {

    protected WingedGraceParticles(ClientLevel world, double x, double y, double z, double dx, double dy, double dz, TextureAtlasSprite sprites) {
        super(world, x, y, z, dx, dy, dz, sprites);

        this.xd *= 0.98;
        this.zd *= 0.98;
        this.yd += 0.002;
        this.gravity = 0.0F;
        this.quadSize *= 0.85F;
        this.lifetime = 60;

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
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
        public Particle createParticle(SimpleParticleType type, ClientLevel world,
                                       double x, double y, double z, double dx, double dy, double dz, RandomSource random) {
            return new WingedGraceParticles(world, x, y, z, dx, dy, dz, sprites.get(random));
        }
    }
}