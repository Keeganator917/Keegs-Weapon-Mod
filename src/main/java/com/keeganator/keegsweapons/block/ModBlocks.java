package com.keeganator.keegsweapons.block;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.block.custom.AntiAirBeaconBlock;
import com.keeganator.keegsweapons.block.custom.WeaponForgeBlock;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponType;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ModBlocks {

    public static final Block SCYTHE_WEAPON_FORGE = registerBlock("scythe_weapon_forge",
            properties -> new WeaponForgeBlock(
                    WeaponType.SCYTHE,
                    properties.mapColor(MapColor.DARK_RED).solid().strength(100f).sounds(BlockSoundGroup.NETHERITE).nonOpaque()));

    public static final Block DAGGER_WEAPON_FORGE = registerBlock("dagger_weapon_forge",
            properties -> new WeaponForgeBlock(WeaponType.DAGGER,
                    properties.mapColor(MapColor.DARK_RED).solid().strength(100f).sounds(BlockSoundGroup.NETHERITE).nonOpaque()));

    public static final Block KATANA_WEAPON_FORGE = registerBlock("katana_weapon_forge",
            properties -> new WeaponForgeBlock(WeaponType.KATANA,
                    properties.mapColor(MapColor.DARK_RED).solid().strength(100f).sounds(BlockSoundGroup.NETHERITE).nonOpaque()));

    public static final Block GREATSWORD_WEAPON_FORGE = registerBlock("greatsword_weapon_forge",
            properties -> new WeaponForgeBlock(WeaponType.GREATSWORD,
                    properties.mapColor(MapColor.DARK_RED).solid().strength(100f).sounds(BlockSoundGroup.NETHERITE).nonOpaque()));

    public static final Block ANTI_AIR_BEACON = registerBlock("anti_air_beacon",
            properties -> new AntiAirBeaconBlock(properties.nonOpaque().strength(1f)),
            Collections.singletonList(Text.translatable("block.keegsweapons.anti_air_beacon.tooltip").formatted(Formatting.GRAY)),
            Rarity.UNCOMMON);

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> function) {
        return registerBlock(name, function, List.of(), Rarity.EPIC);
    }

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> function, List<Text> tooltipLines, Rarity rarity) {
        Block toRegister = function.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(KeegsWeapons.MOD_ID, name))));
        registerBlockItem(name, toRegister, tooltipLines, rarity);
        return Registry.register(Registries.BLOCK, Identifier.of(KeegsWeapons.MOD_ID, name), toRegister);
    }

    private static void registerBlockItem(String name, Block block, List<Text> tooltipLines, Rarity rarity) {
        Item.Settings settings = new Item.Settings().useBlockPrefixedTranslationKey()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(KeegsWeapons.MOD_ID, name)))
                .rarity(rarity);

        if (!tooltipLines.isEmpty()) {
            settings.component(DataComponentTypes.LORE, new LoreComponent(tooltipLines, tooltipLines));
        }

        Registry.register(Registries.ITEM, Identifier.of(KeegsWeapons.MOD_ID, name), new BlockItem(block, settings));
    }

    public static void registerModBlocks() {
        KeegsWeapons.LOGGER.info("Registering Mod Blocks for " + KeegsWeapons.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(ModBlocks.SCYTHE_WEAPON_FORGE);
            entries.add(ModBlocks.DAGGER_WEAPON_FORGE);
            entries.add(ModBlocks.KATANA_WEAPON_FORGE);
            entries.add(ModBlocks.GREATSWORD_WEAPON_FORGE);
            entries.add(ModBlocks.ANTI_AIR_BEACON);
        });
    }
}