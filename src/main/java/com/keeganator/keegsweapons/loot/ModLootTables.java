package com.keeganator.keegsweapons.loot;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModLootTables {
    public static final ResourceKey<LootTable> INJECT_VILLAGE_WEAPONSMITH =
            registerKey("inject/" + BuiltInLootTables.VILLAGE_WEAPONSMITH.identifier().getPath());

    public static final ResourceKey<LootTable> INJECT_END_CITY_TREASURE =
            registerKey("inject/" + BuiltInLootTables.END_CITY_TREASURE.identifier().getPath());

    public static final ResourceKey<LootTable> INJECT_ANCIENT_CITY =
            registerKey("inject/" + BuiltInLootTables.ANCIENT_CITY.identifier().getPath());

    private static ResourceKey<LootTable> registerKey(String path) {
        Identifier id = Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, path);
        return ResourceKey.create(Registries.LOOT_TABLE, id);
    }
}