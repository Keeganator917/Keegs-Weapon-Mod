package com.keeganator.keegsweapons.enchantment;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.enchantment.custom.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public class ModEnchantmentEffects {
    public static final MapCodec<? extends EnchantmentEntityEffect> THUNDERING =
            registerEntityEffect("thundering", ThunderingEnchantment.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> POISON_TIPPED =
            registerEntityEffect("poison_tipped", PoisonTippedEnchantment.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> LEECH =
            registerEntityEffect("leech", LeechEnchantment.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> DASH =
            registerEntityEffect("dash", DashEnchantment.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> PATHFINDER =
            registerEntityEffect("pathfinder", PathfinderEnchantment.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> EXPERT =
            registerEntityEffect("expert", ExpertEnchantment.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> ILLAGER_BANE =
            registerEntityEffect("illager_bane", IllagerBaneEnchantment.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> DOUBLE_JUMP =
            registerEntityEffect("double_jump", DoubleJumpEnchantment.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> RAGE =
            registerEntityEffect("rage", RageEnchantment.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> GRAVITY =
            registerEntityEffect("gravity", GravityEnchantment.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> FROSTFALL =
            registerEntityEffect("frostfall", FrostfallEnchantment.CODEC);


    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, name), codec);
    }

    public static void registerEnchantmentEffects() {
        KeegsWeapons.LOGGER.info("Registering Mod Enchantment Effects for " + KeegsWeapons.MOD_ID);
    }
}
