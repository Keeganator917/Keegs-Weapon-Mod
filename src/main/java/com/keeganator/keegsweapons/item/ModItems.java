package com.keeganator.keegsweapons.item;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.item.custom.GrandAssassinsDaggerItem;
import com.keeganator.keegsweapons.item.custom.KinglyGreatswordItem;
import com.keeganator.keegsweapons.item.custom.ReapersScytheItem;
import com.keeganator.keegsweapons.item.custom.ShogunsKatanaItem;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
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
            new ScytheItem(ToolMaterial.NETHERITE, 4, -2.8F, settings.fireResistant()));

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
            new DaggerItem(ToolMaterial.NETHERITE, 1, -1F, settings.fireResistant()));

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
            new KatanaItem(ToolMaterial.NETHERITE, 3, -2.2F, settings.fireResistant()));

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
            new GreatswordItem(ToolMaterial.NETHERITE, 5, -2.8F, settings.fireResistant()));

    public static final Item REAPERS_SCYTHE = registerItem("reapers_scythe", settings ->
            new ReapersScytheItem(ModMaterialTypes.LEGENDARY, 4, -2.8F, settings.rarity(Rarity.EPIC).fireResistant()));
    public static final Item GRAND_ASSASSINS_DAGGER = registerItem("grand_assassins_dagger", settings ->
            new GrandAssassinsDaggerItem(ModMaterialTypes.LEGENDARY, 1, -1F, settings.rarity(Rarity.EPIC).fireResistant()));
    public static final Item SHOGUNS_KATANA = registerItem("shoguns_katana", settings ->
            new ShogunsKatanaItem(ModMaterialTypes.LEGENDARY, 3, -2.2F, settings.rarity(Rarity.EPIC).fireResistant()));
    public static final Item KINGLY_GREATSWORD = registerItem("kingly_greatsword", settings ->
            new KinglyGreatswordItem(ModMaterialTypes.LEGENDARY, 7, -2.8F, settings.rarity(Rarity.EPIC).fireResistant()));

    // Give Legendary Weapons Epic Rarity and add them to legendary rarity tag

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, name)))));
    }

    public static void registerModItems() {
        KeegsWeapons.LOGGER.info("Registering Mod Items for " + KeegsWeapons.MOD_ID);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.accept(ModItems.WOODEN_SCYTHE);
            entries.accept(ModItems.STONE_SCYTHE);
            entries.accept(ModItems.COPPER_SCYTHE);
            entries.accept(ModItems.GOLDEN_SCYTHE);
            entries.accept(ModItems.IRON_SCYTHE);
            entries.accept(ModItems.DIAMOND_SCYTHE);
            entries.accept(ModItems.NETHERITE_SCYTHE);
            entries.accept(ModItems.WOODEN_DAGGER);
            entries.accept(ModItems.STONE_DAGGER);
            entries.accept(ModItems.COPPER_DAGGER);
            entries.accept(ModItems.GOLDEN_DAGGER);
            entries.accept(ModItems.IRON_DAGGER);
            entries.accept(ModItems.DIAMOND_DAGGER);
            entries.accept(ModItems.NETHERITE_DAGGER);
            entries.accept(ModItems.WOODEN_KATANA);
            entries.accept(ModItems.STONE_KATANA);
            entries.accept(ModItems.COPPER_KATANA);
            entries.accept(ModItems.GOLDEN_KATANA);
            entries.accept(ModItems.IRON_KATANA);
            entries.accept(ModItems.DIAMOND_KATANA);
            entries.accept(ModItems.NETHERITE_KATANA);
            entries.accept(ModItems.WOODEN_GREATSWORD);
            entries.accept(ModItems.STONE_GREATSWORD);
            entries.accept(ModItems.COPPER_GREATSWORD);
            entries.accept(ModItems.GOLDEN_GREATSWORD);
            entries.accept(ModItems.IRON_GREATSWORD);
            entries.accept(ModItems.DIAMOND_GREATSWORD);
            entries.accept(ModItems.NETHERITE_GREATSWORD);

            entries.accept(ModItems.REAPERS_SCYTHE);
            entries.accept(ModItems.GRAND_ASSASSINS_DAGGER);
            entries.accept(ModItems.SHOGUNS_KATANA);
            entries.accept(ModItems.KINGLY_GREATSWORD);
        });
    }
}