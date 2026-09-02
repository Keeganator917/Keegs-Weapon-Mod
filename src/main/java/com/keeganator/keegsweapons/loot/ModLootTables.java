package com.keeganator.keegsweapons.loot;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModLootTables {
    public static final RegistryKey<LootTable> INJECT_VILLAGE_WEAPONSMITH =
            registerKey("inject/" + LootTables.VILLAGE_WEAPONSMITH_CHEST.getValue().getPath());

    public static final RegistryKey<LootTable> INJECT_END_CITY_TREASURE =
            registerKey("inject/" + LootTables.END_CITY_TREASURE_CHEST.getValue().getPath());

    public static final RegistryKey<LootTable> INJECT_ANCIENT_CITY =
            registerKey("inject/" + LootTables.ANCIENT_CITY_CHEST.getValue().getPath());

    private static RegistryKey<LootTable> registerKey(String path) {
        Identifier id = Identifier.of(KeegsWeapons.MOD_ID, path);
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, id);
    }
}