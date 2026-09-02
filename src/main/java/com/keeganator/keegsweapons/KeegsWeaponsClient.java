package com.keeganator.keegsweapons;

import com.keeganator.keegsweapons.entity.client.ModEntityRenderers;
import com.keeganator.keegsweapons.events.DashEvent;
import com.keeganator.keegsweapons.hud.RageHudOverlay;
import com.keeganator.keegsweapons.network.MyModNetwork;
import com.keeganator.keegsweapons.particles.ModParticles;
import com.keeganator.keegsweapons.particles.custom.*;
import com.keeganator.keegsweapons.screen.ModScreens;
import com.keeganator.keegsweapons.util.ModKeyBinding;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;

public class KeegsWeaponsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModKeyBinding.register();
        ModKeyBinding.registerTickHandler();
        DashEvent.register();
        MyModNetwork.registerClient();

        ModEntityRenderers.register();

        ModScreens.register();
        RageHudOverlay.register();

        ParticleProviderRegistry.getInstance().register(
                ModParticles.BACKSTAB_PARTICLES,
                BackstabParticles.Provider::new
        );

        ParticleProviderRegistry.getInstance().register(
                ModParticles.WINGED_GRACE_PARTICLES,
                WingedGraceParticles.Provider::new
        );

        ParticleProviderRegistry.getInstance().register(
                ModParticles.REAPERS_SCYTHE_ABILITY_PARTICLES,
                ReapersScytheAbilityParticle.Provider::new
        );

        ParticleProviderRegistry.getInstance().register(
                ModParticles.REAPERS_SCYTHE_SWEEP_PARTICLES,
                ReapersScytheSweepParticles.Provider::new
        );

        ParticleProviderRegistry.getInstance().register(
                ModParticles.GRAND_ASSASSINS_DAGGER_SWEEP_PARTICLES,
                GrandAssassinsDaggerSweepParticles.Provider::new
        );

        ParticleProviderRegistry.getInstance().register(
                ModParticles.SHOGUNS_KATANA_SWEEP_PARTICLES,
                ShogunsKatanaSweepParticles.Provider::new
        );

        ParticleProviderRegistry.getInstance().register(
                ModParticles.KINGLY_GREATSWORD_SWEEP_PARTICLES,
                KinglyGreatswordSweepParticles.Provider::new
        );

        ParticleProviderRegistry.getInstance().register(
                ModParticles.SHOCKWAVE, ShockwaveParticle.Provider::new
        );

    }
}
