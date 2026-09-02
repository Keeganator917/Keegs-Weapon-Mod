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
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class ShogunsKatanaItem extends KatanaItem {
    public ShogunsKatanaItem(ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            if (world.isClient()) {
                if (DashKeyUtil.isDashBoundToRightClick(ModKeyBinding.DASH_KEY)) {
                    MyModNetwork.sendDash();
                    return ActionResult.SUCCESS;
                }
            } else {
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        }
       return ActionResult.PASS;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        return true;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        var client = MinecraftClient.getInstance();
        if (client.world == null) return 0;

        long time = client.world.getTime();
        NbtCompound tag = getOrCreateCustomData(stack);

        long cooldownEnd = tag.getLong("CooldownEnd").orElse(0L);
        long remaining = Math.max(0, cooldownEnd - time);

        // Fully charged
        if (remaining == 0) return 13;

        // Recharge progres
        long currentChargeRemaining = remaining > DashHandler.HALF_COOLDOWN ? remaining - DashHandler.HALF_COOLDOWN : remaining;

        float progress = 1f - ((float) currentChargeRemaining / DashHandler.HALF_COOLDOWN);
        return Math.round(progress * 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        var client = MinecraftClient.getInstance();
        if (client.world == null) return 0xD4AF37;

        long time = client.world.getTime();
        NbtCompound tag = getOrCreateCustomData(stack);
        long cooldownEnd = tag.getLong("CooldownEnd").orElse(0L);

        long remaining = Math.max(0, cooldownEnd - time);

        // Gold when fully charged
        if (remaining == 0) return 0xD4AF37;

        // Red if 0 dashes available, Gold if 1 dash available
        return remaining > DashHandler.HALF_COOLDOWN ? 0xFF5555 : 0xD4AF37;
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

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker.getEntityWorld() instanceof ServerWorld serverWorld)) return;

        if (attacker instanceof PlayerEntity player && shouldDoSweep(player)) {
            float yaw = player.getYaw() * (MathHelper.PI / 180F);
            double ox = -MathHelper.sin(yaw);
            double oz = MathHelper.cos(yaw);

            serverWorld.spawnParticles(ModParticles.SHOGUNS_KATANA_SWEEP_PARTICLES,
                    player.getX() + ox, player.getBodyY(0.5D), player.getZ() + oz,
                    0, 0, 0, 0, 0);

            serverWorld.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public DamageSource getDamageSource(LivingEntity attacker) {
        if (!(attacker.getEntityWorld() instanceof ServerWorld serverLevel)) {
            return super.getDamageSource(attacker);
        }

        return ModDamageTypes.shogunsKatanaAttack(serverLevel, attacker);
    }

    private boolean shouldDoSweep(PlayerEntity player) {
        if (!(player.getAttackCooldownProgress(0.5F) <= 0.9F)) return false;
        if (!player.isOnGround()) return false;
        if (player.isTouchingWater() || player.isClimbing()) return false;
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.literal("The Apprentice Becomes The Master").formatted(Formatting.GOLD, Formatting.ITALIC));
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        if (enchantment.matchesKey(Enchantments.KNOCKBACK)) return true;

        if (enchantment.matchesKey(Enchantments.SHARPNESS)
                || enchantment.matchesKey(Enchantments.BANE_OF_ARTHROPODS)
                || enchantment.matchesKey(Enchantments.SMITE)
                || enchantment.matchesKey(Enchantments.UNBREAKING)
                || enchantment.matchesKey(Enchantments.MENDING)
                || enchantment.matchesKey(Enchantments.FIRE_ASPECT)
                || enchantment.matchesKey(Enchantments.LOOTING)
                || enchantment.matchesKey(Enchantments.SWEEPING_EDGE)
                || enchantment.matchesKey(Enchantments.VANISHING_CURSE)
                || enchantment.matchesKey(ModEnchantments.DASH)
                || enchantment.matchesKey(ModEnchantments.THUNDERING)
                || enchantment.matchesKey(ModEnchantments.POISON_TIPPED)
                || enchantment.matchesKey(ModEnchantments.LEECH)
                || enchantment.matchesKey(ModEnchantments.EXPERT)
                || enchantment.matchesKey(ModEnchantments.ILLAGER_BANE)
                || enchantment.matchesKey(ModEnchantments.RAGE)
                || enchantment.matchesKey(ModEnchantments.FROSTFALL)
                || enchantment.matchesKey(ModEnchantments.GRAVITY)
        ) {
            return false;
        }

        return enchantment.value().isAcceptableItem(stack);
    }
}
