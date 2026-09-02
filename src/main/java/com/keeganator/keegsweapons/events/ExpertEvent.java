package com.keeganator.keegsweapons.events;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import java.util.Optional;

public class ExpertEvent {
    public static void register() {

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (!(source.getEntity() instanceof Player player)) return;
            if (!(player.level() instanceof ServerLevel world)) return;

            Holder<Enchantment> poisonTipped =
                    player.level()
                            .registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .get(ModEnchantments.EXPERT)
                            .orElse(null);

            if (poisonTipped != null) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, player.getMainHandItem());
                if (level <= 0) return;

                BlockPos pos = player.blockPosition();
                int extraOrbs = level * 2;

                for (int i = 0; i < extraOrbs; i++) {
                    ExperienceOrb.award(world, Vec3.atLowerCornerOf(pos), 1);
                }
            }
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClientSide()) return InteractionResult.PASS;
            if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.PASS;
            if (!(entity instanceof MushroomCow mooshroom)) return InteractionResult.PASS;

            ItemStack stack = player.getItemInHand(hand);
            if (!(stack.getItem() instanceof ShearsItem)) return InteractionResult.PASS;
            if (!mooshroom.readyForShearing()) return InteractionResult.PASS;

            Holder<Enchantment> poisonTipped =
                    player.level()
                            .registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .get(ModEnchantments.EXPERT)
                            .orElse(null);

            if (poisonTipped != null) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, player.getMainHandItem());
                if (level > 0) {
                    int extraXp = Math.max(1, (int) (level * 0.5f));
                    ExperienceOrb.award(
                            (ServerLevel) world,
                            Vec3.atCenterOf(entity.blockPosition()),
                            extraXp
                    );
                }
            }

            return InteractionResult.PASS;
        });

    }
}
