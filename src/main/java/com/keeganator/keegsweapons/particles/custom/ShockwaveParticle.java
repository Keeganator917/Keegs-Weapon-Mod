package com.keeganator.keegsweapons.particles.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public class ShockwaveParticle extends SingleQuadParticle {

    private final SpriteSet spriteProvider;

    protected ShockwaveParticle(ClientLevel world, double x, double y, double z, SpriteSet spriteProvider, float radius, int lifetimeTicks) {
        super(world, x, y, z, spriteProvider.get(0, lifetimeTicks));
        this.spriteProvider = spriteProvider;

        this.hasPhysics = false;
        this.setLifetime(lifetimeTicks);

        this.quadSize = radius;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.spriteProvider);
    }

    @Override
    public SingleQuadParticle.FacingCameraMode getFacingCameraMode() {
        // Makes particles lie flat on ground
        return (quaternion, camera, tickDelta) -> quaternion.rotationX((float) (-Math.PI / 2));
    }

    @Override
    protected SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, RandomSource random) {
            return new ShockwaveParticle(world, x, y, z, this.sprites, 5.0f, 12);
        }
    }
}