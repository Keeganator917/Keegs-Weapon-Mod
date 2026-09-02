package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(FishingBobberEntity.class)
public abstract class FishingMixin {

    @Inject(method = "use", at = @At("TAIL"))
    private void keegsweapons$onFish(ItemStack rod, CallbackInfoReturnable<Integer> cir) {
        FishingBobberEntity bobber = (FishingBobberEntity) (Object) this;
        Entity owner = bobber.getOwner();
        if (!(owner instanceof ServerPlayerEntity player)) return;

        RegistryEntry<Enchantment> poisonTipped =
                player.getEntityWorld()
                        .getRegistryManager()
                        .getOrThrow(RegistryKeys.ENCHANTMENT)
                        .getOptional(ModEnchantments.EXPERT)
                        .orElse(null);

        if (poisonTipped != null) {
            int level = EnchantmentHelper.getLevel(poisonTipped, rod);
            if (level <= 0) return;

            ExperienceOrbEntity.spawn(
                    player.getEntityWorld(),
                    Vec3d.ofCenter(player.getBlockPos()),
                    level * 2
            );
        }
    }
}