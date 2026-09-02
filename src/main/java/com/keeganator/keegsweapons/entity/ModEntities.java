package com.keeganator.keegsweapons.entity;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static final Identifier REAPERS_SCYTHE_ABILITY_ID =
            Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "reapers_scythe_ability");

    public static final ResourceKey<EntityType<?>> REAPERS_SCYTHE_ABILITY_KEY =
            ResourceKey.create(Registries.ENTITY_TYPE, REAPERS_SCYTHE_ABILITY_ID);


    public static final EntityType<ReapersScytheAbility> REAPERS_SCYTHE_ABILITY =
            Registry.register(BuiltInRegistries.ENTITY_TYPE, REAPERS_SCYTHE_ABILITY_KEY,
                    EntityType.Builder.<ReapersScytheAbility>of(ReapersScytheAbility::new, MobCategory.MISC)
                            .sized(0.7f, 0.7f).updateInterval(1).build(REAPERS_SCYTHE_ABILITY_KEY)
            );


    public static void registerModEntities() {
        KeegsWeapons.LOGGER.info("Registering Mod Entities for " + KeegsWeapons.MOD_ID);
    }
}
