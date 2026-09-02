package com.keeganator.keegsweapons;

import com.keeganator.keegsweapons.block.ModBlocks;
import com.keeganator.keegsweapons.entity.client.ModEntityRenderers;
import com.keeganator.keegsweapons.events.DashEvent;
import com.keeganator.keegsweapons.hud.RageHudOverlay;
import com.keeganator.keegsweapons.network.MyModNetwork;
import com.keeganator.keegsweapons.particles.ModParticles;
import com.keeganator.keegsweapons.particles.custom.*;
import com.keeganator.keegsweapons.screen.ModScreens;
import com.keeganator.keegsweapons.util.ModKeyBinding;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;

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

        BlockRenderLayerMap.putBlock(ModBlocks.SCYTHE_WEAPON_FORGE, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.DAGGER_WEAPON_FORGE, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.KATANA_WEAPON_FORGE, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.GREATSWORD_WEAPON_FORGE, BlockRenderLayer.CUTOUT);

        BlockRenderLayerMap.putBlock(ModBlocks.ANTI_AIR_BEACON, BlockRenderLayer.CUTOUT);

        ParticleFactoryRegistry.getInstance().register(
                ModParticles.BACKSTAB_PARTICLES,
                BackstabParticles.Provider::new
        );

        ParticleFactoryRegistry.getInstance().register(
                ModParticles.WINGED_GRACE_PARTICLES,
                WingedGraceParticles.Provider::new
        );

        ParticleFactoryRegistry.getInstance().register(
                ModParticles.REAPERS_SCYTHE_ABILITY_PARTICLES,
                ReapersScytheAbilityParticle.Provider::new
        );

        ParticleFactoryRegistry.getInstance().register(
                ModParticles.REAPERS_SCYTHE_SWEEP_PARTICLES,
                ReapersScytheSweepParticles.Provider::new
        );

        ParticleFactoryRegistry.getInstance().register(
                ModParticles.GRAND_ASSASSINS_DAGGER_SWEEP_PARTICLES,
                GrandAssassinsDaggerSweepParticles.Provider::new
        );

        ParticleFactoryRegistry.getInstance().register(
                ModParticles.SHOGUNS_KATANA_SWEEP_PARTICLES,
                ShogunsKatanaSweepParticles.Provider::new
        );

        ParticleFactoryRegistry.getInstance().register(
                ModParticles.KINGLY_GREATSWORD_SWEEP_PARTICLES,
                KinglyGreatswordSweepParticles.Provider::new
        );

        ParticleFactoryRegistry.getInstance().register(
                ModParticles.SHOCKWAVE, ShockwaveParticle.Provider::new
        );

    }
}
