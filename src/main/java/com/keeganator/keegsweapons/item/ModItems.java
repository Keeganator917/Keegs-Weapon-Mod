package com.keeganator.keegsweapons.item;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.item.custom.GrandAssassinsDaggerItem;
import com.keeganator.keegsweapons.item.custom.KinglyGreatswordItem;
import com.keeganator.keegsweapons.item.custom.ReapersScytheItem;
import com.keeganator.keegsweapons.item.custom.ShogunsKatanaItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Function;

public class ModItems {
        public static final Item WOODEN_SCYTHE = registerItem("wooden_scythe", settings ->
                new ScytheItem(ToolMaterial.WOOD, 4, -2.8F, settings));
        public static final Item STONE_SCYTHE = registerItem("stone_scythe", settings ->
                new ScytheItem(ToolMaterial.STONE, 4, -2.8F, settings));
        public static final Item COPPER_SCYTHE = registerItem("copper_scythe", settings ->
                new ScytheItem(ToolMaterial.COPPER, 4, -2.8F, settings));
        public static final Item GOLDEN_SCYTHE = registerItem("golden_scythe", settings ->
                new ScytheItem(ToolMaterial.GOLD, 4, -2.8F, settings));
        public static final Item IRON_SCYTHE = registerItem("iron_scythe", settings ->
                new ScytheItem(ToolMaterial.IRON, 4, -2.8F, settings));
        public static final Item DIAMOND_SCYTHE = registerItem("diamond_scythe", settings ->
                new ScytheItem(ToolMaterial.DIAMOND, 4, -2.8F, settings));
        public static final Item NETHERITE_SCYTHE = registerItem("netherite_scythe", settings ->
                new ScytheItem(ToolMaterial.NETHERITE, 4, -2.8F, settings.fireproof()));

        public static final Item WOODEN_DAGGER = registerItem("wooden_dagger", settings ->
                new DaggerItem(ToolMaterial.WOOD, 1, -1F, settings));
        public static final Item STONE_DAGGER = registerItem("stone_dagger", settings ->
                new DaggerItem(ToolMaterial.STONE, 1, -1F, settings));
        public static final Item COPPER_DAGGER = registerItem("copper_dagger", settings ->
                new DaggerItem(ToolMaterial.COPPER, 1, -1F, settings));
        public static final Item GOLDEN_DAGGER = registerItem("golden_dagger", settings ->
                new DaggerItem(ToolMaterial.GOLD, 1, -1F, settings));
        public static final Item IRON_DAGGER = registerItem("iron_dagger", settings ->
                new DaggerItem(ToolMaterial.IRON, 1, -1F, settings));
        public static final Item DIAMOND_DAGGER = registerItem("diamond_dagger", settings ->
                new DaggerItem(ToolMaterial.DIAMOND, 1, -1F, settings));
        public static final Item NETHERITE_DAGGER = registerItem("netherite_dagger", settings ->
                new DaggerItem(ToolMaterial.NETHERITE, 1, -1F, settings.fireproof()));

        public static final Item WOODEN_KATANA = registerItem("wooden_katana", settings ->
                new KatanaItem(ToolMaterial.WOOD, 3, -2.2F, settings));
        public static final Item STONE_KATANA = registerItem("stone_katana", settings ->
                new KatanaItem(ToolMaterial.STONE, 3, -2.2F, settings));
        public static final Item COPPER_KATANA = registerItem("copper_katana", settings ->
                new KatanaItem(ToolMaterial.COPPER, 3, -2.2F, settings));
        public static final Item GOLDEN_KATANA = registerItem("golden_katana", settings ->
                new KatanaItem(ToolMaterial.GOLD, 3, -2.2F, settings));
        public static final Item IRON_KATANA = registerItem("iron_katana", settings ->
                new KatanaItem(ToolMaterial.IRON, 3, -2.2F, settings));
        public static final Item DIAMOND_KATANA = registerItem("diamond_katana", settings ->
                new KatanaItem(ToolMaterial.DIAMOND, 3, -2.2F, settings));
        public static final Item NETHERITE_KATANA = registerItem("netherite_katana", settings ->
                new KatanaItem(ToolMaterial.NETHERITE, 3, -2.2F, settings.fireproof()));

        public static final Item WOODEN_GREATSWORD = registerItem("wooden_greatsword", settings ->
                new GreatswordItem(ToolMaterial.WOOD, 5, -2.8F, settings));
        public static final Item STONE_GREATSWORD = registerItem("stone_greatsword", settings ->
                new GreatswordItem(ToolMaterial.STONE, 5, -2.8F, settings));
        public static final Item COPPER_GREATSWORD = registerItem("copper_greatsword", settings ->
                new GreatswordItem(ToolMaterial.COPPER, 5, -2.8F, settings));
        public static final Item GOLDEN_GREATSWORD = registerItem("golden_greatsword", settings ->
                new GreatswordItem(ToolMaterial.GOLD, 5, -2.8F, settings));
        public static final Item IRON_GREATSWORD = registerItem("iron_greatsword", settings ->
                new GreatswordItem(ToolMaterial.IRON, 5, -2.8F, settings));
        public static final Item DIAMOND_GREATSWORD = registerItem("diamond_greatsword", settings ->
                new GreatswordItem(ToolMaterial.DIAMOND, 5, -2.8F, settings));
        public static final Item NETHERITE_GREATSWORD = registerItem("netherite_greatsword", settings ->
                new GreatswordItem(ToolMaterial.NETHERITE, 5, -2.8F, settings.fireproof()));

        public static final Item REAPERS_SCYTHE = registerItem("reapers_scythe", settings ->
                new ReapersScytheItem(ModMaterialTypes.LEGENDARY, 4, -2.8F, settings.rarity(Rarity.EPIC).fireproof()));
        public static final Item GRAND_ASSASSINS_DAGGER = registerItem("grand_assassins_dagger", settings ->
                new GrandAssassinsDaggerItem(ModMaterialTypes.LEGENDARY, 1, -1F, settings.rarity(Rarity.EPIC).fireproof()));
    public static final Item SHOGUNS_KATANA = registerItem("shoguns_katana", settings ->
            new ShogunsKatanaItem(ModMaterialTypes.LEGENDARY, 3, -2.2F, settings.rarity(Rarity.EPIC).fireproof()));
    public static final Item KINGLY_GREATSWORD = registerItem("kingly_greatsword", settings ->
            new KinglyGreatswordItem(ModMaterialTypes.LEGENDARY, 7, -2.8F, settings.rarity(Rarity.EPIC).fireproof()));

// Give Legendary Weapons Epic Rarity and add them to legendary rarity tag

    private static Item registerItem(String name, Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(KeegsWeapons.MOD_ID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(KeegsWeapons.MOD_ID, name)))));
    }

    public static void registerModItems() {
        KeegsWeapons.LOGGER.info("Registering Mod Items for " + KeegsWeapons.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(ModItems.WOODEN_SCYTHE);
            entries.add(ModItems.STONE_SCYTHE);
            entries.add(ModItems.COPPER_SCYTHE);
            entries.add(ModItems.GOLDEN_SCYTHE);
            entries.add(ModItems.IRON_SCYTHE);
            entries.add(ModItems.DIAMOND_SCYTHE);
            entries.add(ModItems.NETHERITE_SCYTHE);
            entries.add(ModItems.WOODEN_DAGGER);
            entries.add(ModItems.STONE_DAGGER);
            entries.add(ModItems.COPPER_DAGGER);
            entries.add(ModItems.GOLDEN_DAGGER);
            entries.add(ModItems.IRON_DAGGER);
            entries.add(ModItems.DIAMOND_DAGGER);
            entries.add(ModItems.NETHERITE_DAGGER);
            entries.add(ModItems.WOODEN_KATANA);
            entries.add(ModItems.STONE_KATANA);
            entries.add(ModItems.COPPER_KATANA);
            entries.add(ModItems.GOLDEN_KATANA);
            entries.add(ModItems.IRON_KATANA);
            entries.add(ModItems.DIAMOND_KATANA);
            entries.add(ModItems.NETHERITE_KATANA);
            entries.add(ModItems.WOODEN_GREATSWORD);
            entries.add(ModItems.STONE_GREATSWORD);
            entries.add(ModItems.COPPER_GREATSWORD);
            entries.add(ModItems.GOLDEN_GREATSWORD);
            entries.add(ModItems.IRON_GREATSWORD);
            entries.add(ModItems.DIAMOND_GREATSWORD);
            entries.add(ModItems.NETHERITE_GREATSWORD);

            entries.add(ModItems.REAPERS_SCYTHE);
            entries.add(ModItems.GRAND_ASSASSINS_DAGGER);
            entries.add(ModItems.SHOGUNS_KATANA);
            entries.add(ModItems.KINGLY_GREATSWORD);
        });
    }
}