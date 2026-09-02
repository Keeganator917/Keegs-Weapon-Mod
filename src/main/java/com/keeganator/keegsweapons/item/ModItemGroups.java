package com.keeganator.keegsweapons.item;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    /*
    public static final ItemGroup KEEGS_WEAPONS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(KeegsWeapons.MOD_ID, "keegsweapons"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.DIAMOND_SCYTHE))
                    .displayName(Text.translatable("itemgroup.keegsweapons.keegsweapons"))
                    .entries((displayContext, entries) -> {


                    }).build());

     */
    public static void registerItemGroups() {
        KeegsWeapons.LOGGER.info("Registering Item Groups for " + KeegsWeapons.MOD_ID);
    }
}
