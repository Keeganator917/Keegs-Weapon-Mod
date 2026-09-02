package com.keeganator.keegsweapons.datagen;

import com.keeganator.keegsweapons.item.ModItems;
import com.keeganator.keegsweapons.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        valueLookupBuilder(ModTags.Items.SCYTHE)
                .add(ModItems.WOODEN_SCYTHE)
                .add(ModItems.STONE_SCYTHE)
                .add(ModItems.COPPER_SCYTHE)
                .add(ModItems.GOLDEN_SCYTHE)
                .add(ModItems.IRON_SCYTHE)
                .add(ModItems.DIAMOND_SCYTHE)
                .add(ModItems.NETHERITE_SCYTHE);
        valueLookupBuilder(ModTags.Items.DAGGER)
                .add(ModItems.WOODEN_DAGGER)
                .add(ModItems.STONE_DAGGER)
                .add(ModItems.COPPER_DAGGER)
                .add(ModItems.GOLDEN_DAGGER)
                .add(ModItems.IRON_DAGGER)
                .add(ModItems.DIAMOND_DAGGER)
                .add(ModItems.NETHERITE_DAGGER);
        valueLookupBuilder(ModTags.Items.KATANA)
                .add(ModItems.WOODEN_KATANA)
                .add(ModItems.STONE_KATANA)
                .add(ModItems.COPPER_KATANA)
                .add(ModItems.GOLDEN_KATANA)
                .add(ModItems.IRON_KATANA)
                .add(ModItems.DIAMOND_KATANA)
                .add(ModItems.NETHERITE_KATANA);
        valueLookupBuilder(ModTags.Items.GREATSWORD)
                .add(ModItems.WOODEN_GREATSWORD)
                .add(ModItems.STONE_GREATSWORD)
                .add(ModItems.COPPER_GREATSWORD)
                .add(ModItems.GOLDEN_GREATSWORD)
                .add(ModItems.IRON_GREATSWORD)
                .add(ModItems.DIAMOND_GREATSWORD)
                .add(ModItems.NETHERITE_GREATSWORD);

    }
}