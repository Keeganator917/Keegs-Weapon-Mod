package com.keeganator.keegsweapons.item.custom;

import com.keeganator.keegsweapons.damagetypes.ModDamageTypes;
import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.item.KatanaItem;
import com.keeganator.keegsweapons.network.DashHandler;
import com.keeganator.keegsweapons.network.MyModNetwork;
import com.keeganator.keegsweapons.particles.ModParticles;
import com.keeganator.keegsweapons.util.DashKeyUtil;
import com.keeganator.keegsweapons.util.ModKeyBinding;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Consumer;

public class ShogunsKatanaItem extends KatanaItem {
    public ShogunsKatanaItem(ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Properties settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            if (world.isClientSide()) {
                if (DashKeyUtil.isDashBoundToRightClick(ModKeyBinding.DASH_KEY)) {
                    MyModNetwork.sendDash();
                    return InteractionResult.SUCCESS;
                }
            } else {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
       return InteractionResult.PASS;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity miner) {
        return true;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        var client = Minecraft.getInstance();
        if (client.level == null) return 0;

        long time = client.level.getGameTime();
        CompoundTag tag = getOrCreateCustomData(stack);

        long cooldownEnd = tag.getLong("CooldownEnd").orElse(0L);
        long remaining = Math.max(0, cooldownEnd - time);

        // Fully charged (2 dashes)
        if (remaining == 0) return 13;

        // Determine the recharge progress of the *current* 160-tick segment
        long currentChargeRemaining = remaining > DashHandler.HALF_COOLDOWN ?
                remaining - DashHandler.HALF_COOLDOWN :
                remaining;

        float progress = 1f - ((float) currentChargeRemaining / DashHandler.HALF_COOLDOWN);
        return Math.round(progress * 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        var client = Minecraft.getInstance();
        if (client.level == null) return 0xD4AF37;

        long time = client.level.getGameTime();
        CompoundTag tag = getOrCreateCustomData(stack);
        long cooldownEnd = tag.getLong("CooldownEnd").orElse(0L);

        long remaining = Math.max(0, cooldownEnd - time);

        // Gold when fully charged
        if (remaining == 0) return 0xD4AF37;

        // Red if 0 dashes available (working on 1st charge)
        // Gold if 1 dash available (working on 2nd charge)
        return remaining > DashHandler.HALF_COOLDOWN ? 0xFF5555 : 0xD4AF37;
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

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker.level() instanceof ServerLevel serverWorld)) return;

        if (attacker instanceof Player player && shouldDoSweep(player)) {
            float yaw = player.getYRot() * (Mth.PI / 180F);
            double ox = -Mth.sin(yaw);
            double oz = Mth.cos(yaw);

            serverWorld.sendParticles(ModParticles.SHOGUNS_KATANA_SWEEP_PARTICLES,
                    player.getX() + ox, player.getY(0.5D), player.getZ() + oz,
                    0, 0, 0, 0, 0);

            serverWorld.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public DamageSource getItemDamageSource(LivingEntity attacker) {
        if (!(attacker.level() instanceof ServerLevel serverLevel)) {
            return super.getItemDamageSource(attacker);
        }

        return ModDamageTypes.shogunsKatanaAttack(serverLevel, attacker);
    }

    private boolean shouldDoSweep(Player player) {
        if (!(player.getAttackStrengthScale(0.5F) <= 0.9F)) return false;
        if (!player.onGround()) return false;
        if (player.isInWater() || player.onClimbable()) return false;
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.literal("The Apprentice Becomes The Master").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
        if (enchantment.is(Enchantments.KNOCKBACK)) return true;

        if (enchantment.is(Enchantments.SHARPNESS)
                || enchantment.is(Enchantments.BANE_OF_ARTHROPODS)
                || enchantment.is(Enchantments.SMITE)
                || enchantment.is(Enchantments.UNBREAKING)
                || enchantment.is(Enchantments.MENDING)
                || enchantment.is(Enchantments.FIRE_ASPECT)
                || enchantment.is(Enchantments.LOOTING)
                || enchantment.is(Enchantments.SWEEPING_EDGE)
                || enchantment.is(Enchantments.VANISHING_CURSE)
                || enchantment.is(ModEnchantments.DASH)
                || enchantment.is(ModEnchantments.THUNDERING)
                || enchantment.is(ModEnchantments.POISON_TIPPED)
                || enchantment.is(ModEnchantments.LEECH)
                || enchantment.is(ModEnchantments.EXPERT)
                || enchantment.is(ModEnchantments.ILLAGER_BANE)
                || enchantment.is(ModEnchantments.RAGE)
                || enchantment.is(ModEnchantments.FROSTFALL)
                || enchantment.is(ModEnchantments.GRAVITY)
        ) {
            return false;
        }

        return enchantment.value().canEnchant(stack);
    }
}
