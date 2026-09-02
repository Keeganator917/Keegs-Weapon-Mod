package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class FurnaceSmeltMixin {

    @Inject(method = "dropExperienceForRecipesUsed", at = @At("HEAD"))
    private void keegsBonusXp(ServerPlayerEntity player, CallbackInfo ci) {

        ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
        if (chest.isEmpty()) return;

        RegistryEntry<Enchantment> poisonTipped =
                player.getEntityWorld()
                        .getRegistryManager()
                        .getOrThrow(RegistryKeys.ENCHANTMENT)
                        .getOptional(ModEnchantments.EXPERT)
                        .orElse(null);

        if (poisonTipped != null) {
            int level = EnchantmentHelper.getLevel(poisonTipped, chest);
            if (level <= 0) return;

            ServerWorld world = player.getEntityWorld();
            ExperienceOrbEntity.spawn(world, Vec3d.ofCenter(player.getBlockPos()), level);
        }
    }
}