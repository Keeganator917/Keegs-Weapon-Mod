package com.keeganator.keegsweapons.mixin.util;

import com.keeganator.keegsweapons.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "selectEnchantment", at = @At("RETURN"), cancellable = true)
    private static void limitLegendaryEnchantingTable(RandomSource random, ItemStack stack, int level, Stream<Holder<Enchantment>> possibleEnchantments,
                                                      CallbackInfoReturnable<List<EnchantmentInstance>> cir) {

        if (stack.is(ModTags.Items.LEGENDARY_RARITY)) {
            List<EnchantmentInstance> generated = cir.getReturnValue();

            if (generated != null && generated.size() > 1) {
                List<EnchantmentInstance> limited = new ArrayList<>();
                limited.add(generated.getFirst());
                cir.setReturnValue(limited);
            }
        }
    }
}