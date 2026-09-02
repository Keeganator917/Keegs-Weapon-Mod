package com.keeganator.keegsweapons.network;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.item.ModItems;
import com.keeganator.keegsweapons.item.custom.ShogunsKatanaItem;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class DashHandler {

    public static final int DASH_COOLDOWN_TICKS = 400; // Was 5 seconds (100 ticks)

    public static final int SHOGUNS_KATANA_COOLDOWN = 320;
    public static final int HALF_COOLDOWN = SHOGUNS_KATANA_COOLDOWN / 2;

    public static void tryDash(ServerPlayer player) {

        ItemStack stack = player.getMainHandItem();

        if (stack.is(ModItems.SHOGUNS_KATANA)) {
            long time = player.level().getGameTime();
            CompoundTag tag = getOrCreateCustomData(stack);

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

        Holder<Enchantment> poisonTipped =
                player.level()
                        .registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(ModEnchantments.DASH)
                        .orElse(null);

        if (poisonTipped != null) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, stack);
            if (level <= 0) return;

            if (player.getCooldowns().isOnCooldown(stack)) {
                return;
            }

            performDash(player);
            player.getCooldowns().addCooldown(stack, DASH_COOLDOWN_TICKS);
        }
    }

    private static void performDash(ServerPlayer player) {
        Vec3 look = player.getLookAngle().normalize().scale(1.2);
        player.setDeltaMovement(player.getDeltaMovement().add(look));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));

        ServerLevel world = player.level();
        world.sendParticles(
                ParticleTypes.CLOUD,
                player.getX(), player.getY(), player.getZ(),
                10, 0.2, 0.4, 0.2, 0.01
        );
    }

    private static void setDashData(ItemStack stack, long cooldownEnd, long dashTime) {
        CompoundTag nbt = new CompoundTag();

        var component = stack.get(DataComponents.CUSTOM_DATA);
        if (component != null) {
            nbt = component.copyTag();
        }

        nbt.putLong("CooldownEnd", cooldownEnd);
        nbt.putLong("LastDashTime", dashTime);

        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
    }
    private static CompoundTag getOrCreateCustomData(ItemStack stack) {
        CustomData component = stack.get(DataComponents.CUSTOM_DATA);

        if (component == null) {
            CompoundTag tag = new CompoundTag();
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            return tag;
        }

        return component.copyTag();
    }
}
