package com.keeganator.keegsweapons.item;

import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ToolMaterial;

public class ModMaterialTypes {
    public static final ToolMaterial LEGENDARY = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            9999999,
            9.0f,
            4.0f,
            15,
            ModTags.Items.BLANK_TAG
    );
}