package com.keeganator.keegsweapons.util;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> PATH_BLOCKS = createTag("path_blocks");
        public static final TagKey<Block> EXPERIENCE_BLOCKS = createTag("experience_blocks");

        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, name));
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
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, name));
        }
    }

}
