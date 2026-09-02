package com.keeganator.keegsweapons.mixin.enchantments;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

@Mixin(FishingHook.class)
public abstract class FishingMixin {

    @Inject(method = "retrieve", at = @At("TAIL"))
    private void keegsweapons$onFish(ItemStack rod, CallbackInfoReturnable<Integer> cir) {
        FishingHook bobber = (FishingHook) (Object) this;
        Entity owner = bobber.getOwner();
        if (!(owner instanceof ServerPlayer player)) return;

        Holder<Enchantment> poisonTipped =
                player.level()
                        .registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(ModEnchantments.EXPERT)
                        .orElse(null);

        if (poisonTipped != null) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, rod);
            if (level <= 0) return;

            ExperienceOrb.award(
                    player.level(),
                    Vec3.atCenterOf(player.blockPosition()),
                    level * 2
            );
        }
    }
}