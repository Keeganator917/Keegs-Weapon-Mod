package com.keeganator.keegsweapons.damagetypes;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.util.EntityExcludedDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ModDamageTypes {
    public static final RegistryKey<DamageType> REAPERS_SCYTHE_ABILITY =
            create("reapers_scythe_ability");
    public static final RegistryKey<DamageType> SHOCKWAVE =
            create("shockwave");

    public static final RegistryKey<DamageType> REAPERS_SCYTHE_ATTACK =
            create("reapers_scythe_attack");
    public static final RegistryKey<DamageType> GRAND_ASSASSINS_DAGGER_ATTACK =
            create("grand_assassins_dagger_attack");
    public static final RegistryKey<DamageType> SHOGUNS_KATANA_ATTACK =
            create("shoguns_katana_attack");
    public static final RegistryKey<DamageType> KINGLY_GREATSWORD_ATTACK =
            create("kingly_greatsword_attack");

    public static final RegistryKey<DamageType> GROUNDED =
            create("grounded");

    private static RegistryKey<DamageType> create(String name) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(KeegsWeapons.MOD_ID, name));
    }

    /**
     * Unified method for creating DamageSource with optional owner and excluded entity types
     */
    public static DamageSource create(ServerWorld world, RegistryKey<DamageType> type, Entity direct, @Nullable Entity owner, EntityType<?>... toIgnore) {
        RegistryEntryLookup<DamageType> lookup = world.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE);
        RegistryEntry<DamageType> entry = lookup.getOrThrow(type);

        if (toIgnore.length > 0) {
            return new EntityExcludedDamageSource(entry, toIgnore);
        }

        return new DamageSource(entry, direct, owner);
    }

    /**
     * Convenience wrappers for your mod-specific damage types
     */
    public static DamageSource reaperScytheAbility(ServerWorld world, Entity direct, @Nullable Entity owner) {
        return create(world, REAPERS_SCYTHE_ABILITY, direct, owner);
    }
    public static DamageSource shockwave(ServerWorld world, Entity attacker) {
        return create(world, SHOCKWAVE, attacker, attacker);
    }

    public static DamageSource reaperScytheAttack(ServerWorld world, Entity attacker) {
        return create(world, REAPERS_SCYTHE_ATTACK, attacker, attacker);
    }
    public static DamageSource grandAssassinsDaggerAttack(ServerWorld world, Entity attacker) {
        return create(world, GRAND_ASSASSINS_DAGGER_ATTACK, attacker, attacker);
    }
    public static DamageSource shogunsKatanaAttack(ServerWorld world, Entity attacker) {
        return create(world, SHOGUNS_KATANA_ATTACK, attacker, attacker);
    }
    public static DamageSource kinglyGreatswordAttack(ServerWorld world, Entity attacker) {
        return create(world, KINGLY_GREATSWORD_ATTACK, attacker, attacker);
    }

    public static DamageSource groundedDamage(ServerWorld world) {
        return create(world, GROUNDED, null, null);
    }

    /**
     * Generic convenience methods
     */
    public static DamageSource direct(ServerWorld world, RegistryKey<DamageType> type, Entity direct) {
        return create(world, type, direct, null);
    }

    public static DamageSource indirect(ServerWorld world, RegistryKey<DamageType> type, Entity direct, @Nullable Entity owner) {
        return create(world, type, direct, owner);
    }

    public static DamageSource exclude(ServerWorld world, RegistryKey<DamageType> type, Entity direct, EntityType<?>... toIgnore) {
        return create(world, type, direct, null, toIgnore);
    }
}
