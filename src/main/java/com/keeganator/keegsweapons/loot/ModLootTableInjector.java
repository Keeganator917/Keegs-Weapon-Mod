package com.keeganator.keegsweapons.loot;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ModLootTableInjector {
    public static void init() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {

            if (key.equals(BuiltInLootTables.VILLAGE_WEAPONSMITH)) {
                KeegsWeapons.LOGGER.info("Adding Iron Weapons to Village Weaponsmith");
                tableBuilder.withPool(createPool(ModLootTables.INJECT_VILLAGE_WEAPONSMITH));
            }

            if (key.equals(BuiltInLootTables.END_CITY_TREASURE)) {
                KeegsWeapons.LOGGER.info("Adding Diamond Weapons to End City Treasure");
                tableBuilder.withPool(createPool(ModLootTables.INJECT_END_CITY_TREASURE));
            }

            if (key.equals(BuiltInLootTables.ANCIENT_CITY)) {
                KeegsWeapons.LOGGER.info("Adding Leech Enchant to Ancient City");
                tableBuilder.withPool(createPool(ModLootTables.INJECT_ANCIENT_CITY));
            }
        });
    }

    private static LootPool.Builder createPool(ResourceKey<LootTable> tableKey) {
        return LootPool.lootPool()
                .add(NestedLootTable.lootTableReference(tableKey).setWeight(1))
                .setRolls(UniformGenerator.between(1, 1));
    }
}
