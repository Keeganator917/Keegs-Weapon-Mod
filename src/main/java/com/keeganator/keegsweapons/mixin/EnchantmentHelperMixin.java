package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "generateEnchantments", at = @At("RETURN"), cancellable = true)
    private static void limitLegendaryEnchantingTable(Random random, ItemStack stack, int level, Stream<RegistryEntry<Enchantment>> possibleEnchantments,
                                                      CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {

        if (stack.isIn(ModTags.Items.LEGENDARY_RARITY)) {
            List<EnchantmentLevelEntry> generated = cir.getReturnValue();

            if (generated != null && generated.size() > 1) {
                List<EnchantmentLevelEntry> limited = new ArrayList<>();
                limited.add(generated.getFirst());
                cir.setReturnValue(limited);
            }
        }
    }
}