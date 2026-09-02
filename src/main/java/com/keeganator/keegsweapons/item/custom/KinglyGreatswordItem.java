package com.keeganator.keegsweapons.item.custom;

import com.keeganator.keegsweapons.damagetypes.ModDamageTypes;
import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.item.GreatswordItem;
import com.keeganator.keegsweapons.particles.ModParticles;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

public class KinglyGreatswordItem extends GreatswordItem {
    public KinglyGreatswordItem(ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {

        if (!world.isClient() && !user.getItemCooldownManager().isCoolingDown(this.getDefaultStack())) {
            double radius = 5.0;

            List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, user.getBoundingBox().expand(radius), entity -> entity != user);

            for (LivingEntity target : entities) {
                Vec3d direction = Vec3d.of(target.getBlockPos().subtract(user.getBlockPos()));

                if (direction.lengthSquared() > 0.0001) {
                    direction = direction.normalize();

                    target.damage((ServerWorld) world, ModDamageTypes.shockwave((ServerWorld) world, user), 4.0F);
                    target.addVelocity(direction.x * 1.2, 0.35, direction.z * 1.2);

                    target.velocityDirty = true;
                    //user.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(user));  // Make it sync with players at some point
                }
            }

            ((ServerWorld) world).spawnParticles(ModParticles.SHOCKWAVE, user.getX(), user.getY() + 0.6, user.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            ((ServerWorld) world).spawnParticles(ModParticles.SHOCKWAVE, user.getX(), user.getY() + 0.4, user.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.7F, 1.4F);
        }

        user.getItemCooldownManager().set(this.getDefaultStack(), 320);
        return ActionResult.SUCCESS;
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker.getEntityWorld() instanceof ServerWorld serverWorld)) return;

        if (attacker instanceof PlayerEntity player && shouldDoSweep(player)) {
            float yaw = player.getYaw() * (MathHelper.PI / 180F);
            double ox = -MathHelper.sin(yaw);
            double oz = MathHelper.cos(yaw);

            serverWorld.spawnParticles(ModParticles.KINGLY_GREATSWORD_SWEEP_PARTICLES,
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

        return ModDamageTypes.kinglyGreatswordAttack(serverLevel, attacker);
    }

    private boolean shouldDoSweep(PlayerEntity player) {
        if (!(player.getAttackCooldownProgress(0.5F) <= 0.9F)) return false;
        if (!player.isOnGround()) return false;
        if (player.isTouchingWater() || player.isClimbing()) return false;
        return true;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.literal("Feel The Wrath Of Royalty").formatted(Formatting.GOLD, Formatting.BOLD));
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        if (enchantment.matchesKey(Enchantments.FIRE_ASPECT) || enchantment.matchesKey(ModEnchantments.THUNDERING)) return true;

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
                || enchantment.matchesKey(ModEnchantments.POISON_TIPPED)
                || enchantment.matchesKey(ModEnchantments.LEECH)
                || enchantment.matchesKey(ModEnchantments.EXPERT)
                || enchantment.matchesKey(ModEnchantments.ILLAGER_BANE)
                || enchantment.matchesKey(ModEnchantments.GRAVITY)
                || enchantment.matchesKey(ModEnchantments.FROSTFALL)
                || enchantment.matchesKey(ModEnchantments.RAGE)
        ) {
            return false;
        }

        return enchantment.value().isAcceptableItem(stack);
    }

}
