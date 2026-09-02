package com.keeganator.keegsweapons.loot;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;

public class ModLootTableInjector {
    public static void init() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {

            if (key.equals(LootTables.VILLAGE_WEAPONSMITH_CHEST)) {
                KeegsWeapons.LOGGER.info("Adding Iron Weapons to Village Weaponsmith");
                tableBuilder.pool(createPool(ModLootTables.INJECT_VILLAGE_WEAPONSMITH));
            }

            if (key.equals(LootTables.END_CITY_TREASURE_CHEST)) {
                KeegsWeapons.LOGGER.info("Adding Diamond Weapons to End City Treasure");
                tableBuilder.pool(createPool(ModLootTables.INJECT_END_CITY_TREASURE));
            }

            if (key.equals(LootTables.ANCIENT_CITY_CHEST)) {
                KeegsWeapons.LOGGER.info("Adding Leech Enchant to Ancient City");
                tableBuilder.pool(createPool(ModLootTables.INJECT_ANCIENT_CITY));
            }
        });
    }

    private static LootPool.Builder createPool(RegistryKey<LootTable> tableKey) {
        return LootPool.builder()
                .with(LootTableEntry.builder(tableKey).weight(1))
                .rolls(UniformLootNumberProvider.create(1, 1));
    }
}
