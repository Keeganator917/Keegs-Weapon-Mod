package com.keeganator.keegsweapons.network;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.item.ModItems;
import com.keeganator.keegsweapons.item.custom.ShogunsKatanaItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class DashHandler {

    public static final int DASH_COOLDOWN_TICKS = 400; // Was 5 seconds (100 ticks)

    public static final int SHOGUNS_KATANA_COOLDOWN = 320;
    public static final int HALF_COOLDOWN = SHOGUNS_KATANA_COOLDOWN / 2;

    public static void tryDash(ServerPlayerEntity player) {

        ItemStack stack = player.getMainHandStack();

        if (stack.isOf(ModItems.SHOGUNS_KATANA)) {
            long time = player.getEntityWorld().getTime();
            NbtCompound tag = getOrCreateCustomData(stack);

            // Prevents double-firing if packets arrive in the same tick
            long lastDashTime = tag.getLong("LastDashTime").orElse(0L);
            if (time - lastDashTime < 5) return;

            long cooldownEnd = tag.getLong("CooldownEnd").orElse(0L);
            long remaining = Math.max(0, cooldownEnd - time);

            if (remaining <= HALF_COOLDOWN) {
                performDash(player);

                long newRemaining = remaining + HALF_COOLDOWN;
                long newCooldownEnd = time + newRemaining;

                setDashData(stack, newCooldownEnd, time);
                return;
            }
        }

        RegistryEntry<Enchantment> poisonTipped =
                player.getEntityWorld()
                        .getRegistryManager()
                        .getOrThrow(RegistryKeys.ENCHANTMENT)
                        .getOptional(ModEnchantments.DASH)
                        .orElse(null);

        if (poisonTipped != null) {
            int level = EnchantmentHelper.getLevel(poisonTipped, stack);
            if (level <= 0) return;

            if (player.getItemCooldownManager().isCoolingDown(stack)) {
                return;
            }

            performDash(player);
            player.getItemCooldownManager().set(stack, DASH_COOLDOWN_TICKS);
        }
    }

    private static void performDash(ServerPlayerEntity player) {
        Vec3d look = player.getRotationVector().normalize().multiply(1.2);
        player.setVelocity(player.getVelocity().add(look));
        player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));

        ServerWorld world = player.getEntityWorld();
        world.spawnParticles(
                ParticleTypes.CLOUD,
                player.getX(), player.getY(), player.getZ(),
                10, 0.2, 0.4, 0.2, 0.01
        );
    }

    private static void setDashData(ItemStack stack, long cooldownEnd, long dashTime) {
        NbtCompound nbt = new NbtCompound();

        var component = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (component != null) {
            nbt = component.copyNbt();
        }

        nbt.putLong("CooldownEnd", cooldownEnd);
        nbt.putLong("LastDashTime", dashTime);

        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }
    private static NbtCompound getOrCreateCustomData(ItemStack stack) {
        NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);

        if (component == null) {
            NbtCompound tag = new NbtCompound();
            stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
            return tag;
        }

        return component.copyNbt();
    }
}
