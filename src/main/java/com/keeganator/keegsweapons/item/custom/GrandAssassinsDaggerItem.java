package com.keeganator.keegsweapons.item.custom;

import com.keeganator.keegsweapons.damagetypes.ModDamageTypes;
import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.item.DaggerItem;
import com.keeganator.keegsweapons.particles.ModParticles;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class GrandAssassinsDaggerItem extends DaggerItem {
    public GrandAssassinsDaggerItem(ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.attackDamage = attackDamage + toolMaterial.attackDamageBonus();
    }

    private final float attackDamage;

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        double x = user.getX();
        double y = user.getY();
        double z = user.getZ();

        user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 350, 2));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 600, 1));

        if (!world.isClient()) {
            ServerWorld serverWorld = (ServerWorld) world;

            for (int i = 0; i < 10; i++) {
                serverWorld.spawnParticles(ModParticles.WINGED_GRACE_PARTICLES,
                        x + world.random.nextFloat() - 0.5, y + world.random.nextFloat(), z + world.random.nextFloat() - 0.5,
                        1, 0.2, 0.3, 0.2, 0.05);
            }
        }

        user.getItemCooldownManager().set(this.getDefaultStack(), 800);
        return ActionResult.SUCCESS;
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker.getEntityWorld() instanceof ServerWorld serverWorld)) return;

        DamageSource source = attacker instanceof PlayerEntity player
                ? attacker.getDamageSources().playerAttack(player)
                : attacker.getDamageSources().mobAttack(attacker);

        float finalDamage = attackDamage;

        if (isBackstab(attacker, target)) {
            finalDamage *= 1.5f;

            serverWorld.spawnParticles(
                    ModParticles.BACKSTAB_PARTICLES,
                    target.getX(), target.getY() + target.getHeight() / 2, target.getZ(),
                    6, 0.2, 0.2, 0.2, 0.05);

            serverWorld.playSound(null, attacker.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }

        target.damage(serverWorld, source, finalDamage);

        if (attacker instanceof PlayerEntity player && shouldDoSweep(player)) {
            float yaw = player.getYaw() * (MathHelper.PI / 180F);
            double ox = -MathHelper.sin(yaw);
            double oz = MathHelper.cos(yaw);

            serverWorld.spawnParticles(ModParticles.GRAND_ASSASSINS_DAGGER_SWEEP_PARTICLES,
                    player.getX() + ox, player.getBodyY(0.5D), player.getZ() + oz,
                    0, 0, 0, 0, 0);

            serverWorld.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {

    }

    @Override
    public DamageSource getDamageSource(LivingEntity attacker) {
        if (!(attacker.getEntityWorld() instanceof ServerWorld serverLevel)) {
            return super.getDamageSource(attacker);
        }

        return ModDamageTypes.grandAssassinsDaggerAttack(serverLevel, attacker);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return false;
    }

    private boolean isBackstab(LivingEntity attacker, LivingEntity target) {
        Vec3d targetLook = new Vec3d(target.getRotationVector().x, 0, target.getRotationVector().z).normalize();
        Vec3d attackerVec = new Vec3d(attacker.getX() - target.getX(), 0, attacker.getZ() - target.getZ()).normalize();
        return targetLook.dotProduct(attackerVec) < -0.5;
    }

    private boolean shouldDoSweep(PlayerEntity player) {
        if (!(player.getAttackCooldownProgress(0.5F) <= 0.9F)) return false;
        if (!player.isOnGround()) return false;
        if (player.isTouchingWater() || player.isClimbing()) return false;
        return true;
    }


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.literal("trust no one").formatted(net.minecraft.util.Formatting.DARK_RED, net.minecraft.util.Formatting.ITALIC));
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        if (enchantment.matchesKey(Enchantments.FIRE_ASPECT)) return true;

        if (enchantment.matchesKey(Enchantments.SHARPNESS)
                || enchantment.matchesKey(Enchantments.BANE_OF_ARTHROPODS)
                || enchantment.matchesKey(Enchantments.SMITE)
                || enchantment.matchesKey(Enchantments.UNBREAKING)
                || enchantment.matchesKey(Enchantments.MENDING)
                || enchantment.matchesKey(Enchantments.KNOCKBACK)
                || enchantment.matchesKey(Enchantments.LOOTING)
                || enchantment.matchesKey(Enchantments.SWEEPING_EDGE)
                || enchantment.matchesKey(Enchantments.VANISHING_CURSE)
                || enchantment.matchesKey(ModEnchantments.DASH)
                || enchantment.matchesKey(ModEnchantments.THUNDERING)
                || enchantment.matchesKey(ModEnchantments.POISON_TIPPED)
                || enchantment.matchesKey(ModEnchantments.LEECH)
                || enchantment.matchesKey(ModEnchantments.EXPERT)
                || enchantment.matchesKey(ModEnchantments.ILLAGER_BANE)
                || enchantment.matchesKey(ModEnchantments.FROSTFALL)
                || enchantment.matchesKey(ModEnchantments.GRAVITY)
                || enchantment.matchesKey(ModEnchantments.RAGE)
        ) {
            return false;
        }

        return enchantment.value().isAcceptableItem(stack);
    }
}