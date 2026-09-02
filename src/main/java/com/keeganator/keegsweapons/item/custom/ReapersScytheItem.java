package com.keeganator.keegsweapons.item.custom;

import com.keeganator.keegsweapons.damagetypes.ModDamageTypes;
import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.entity.ReapersScytheAbility;
import com.keeganator.keegsweapons.item.ScytheItem;
import com.keeganator.keegsweapons.particles.ModParticles;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;
import java.util.function.Consumer;

public class ReapersScytheItem extends ScytheItem {

    private final float attackDamage;

    public static final Color SOUL_BAR_COLOR = new Color(47, 134, 194);
    public static final int SOUL_BAR_RGB = SOUL_BAR_COLOR.getRGB();

    public ReapersScytheItem(ToolMaterial material, int attackDamageModifier, float attackSpeedModifier, Settings settings) {
        super(material, attackDamageModifier, attackSpeedModifier, settings);
        this.attackDamage = attackDamageModifier + material.attackDamageBonus();
    }


    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        NbtCompound tag = getOrCreateCustomData(stack);

        if (!user.getItemCooldownManager().isCoolingDown(this.getDefaultStack())) {

            Vec3d eyePos = new Vec3d(user.getX(), user.getEyeY(), user.getZ());
            Vec3d look = user.getRotationVec(1.0F).normalize();

            if (world.isClient()) {
                int souls = tag.getInt("Souls").orElse(0);

                float scale = 1.0F + Math.min(souls, 10) / 10.0F;
                for (int i = 1; i < 170; i++) {
                    Vec3d pos = eyePos.add(look.multiply(i));
                    world.addParticleClient(
                            ModParticles.REAPERS_SCYTHE_ABILITY_PARTICLES,
                            pos.x, pos.y, pos.z,
                            scale, 0, 0
                    );
                }

                tag.putInt("Souls", 0);
                saveCustomData(stack, tag);
            }

            if (!world.isClient()) {
                int souls = tag.getInt("Souls").orElse(0);

                ReapersScytheAbility ability = new ReapersScytheAbility(world, user, stack, souls);
                ability.setItem(Items.AIR.getDefaultStack()); // Prevents displaying item texture

                ability.setPos(user.getX(), user.getEyeY() - 0.1, user.getZ());
                ability.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 0.0F);

                world.spawnEntity(ability);

                tag.putInt("Souls", 0);
                saveCustomData(stack, tag);
                if (!user.isInCreativeMode()) {
                    user.getItemCooldownManager().set(this.getDefaultStack(), 600);
                } else if (user.isInCreativeMode()) {
                    user.getItemCooldownManager().set(this.getDefaultStack(), 20);
                }


                float scale = 1.0F + Math.min(souls, 10) / 10.0F;
                for (int i = 1; i < 170; i++) {
                    Vec3d pos = eyePos.add(look.multiply(i));
                    ((ServerWorld) world).spawnParticles(
                            ModParticles.REAPERS_SCYTHE_ABILITY_PARTICLES,
                            pos.x, pos.y, pos.z, 1, scale, 0, 0, 0
                    );
                }
            }

            user.playSound(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 0.6F, 0.5F);
        }

        return ActionResult.SUCCESS;
    }


    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (!attacker.getEntityWorld().isClient()) {
            NbtCompound tag = getOrCreateCustomData(stack);

            if (attacker instanceof PlayerEntity player && shouldDoSweep(player)) {
                double yaw = Math.toRadians(player.getYaw());
                double offsetX = -MathHelper.sin((float) yaw);
                double offsetZ = MathHelper.cos((float) yaw);

                ((ServerWorld) player.getEntityWorld()).spawnParticles(
                        ModParticles.REAPERS_SCYTHE_SWEEP_PARTICLES,
                        player.getX() + offsetX, player.getBodyY(0.5D), player.getZ() + offsetZ,
                        0, offsetX, 0.0, offsetZ, 0.0
                );

                player.getEntityWorld().playSound(
                        null,
                        player.getBlockPos(),
                        SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
                        player.getSoundCategory(),
                        1.0F,
                        1.0F
                );
            }
        }
    }

    @Override
    public DamageSource getDamageSource(LivingEntity attacker) {
        if (!(attacker.getEntityWorld() instanceof ServerWorld serverLevel)) {
            return super.getDamageSource(attacker);
        }

        return ModDamageTypes.reaperScytheAttack(serverLevel, attacker);
    }


    public static void addSoulToScythe(PlayerEntity player) {
        ItemStack stack =
                player.getMainHandStack().getItem() instanceof ReapersScytheItem ? player.getMainHandStack() : player.getOffHandStack();

        if (stack.getItem() instanceof ReapersScytheItem) {
            NbtCompound tag = getOrCreateCustomData(stack);

            int souls = tag.getInt("Souls").orElse(0);
            if (souls < 10) {
                tag.putInt("Souls", souls + 1);
                saveCustomData(stack, tag);
            }
        }
    }

    private boolean shouldDoSweep(PlayerEntity player) {
        if (!(player.getAttackCooldownProgress(0.5F) <= 0.9F)) return false;
        if (!player.isOnGround()) return false;
        if (player.isTouchingWater() || player.isClimbing()) return false;
        return true;
    }


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        NbtCompound tag = getOrCreateCustomData(stack);
        int souls = tag.getInt("Souls").orElse(0);

        textConsumer.accept(Text.literal("Souls: " + souls + "/10").formatted(Formatting.AQUA));
        textConsumer.accept(Text.literal("Nihil bonum a mortuis venit").formatted(Formatting.DARK_GRAY));

        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        NbtCompound tag = getOrCreateCustomData(stack);
        int souls = tag.getInt("Souls").orElse(0);
        return Math.round(13.0F * souls / 10.0F);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return SOUL_BAR_RGB;
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

    private static void saveCustomData(ItemStack stack, NbtCompound tag) {
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {

        boolean allowed = enchantment.matchesKey(ModEnchantments.LEECH) || enchantment.matchesKey(ModEnchantments.GRAVITY);

        boolean denied = enchantment.matchesKey(Enchantments.FORTUNE) || enchantment.matchesKey(Enchantments.SHARPNESS) ||
                enchantment.matchesKey(Enchantments.SMITE) || enchantment.matchesKey(Enchantments.BANE_OF_ARTHROPODS) ||
                enchantment.matchesKey(Enchantments.UNBREAKING) || enchantment.matchesKey(Enchantments.MENDING) ||
                enchantment.matchesKey(Enchantments.SWEEPING_EDGE) || enchantment.matchesKey(Enchantments.FIRE_ASPECT) ||
                enchantment.matchesKey(Enchantments.KNOCKBACK) || enchantment.matchesKey(Enchantments.VANISHING_CURSE) ||
                enchantment.matchesKey(Enchantments.LOOTING) ||
                enchantment.matchesKey(ModEnchantments.DASH) || enchantment.matchesKey(ModEnchantments.THUNDERING) ||
                enchantment.matchesKey(ModEnchantments.POISON_TIPPED) || enchantment.matchesKey(ModEnchantments.EXPERT)
                || enchantment.matchesKey(ModEnchantments.ILLAGER_BANE) || enchantment.matchesKey(ModEnchantments.RAGE);

        if (allowed) {
            return true;
        }
        if (denied) {
            return false;
        }

        return enchantment.value().isAcceptableItem(stack);
    }
}