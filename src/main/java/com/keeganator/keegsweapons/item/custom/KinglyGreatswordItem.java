package com.keeganator.keegsweapons.item.custom;

import com.keeganator.keegsweapons.damagetypes.ModDamageTypes;
import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.item.GreatswordItem;
import com.keeganator.keegsweapons.particles.ModParticles;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.List;
import java.util.function.Consumer;

public class KinglyGreatswordItem extends GreatswordItem {
    public KinglyGreatswordItem(ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Properties settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {

        if (!world.isClientSide() && !user.getCooldowns().isOnCooldown(this.getDefaultInstance())) {
            double radius = 5.0;

            List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(radius), entity -> entity != user);

            for (LivingEntity target : entities) {
                Vec3 direction = Vec3.atLowerCornerOf(target.blockPosition().subtract(user.blockPosition()));

                if (direction.lengthSqr() > 0.0001) {
                    direction = direction.normalize();

                    target.hurtServer((ServerLevel) world, ModDamageTypes.shockwave((ServerLevel) world, user), 4.0F);
                    target.push(direction.x * 1.2, 0.35, direction.z * 1.2);

                    target.needsSync = true;

                    //user.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(user));  // Make it sync with players at some point
                }
            }

            ((ServerLevel) world).sendParticles(ModParticles.SHOCKWAVE, user.getX(), user.getY() + 0.6, user.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            ((ServerLevel) world).sendParticles(ModParticles.SHOCKWAVE, user.getX(), user.getY() + 0.4, user.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 0.7F, 1.4F);
        }

        user.getCooldowns().addCooldown(this.getDefaultInstance(), 320); //set at like 300 normally
        return InteractionResult.SUCCESS;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker.level() instanceof ServerLevel serverWorld)) return;

        if (attacker instanceof Player player && shouldDoSweep(player)) {
            float yaw = player.getYRot() * (Mth.PI / 180F);
            double ox = -Mth.sin(yaw);
            double oz = Mth.cos(yaw);

            serverWorld.sendParticles(ModParticles.KINGLY_GREATSWORD_SWEEP_PARTICLES,
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

        return ModDamageTypes.kinglyGreatswordAttack(serverLevel, attacker);
    }

    private boolean shouldDoSweep(Player player) {
        if (!(player.getAttackStrengthScale(0.5F) <= 0.9F)) return false;
        if (!player.onGround()) return false;
        if (player.isInWater() || player.onClimbable()) return false;
        return true;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.literal("Feel The Wrath Of Royalty").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
        if (enchantment.is(Enchantments.FIRE_ASPECT) || enchantment.is(ModEnchantments.THUNDERING)) return true;

        if (enchantment.is(Enchantments.SHARPNESS)
                || enchantment.is(Enchantments.BANE_OF_ARTHROPODS)
                || enchantment.is(Enchantments.SMITE)
                || enchantment.is(Enchantments.UNBREAKING)
                || enchantment.is(Enchantments.MENDING)
                || enchantment.is(Enchantments.KNOCKBACK)
                || enchantment.is(Enchantments.LOOTING)
                || enchantment.is(Enchantments.SWEEPING_EDGE)
                || enchantment.is(Enchantments.VANISHING_CURSE)
                || enchantment.is(ModEnchantments.DASH)
                || enchantment.is(ModEnchantments.POISON_TIPPED)
                || enchantment.is(ModEnchantments.LEECH)
                || enchantment.is(ModEnchantments.EXPERT)
                || enchantment.is(ModEnchantments.ILLAGER_BANE)
                || enchantment.is(ModEnchantments.GRAVITY)
                || enchantment.is(ModEnchantments.FROSTFALL)
                || enchantment.is(ModEnchantments.RAGE)
        ) {
            return false;
        }

        return enchantment.value().canEnchant(stack);
    }

}
