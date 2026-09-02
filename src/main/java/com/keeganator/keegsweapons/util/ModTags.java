package com.keeganator.keegsweapons.util;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> PATH_BLOCKS = createTag("path_blocks");
        public static final TagKey<Block> EXPERIENCE_BLOCKS = createTag("experience_blocks");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(KeegsWeapons.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> SCYTHE = createTag("scythe");
        public static final TagKey<Item> DAGGER = createTag("dagger");
        public static final TagKey<Item> KATANA = createTag("katana");
        public static final TagKey<Item> GREATSWORD = createTag("greatsword");
        public static final TagKey<Item> LEGENDARY_RARITY = createTag("legendary_rarity");
        public static final TagKey<Item> ZOMBIE_CAN_SPAWN_WITH = createTag("zombie_can_spawn_with");
        public static final TagKey<Item> BLANK_TAG = createTag("blank_tag");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(KeegsWeapons.MOD_ID, name));
        }
    }

}
