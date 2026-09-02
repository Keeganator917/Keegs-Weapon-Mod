package com.keeganator.keegsweapons.enchantment;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.enchantment.custom.*;
import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

public class ModEnchantments {

    public static final RegistryKey<Enchantment> THUNDERING =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(KeegsWeapons.MOD_ID, "thundering"));
    public static final RegistryKey<Enchantment> POISON_TIPPED =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(KeegsWeapons.MOD_ID, "poison_tipped"));
    public static final RegistryKey<Enchantment> LEECH =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(KeegsWeapons.MOD_ID, "leech"));
    public static final RegistryKey<Enchantment> DASH =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(KeegsWeapons.MOD_ID, "dash"));
    public static final RegistryKey<Enchantment> PATHFINDER =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(KeegsWeapons.MOD_ID, "pathfinder"));
    public static final RegistryKey<Enchantment> EXPERT =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(KeegsWeapons.MOD_ID, "expert"));
    public static final RegistryKey<Enchantment> ILLAGER_BANE =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(KeegsWeapons.MOD_ID, "illager_bane"));
    public static final RegistryKey<Enchantment> DOUBLE_JUMP =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(KeegsWeapons.MOD_ID, "double_jump"));
    public static final RegistryKey<Enchantment> RAGE =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(KeegsWeapons.MOD_ID, "rage"));
    public static final RegistryKey<Enchantment> GRAVITY =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(KeegsWeapons.MOD_ID, "gravity"));
    public static final RegistryKey<Enchantment> FROSTFALL =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(KeegsWeapons.MOD_ID, "frostfall"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        //enchantment datagen
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}