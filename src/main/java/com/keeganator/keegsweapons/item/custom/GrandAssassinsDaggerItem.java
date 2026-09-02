package com.keeganator.keegsweapons.item.custom;

import com.keeganator.keegsweapons.damagetypes.ModDamageTypes;
import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.item.DaggerItem;
import com.keeganator.keegsweapons.particles.ModParticles;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import java.util.function.Consumer;

public class GrandAssassinsDaggerItem extends DaggerItem {
    public GrandAssassinsDaggerItem(ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Properties settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.attackDamage = attackDamage + toolMaterial.attackDamageBonus();
    }

    private final float attackDamage;

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        double x = user.getX();
        double y = user.getY();
        double z = user.getZ();

        user.addEffect(new MobEffectInstance(MobEffects.SPEED, 350, 2));
        user.addEffect(new MobEffectInstance(MobEffects.SATURATION, 600, 1));

        if (!world.isClientSide()) {
            ServerLevel serverWorld = (ServerLevel) world;

            for (int i = 0; i < 10; i++) {
                serverWorld.sendParticles(ModParticles.WINGED_GRACE_PARTICLES,
                        x + world.getRandom().nextFloat() - 0.5, y + world.getRandom().nextFloat(), z + world.getRandom().nextFloat() - 0.5,
                        1, 0.2, 0.3, 0.2, 0.05);
            }
        }

        user.getCooldowns().addCooldown(this.getDefaultInstance(), 800);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker.level() instanceof ServerLevel serverWorld)) return;

        DamageSource source = attacker instanceof Player player
                ? attacker.damageSources().playerAttack(player)
                : attacker.damageSources().mobAttack(attacker);

        float finalDamage = attackDamage;

        if (isBackstab(attacker, target)) {
            finalDamage *= 1.5f;

            serverWorld.sendParticles(
                    ModParticles.BACKSTAB_PARTICLES,
                    target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(),
                    6, 0.2, 0.2, 0.2, 0.05);

            serverWorld.playSound(null, attacker.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        target.hurtServer(serverWorld, source, finalDamage);

        if (attacker instanceof Player player && shouldDoSweep(player)) {
            float yaw = player.getYRot() * (Mth.PI / 180F);
            double ox = -Mth.sin(yaw);
            double oz = Mth.cos(yaw);

            serverWorld.sendParticles(ModParticles.GRAND_ASSASSINS_DAGGER_SWEEP_PARTICLES,
                    player.getX() + ox, player.getY(0.5D), player.getZ() + oz,
                    0, 0, 0, 0, 0);

            serverWorld.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

    }

    @Override
    public DamageSource getItemDamageSource(LivingEntity attacker) {
        if (!(attacker.level() instanceof ServerLevel serverLevel)) {
            return super.getItemDamageSource(attacker);
        }

        return ModDamageTypes.grandAssassinsDaggerAttack(serverLevel, attacker);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return false;
    }

    private boolean isBackstab(LivingEntity attacker, LivingEntity target) {
        Vec3 targetLook = new Vec3(target.getLookAngle().x, 0, target.getLookAngle().z).normalize();
        Vec3 attackerVec = new Vec3(attacker.getX() - target.getX(), 0, attacker.getZ() - target.getZ()).normalize();
        return targetLook.dot(attackerVec) < -0.5;
    }

    private boolean shouldDoSweep(Player player) {
        if (!(player.getAttackStrengthScale(0.5F) <= 0.9F)) return false;
        if (!player.onGround()) return false;
        if (player.isInWater() || player.onClimbable()) return false;
        return true;
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.literal("trust no one").withStyle(net.minecraft.ChatFormatting.DARK_RED, net.minecraft.ChatFormatting.ITALIC));
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
        if (enchantment.is(Enchantments.FIRE_ASPECT)) return true;

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
                || enchantment.is(ModEnchantments.THUNDERING)
                || enchantment.is(ModEnchantments.POISON_TIPPED)
                || enchantment.is(ModEnchantments.LEECH)
                || enchantment.is(ModEnchantments.EXPERT)
                || enchantment.is(ModEnchantments.ILLAGER_BANE)
                || enchantment.is(ModEnchantments.FROSTFALL)
                || enchantment.is(ModEnchantments.GRAVITY)
                || enchantment.is(ModEnchantments.RAGE)
        ) {
            return false;
        }

        return enchantment.value().canEnchant(stack);
    }
}