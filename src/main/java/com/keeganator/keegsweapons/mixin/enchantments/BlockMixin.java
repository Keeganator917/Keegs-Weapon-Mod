package com.keeganator.keegsweapons.mixin.enchantments;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "playerDestroy", at = @At("TAIL"))
    private void keegsweapons$expertBonusXp(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
        if (world.isClientSide()) return;
        if (tool.isEmpty()) return;

        Holder<Enchantment> poisonTipped =
                player.level()
                        .registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(ModEnchantments.EXPERT)
                        .orElse(null);

        if (poisonTipped != null) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, tool);
            if (level <= 0) return;

            if (!state.is(ModTags.Blocks.EXPERIENCE_BLOCKS)) return;

            if (!(world instanceof ServerLevel serverWorld)) return;

            if (state.is(Blocks.SCULK)) {
                ExperienceOrb.award(serverWorld, Vec3.atCenterOf(pos), level);
            } else {
                ExperienceOrb.award(serverWorld, Vec3.atCenterOf(pos), level * 2);
            }
        }
    }
}