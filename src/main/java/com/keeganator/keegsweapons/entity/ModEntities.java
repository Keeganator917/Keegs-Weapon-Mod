package com.keeganator.keegsweapons.entity;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final Identifier REAPERS_SCYTHE_ABILITY_ID =
            Identifier.of(KeegsWeapons.MOD_ID, "reapers_scythe_ability");

    public static final RegistryKey<EntityType<?>> REAPERS_SCYTHE_ABILITY_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, REAPERS_SCYTHE_ABILITY_ID);


    public static final EntityType<ReapersScytheAbility> REAPERS_SCYTHE_ABILITY =
            Registry.register(Registries.ENTITY_TYPE, REAPERS_SCYTHE_ABILITY_KEY,
                    EntityType.Builder.<ReapersScytheAbility>create(ReapersScytheAbility::new, SpawnGroup.MISC)
                            .dimensions(0.7f, 0.7f).trackingTickInterval(1).build(REAPERS_SCYTHE_ABILITY_KEY)
            );


    public static void registerModEntities() {
        KeegsWeapons.LOGGER.info("Registering Mod Entities for " + KeegsWeapons.MOD_ID);
    }
}
