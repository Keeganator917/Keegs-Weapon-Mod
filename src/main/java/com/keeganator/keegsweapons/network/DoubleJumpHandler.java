package com.keeganator.keegsweapons.network;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.util.DoubleJumpUser;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class DoubleJumpHandler {

    public static void tryDoubleJump(ServerPlayerEntity player) {
        DoubleJumpUser data = (DoubleJumpUser)player;

        if (player.isOnGround()) {
            return;
        }


        if (data.keegsweapons$usedDoubleJump()) {
            return;
        }

        ItemStack boots = player.getEquippedStack(EquipmentSlot.FEET);

        RegistryEntry<Enchantment> poisonTipped =
                player.getEntityWorld()
                        .getRegistryManager()
                        .getOrThrow(RegistryKeys.ENCHANTMENT)
                        .getOptional(ModEnchantments.DOUBLE_JUMP)
                        .orElse(null);

        if (poisonTipped != null) {
            int level = EnchantmentHelper.getLevel(poisonTipped, boots);

            if (level > 0) {
                Vec3d v = player.getVelocity();
                player.setVelocity(v.getX(), 0.55, v.getZ());
                player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
                player.velocityDirty = true;

                data.keegsweapons$setUsedDoubleJump(true);


                player.getEntityWorld().spawnParticles(ParticleTypes.CLOUD, player.getX(), player.getY(), player.getZ(),
                        15, 0.2, 0.1, 0.2, 0.05);
                player.playSound(SoundEvents.ENTITY_BREEZE_JUMP, 0.3F, 1.5F);
            }
        }
    }
}
