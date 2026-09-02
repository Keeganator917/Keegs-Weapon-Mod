package com.keeganator.keegsweapons.particles.custom;

import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;
import org.jspecify.annotations.Nullable;

public class ShockwaveParticle extends BillboardParticle {

    private final SpriteProvider spriteProvider;

    protected ShockwaveParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, float radius, int lifetimeTicks) {
        super(world, x, y, z, spriteProvider.getSprite(0, lifetimeTicks));
        this.spriteProvider = spriteProvider;

        this.collidesWithWorld = false;
        this.setMaxAge(lifetimeTicks);

        this.scale = radius;
    }

    @Override
    public void tick() {
        super.tick();
        this.updateSprite(this.spriteProvider);
    }

    @Override
    public BillboardParticle.Rotator getRotator() {
        // Makes particles lie flat on ground
        return (quaternion, camera, tickDelta) -> quaternion.rotationX((float) (-Math.PI / 2));
    }

    @Override
    protected BillboardParticle.RenderType getRenderType() {
        return BillboardParticle.RenderType.PARTICLE_ATLAS_TRANSLUCENT;
    }

    public static class Provider implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider sprites;

        public Provider(SpriteProvider sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            return new ShockwaveParticle(world, x, y, z, this.sprites, 5.0f, 12);
        }
    }
}