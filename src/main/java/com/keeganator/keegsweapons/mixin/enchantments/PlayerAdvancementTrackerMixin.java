package com.keeganator.keegsweapons.mixin.enchantments;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementTrackerMixin {

    @Shadow
    private ServerPlayer player;

    @Inject(method = "award", at = @At("TAIL"))
    private void onGrantCriterion(AdvancementHolder advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || !this.player.getAdvancements().getOrStartProgress(advancement).isDone()) {
            return;
        }

        AdvancementRewards rewards = advancement.value().rewards();
        int baseXp = rewards.experience();
        if (baseXp <= 0) {
            return;
        }

        ItemStack chestplate = this.player.getItemBySlot(EquipmentSlot.CHEST);

        Holder<Enchantment> poisonTipped =
                player.level()
                        .registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(ModEnchantments.EXPERT)
                        .orElse(null);

        if (poisonTipped != null) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, chestplate);
            if (level > 0) {
                int extraXp = baseXp * level;

                ExperienceOrb.award(this.player.level(), this.player.position(), extraXp);
            }
        }
    }
}