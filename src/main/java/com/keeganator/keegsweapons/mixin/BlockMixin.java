package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "afterBreak", at = @At("TAIL"))
    private void keegsweapons$expertBonusXp(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
        if (world.isClient()) return;
        if (tool.isEmpty()) return;

        RegistryEntry<Enchantment> poisonTipped =
                player.getEntityWorld()
                        .getRegistryManager()
                        .getOrThrow(RegistryKeys.ENCHANTMENT)
                        .getOptional(ModEnchantments.EXPERT)
                        .orElse(null);

        if (poisonTipped != null) {
            int level = EnchantmentHelper.getLevel(poisonTipped, tool);
            if (level <= 0) return;

            if (!state.isIn(ModTags.Blocks.EXPERIENCE_BLOCKS)) return;

            if (!(world instanceof ServerWorld serverWorld)) return;

            if (state.isOf(Blocks.SCULK)) {
                ExperienceOrbEntity.spawn(serverWorld, Vec3d.ofCenter(pos), level);
            } else {
                ExperienceOrbEntity.spawn(serverWorld, Vec3d.ofCenter(pos), level * 2);
            }
        }
    }
}