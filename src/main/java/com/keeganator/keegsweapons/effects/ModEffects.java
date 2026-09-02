package com.keeganator.keegsweapons.effects;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

public class ModEffects {

    public static Holder<MobEffect> FREEZING;
    public static Holder<MobEffect> GROUNDED;

    public static void registerEffects() {
        FREEZING = Registry.registerForHolder(
                BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "freezing"), new FreezingEffect()
        );
        GROUNDED = Registry.registerForHolder(
                BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "grounded"), new GroundedEffect()
        );
    }
}