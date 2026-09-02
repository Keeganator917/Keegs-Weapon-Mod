package com.keeganator.keegsweapons.enchantment;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.enchantment.custom.*;
import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {

    public static final ResourceKey<Enchantment> THUNDERING =
            ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "thundering"));
    public static final ResourceKey<Enchantment> POISON_TIPPED =
            ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "poison_tipped"));
    public static final ResourceKey<Enchantment> LEECH =
            ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "leech"));
    public static final ResourceKey<Enchantment> DASH =
            ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "dash"));
    public static final ResourceKey<Enchantment> PATHFINDER =
            ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "pathfinder"));
    public static final ResourceKey<Enchantment> EXPERT =
            ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "expert"));
    public static final ResourceKey<Enchantment> ILLAGER_BANE =
            ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "illager_bane"));
    public static final ResourceKey<Enchantment> DOUBLE_JUMP =
            ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "double_jump"));
    public static final ResourceKey<Enchantment> RAGE =
            ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "rage"));
    public static final ResourceKey<Enchantment> GRAVITY =
            ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "gravity"));
    public static final ResourceKey<Enchantment> FROSTFALL =
            ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "frostfall"));

    public static void bootstrap(BootstrapContext<Enchantment> registerable) {
        var enchantments = registerable.lookup(Registries.ENCHANTMENT);
        var items = registerable.lookup(Registries.ITEM);

        // Datagen for enchants
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.identifier()));
    }
}