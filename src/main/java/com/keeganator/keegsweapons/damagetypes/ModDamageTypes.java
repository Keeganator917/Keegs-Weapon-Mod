package com.keeganator.keegsweapons.damagetypes;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.util.EntityExcludedDamageSource;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> REAPERS_SCYTHE_ABILITY =
            create("reapers_scythe_ability");
    public static final ResourceKey<DamageType> SHOCKWAVE =
            create("shockwave");

    public static final ResourceKey<DamageType> REAPERS_SCYTHE_ATTACK =
            create("reapers_scythe_attack");
    public static final ResourceKey<DamageType> GRAND_ASSASSINS_DAGGER_ATTACK =
            create("grand_assassins_dagger_attack");
    public static final ResourceKey<DamageType> SHOGUNS_KATANA_ATTACK =
            create("shoguns_katana_attack");
    public static final ResourceKey<DamageType> KINGLY_GREATSWORD_ATTACK =
            create("kingly_greatsword_attack");

    public static final ResourceKey<DamageType> GROUNDED =
            create("grounded");

    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, name));
    }

    /**
     * Unified method for creating DamageSource with optional owner and excluded entity types
     */
    public static DamageSource create(ServerLevel world, ResourceKey<DamageType> type, Entity direct, @Nullable Entity owner, EntityType<?>... toIgnore) {
        HolderGetter<DamageType> lookup = world.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
        Holder<DamageType> entry = lookup.getOrThrow(type);

        if (toIgnore.length > 0) {
            return new EntityExcludedDamageSource(entry, direct, owner, toIgnore);
        }

        return new DamageSource(entry, direct, owner);
    }

    /**
     * Convenience wrappers for your mod-specific damage types
     */
    public static DamageSource reaperScytheAbility(ServerLevel world, Entity direct, @Nullable Entity owner) {
        return create(world, REAPERS_SCYTHE_ABILITY, direct, owner);
    }
    public static DamageSource shockwave(ServerLevel world, Entity attacker) {
        return create(world, SHOCKWAVE, attacker, attacker);
    }

    public static DamageSource reaperScytheAttack(ServerLevel world, Entity attacker) {
        return create(world, REAPERS_SCYTHE_ATTACK, attacker, attacker);
    }
    public static DamageSource grandAssassinsDaggerAttack(ServerLevel world, Entity attacker) {
        return create(world, GRAND_ASSASSINS_DAGGER_ATTACK, attacker, attacker);
    }
    public static DamageSource shogunsKatanaAttack(ServerLevel world, Entity attacker) {
        return create(world, SHOGUNS_KATANA_ATTACK, attacker, attacker);
    }
    public static DamageSource kinglyGreatswordAttack(ServerLevel world, Entity attacker) {
        return create(world, KINGLY_GREATSWORD_ATTACK, attacker, attacker);
    }

    public static DamageSource groundedDamage(ServerLevel world) {
        return create(world, GROUNDED, null, null);
    }

    /**
     * Generic convenience methods
     */
    public static DamageSource direct(ServerLevel world, ResourceKey<DamageType> type, Entity direct) {
        return create(world, type, direct, null);
    }

    public static DamageSource indirect(ServerLevel world, ResourceKey<DamageType> type, Entity direct, @Nullable Entity owner) {
        return create(world, type, direct, owner);
    }

    public static DamageSource exclude(ServerLevel world, ResourceKey<DamageType> type, Entity direct, EntityType<?>... toIgnore) {
        return create(world, type, direct, null, toIgnore);
    }
}
