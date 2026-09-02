package com.keeganator.keegsweapons.particles;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {

    public static final SimpleParticleType WINGED_GRACE_PARTICLES =
            register("winged_grace_particles", true);

    public static final SimpleParticleType BACKSTAB_PARTICLES =
            register("backstab_particles", true);

    public static final SimpleParticleType REAPERS_SCYTHE_SWEEP_PARTICLES =
            register("reapers_scythe_sweep_particles", true);

    public static final SimpleParticleType GRAND_ASSASSINS_DAGGER_SWEEP_PARTICLES =
            register("grand_assassins_dagger_sweep_particles", true);

    public static final SimpleParticleType REAPERS_SCYTHE_ABILITY_PARTICLES =
            register("reapers_scythe_ability_particles", true);

    public static final SimpleParticleType SHOGUNS_KATANA_SWEEP_PARTICLES =
            register("shoguns_katana_sweep_particles", true);

    public static final SimpleParticleType KINGLY_GREATSWORD_SWEEP_PARTICLES =
            register("kingly_greatsword_sweep_particles", true);

    public static final SimpleParticleType SHOCKWAVE =
            register("shockwave", true);

    private static SimpleParticleType register(String name, boolean alwaysShow) {
        return Registry.register(
                Registries.PARTICLE_TYPE,
                Identifier.of(KeegsWeapons.MOD_ID, name),
                FabricParticleTypes.simple(alwaysShow)
        );
    }

    public static void init() {
        //intentionally empty to force game to load class
    }
}
