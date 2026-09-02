package com.keeganator.keegsweapons.block;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.block.custom.AntiAirBeaconBlock;
import com.keeganator.keegsweapons.block.custom.WeaponForgeBlock;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponType;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ModBlocks {

    public static final Block SCYTHE_WEAPON_FORGE = registerBlock("scythe_weapon_forge",
            properties -> new WeaponForgeBlock(
                    WeaponType.SCYTHE,
                    properties.mapColor(MapColor.NETHER).forceSolidOn().strength(100f).sound(SoundType.NETHERITE_BLOCK).noOcclusion()));

    public static final Block DAGGER_WEAPON_FORGE = registerBlock("dagger_weapon_forge",
            properties -> new WeaponForgeBlock(WeaponType.DAGGER,
                    properties.mapColor(MapColor.NETHER).forceSolidOn().strength(100f).sound(SoundType.NETHERITE_BLOCK).noOcclusion()));

    public static final Block KATANA_WEAPON_FORGE = registerBlock("katana_weapon_forge",
            properties -> new WeaponForgeBlock(WeaponType.KATANA,
                    properties.mapColor(MapColor.NETHER).forceSolidOn().strength(100f).sound(SoundType.NETHERITE_BLOCK).noOcclusion()));

    public static final Block GREATSWORD_WEAPON_FORGE = registerBlock("greatsword_weapon_forge",
            properties -> new WeaponForgeBlock(WeaponType.GREATSWORD,
                    properties.mapColor(MapColor.NETHER).forceSolidOn().strength(100f).sound(SoundType.NETHERITE_BLOCK).noOcclusion()));

    public static final Block ANTI_AIR_BEACON = registerBlock("anti_air_beacon",
            properties -> new AntiAirBeaconBlock(properties.noOcclusion().strength(1f)),
            Collections.singletonList(Component.translatable("block.keegsweapons.anti_air_beacon.tooltip").withStyle(ChatFormatting.GRAY)),
            Rarity.UNCOMMON);

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function) {
        return registerBlock(name, function, List.of(), Rarity.EPIC);
    }

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function, List<Component> tooltipLines, Rarity rarity) {
        Block toRegister = function.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, name))));
        registerBlockItem(name, toRegister, tooltipLines, rarity);
        return Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, name), toRegister);
    }

    private static void registerBlockItem(String name, Block block, List<Component> tooltipLines, Rarity rarity) {
        Item.Properties settings = new Item.Properties().useBlockDescriptionPrefix()
                .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, name)))
                .rarity(rarity);

        if (!tooltipLines.isEmpty()) {
            settings.component(DataComponents.LORE, new ItemLore(tooltipLines, tooltipLines));
        }

        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, name), new BlockItem(block, settings));
    }

    public static void registerModBlocks() {
        KeegsWeapons.LOGGER.info("Registering Mod Blocks for " + KeegsWeapons.MOD_ID);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.accept(ModBlocks.SCYTHE_WEAPON_FORGE);
            entries.accept(ModBlocks.DAGGER_WEAPON_FORGE);
            entries.accept(ModBlocks.KATANA_WEAPON_FORGE);
            entries.accept(ModBlocks.GREATSWORD_WEAPON_FORGE);
            entries.accept(ModBlocks.ANTI_AIR_BEACON);
        });
    }
}