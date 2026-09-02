package com.keeganator.keegsweapons.network;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.util.DoubleJumpUser;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class DoubleJumpHandler {

    public static void tryDoubleJump(ServerPlayer player) {
        DoubleJumpUser data = (DoubleJumpUser)player;

        if (player.onGround()) {
            return;
        }


        if (data.keegsweapons$usedDoubleJump()) {
            return;
        }

        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

        Holder<Enchantment> poisonTipped =
                player.level()
                        .registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(ModEnchantments.DOUBLE_JUMP)
                        .orElse(null);

        if (poisonTipped != null) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, boots);

            if (level > 0) {
                Vec3 v = player.getDeltaMovement();
                player.setDeltaMovement(v.x(), 0.55, v.z());
                player.connection.send(new ClientboundSetEntityMotionPacket(player));
                player.needsSync = true;

                data.keegsweapons$setUsedDoubleJump(true);


                player.level().sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY(), player.getZ(),
                        15, 0.2, 0.1, 0.2, 0.05);
                player.playSound(SoundEvents.BREEZE_JUMP, 0.3F, 1.5F);
            }
        }
    }
}
