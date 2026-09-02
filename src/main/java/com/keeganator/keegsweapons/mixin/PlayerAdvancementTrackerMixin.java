package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin {

    @Shadow
    private ServerPlayerEntity owner;

    @Inject(method = "grantCriterion", at = @At("TAIL"))
    private void onGrantCriterion(AdvancementEntry advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || !this.owner.getAdvancementTracker().getProgress(advancement).isDone()) {
            return;
        }

        AdvancementRewards rewards = advancement.value().rewards();
        int baseXp = rewards.experience();
        if (baseXp <= 0) {
            return;
        }

        ItemStack chestplate = this.owner.getEquippedStack(EquipmentSlot.CHEST);

        RegistryEntry<Enchantment> poisonTipped =
                owner.getEntityWorld()
                        .getRegistryManager()
                        .getOrThrow(RegistryKeys.ENCHANTMENT)
                        .getOptional(ModEnchantments.EXPERT)
                        .orElse(null);

        if (poisonTipped != null) {
            int level = EnchantmentHelper.getLevel(poisonTipped, chestplate);
            if (level > 0) {
                int extraXp = baseXp * level;

                ExperienceOrbEntity.spawn(this.owner.getEntityWorld(), this.owner.getEntityPos(), extraXp);
            }
        }
    }
}