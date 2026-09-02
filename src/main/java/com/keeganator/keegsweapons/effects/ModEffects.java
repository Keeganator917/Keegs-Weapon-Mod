package com.keeganator.keegsweapons.effects;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEffects {

    public static RegistryEntry<StatusEffect> FREEZING;
    public static RegistryEntry<StatusEffect> GROUNDED;

    public static void registerEffects() {
        FREEZING = Registry.registerReference(
                Registries.STATUS_EFFECT, Identifier.of(KeegsWeapons.MOD_ID, "freezing"), new FreezingEffect()
        );
        GROUNDED = Registry.registerReference(
                Registries.STATUS_EFFECT, Identifier.of(KeegsWeapons.MOD_ID, "grounded"), new GroundedEffect()
        );
    }
}