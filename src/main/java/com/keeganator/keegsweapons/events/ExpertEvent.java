package com.keeganator.keegsweapons.events;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class ExpertEvent {
    public static void register() {

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (!(source.getAttacker() instanceof PlayerEntity player)) return;
            if (!(player.getEntityWorld() instanceof ServerWorld world)) return;

            RegistryEntry<Enchantment> poisonTipped =
                    player.getEntityWorld()
                            .getRegistryManager()
                            .getOrThrow(RegistryKeys.ENCHANTMENT)
                            .getOptional(ModEnchantments.EXPERT)
                            .orElse(null);

            if (poisonTipped != null) {
                int level = EnchantmentHelper.getLevel(poisonTipped, player.getMainHandStack());
                if (level <= 0) return;

                BlockPos pos = player.getBlockPos();
                int extraOrbs = level * 2;

                for (int i = 0; i < extraOrbs; i++) {
                    ExperienceOrbEntity.spawn(world, Vec3d.of(pos), 1);
                }
            }
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient()) return ActionResult.PASS;
            if (!(player instanceof ServerPlayerEntity serverPlayer)) return ActionResult.PASS;
            if (!(entity instanceof MooshroomEntity mooshroom)) return ActionResult.PASS;

            ItemStack stack = player.getStackInHand(hand);
            if (!(stack.getItem() instanceof ShearsItem)) return ActionResult.PASS;
            if (!mooshroom.isShearable()) return ActionResult.PASS;

            RegistryEntry<Enchantment> poisonTipped =
                    player.getEntityWorld()
                            .getRegistryManager()
                            .getOrThrow(RegistryKeys.ENCHANTMENT)
                            .getOptional(ModEnchantments.EXPERT)
                            .orElse(null);

            if (poisonTipped != null) {
                int level = EnchantmentHelper.getLevel(poisonTipped, player.getMainHandStack());
                if (level > 0) {
                    int extraXp = Math.max(1, (int) (level * 0.5f));
                    ExperienceOrbEntity.spawn(
                            (ServerWorld) world,
                            Vec3d.ofCenter(entity.getBlockPos()),
                            extraXp
                    );
                }
            }

            return ActionResult.PASS;
        });

    }
}
