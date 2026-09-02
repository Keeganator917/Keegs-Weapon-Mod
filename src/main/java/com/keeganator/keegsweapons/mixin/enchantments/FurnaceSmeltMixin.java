package com.keeganator.keegsweapons.mixin.enchantments;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.phys.Vec3;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class FurnaceSmeltMixin {

    @Inject(method = "awardUsedRecipesAndPopExperience", at = @At("HEAD"))
    private void keegsBonusXp(ServerPlayer player, CallbackInfo ci) {

        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chest.isEmpty()) return;

        Holder<Enchantment> poisonTipped =
                player.level()
                        .registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(ModEnchantments.EXPERT)
                        .orElse(null);

        if (poisonTipped != null) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, chest);
            if (level <= 0) return;

            ServerLevel world = player.level();
            ExperienceOrb.award(world, Vec3.atCenterOf(player.blockPosition()), level);
        }
    }
}