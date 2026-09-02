package com.keeganator.keegsweapons.item.custom;

import com.keeganator.keegsweapons.damagetypes.ModDamageTypes;
import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.entity.ReapersScytheAbility;
import com.keeganator.keegsweapons.item.ScytheItem;
import com.keeganator.keegsweapons.particles.ModParticles;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.awt.*;
import java.util.function.Consumer;

public class ReapersScytheItem extends ScytheItem {

    public static final Color SOUL_BAR_COLOR = new Color(47, 134, 194);
    public static final int SOUL_BAR_RGB = SOUL_BAR_COLOR.getRGB();

    public ReapersScytheItem(ToolMaterial material, int attackDamageModifier, float attackSpeedModifier, Properties settings) {
        super(material, attackDamageModifier, attackSpeedModifier, settings);
    }


    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        CompoundTag tag = getOrCreateCustomData(stack);

        if (!user.getCooldowns().isOnCooldown(this.getDefaultInstance())) {

            Vec3 eyePos = new Vec3(user.getX(), user.getEyeY(), user.getZ());
            Vec3 look = user.getViewVector(1.0F).normalize();

            if (world.isClientSide()) {
                int souls = tag.getInt("Souls").orElse(0);

                float scale = 1.0F + Math.min(souls, 10) / 10.0F;
                for (int i = 1; i < 170; i++) {
                    Vec3 pos = eyePos.add(look.scale(i));
                    world.addParticle(
                            ModParticles.REAPERS_SCYTHE_ABILITY_PARTICLES,
                            pos.x, pos.y, pos.z,
                            scale, 0, 0
                    );
                }

                tag.putInt("Souls", 0);
                saveCustomData(stack, tag);
            }

            if (!world.isClientSide()) {
                int souls = tag.getInt("Souls").orElse(0);

                ReapersScytheAbility ability = new ReapersScytheAbility(world, user, stack, souls);
                ability.setItem(Items.AIR.getDefaultInstance());

                ability.setPosRaw(user.getX(), user.getEyeY() - 0.1, user.getZ());
                ability.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.5F, 0.0F);

                world.addFreshEntity(ability);

                tag.putInt("Souls", 0);
                saveCustomData(stack, tag);
                if (!user.hasInfiniteMaterials()) {
                    user.getCooldowns().addCooldown(this.getDefaultInstance(), 600);
                } else if (user.hasInfiniteMaterials()) {
                    user.getCooldowns().addCooldown(this.getDefaultInstance(), 20);
                }


                float scale = 1.0F + Math.min(souls, 10) / 10.0F;
                for (int i = 1; i < 170; i++) {
                    Vec3 pos = eyePos.add(look.scale(i));
                    ((ServerLevel) world).sendParticles(
                            ModParticles.REAPERS_SCYTHE_ABILITY_PARTICLES,
                            pos.x, pos.y, pos.z, 1, scale, 0, 0, 0
                    );
                }
            }

            user.playSound(SoundEvents.WARDEN_SONIC_BOOM, 0.6F, 0.5F);
        }

        return InteractionResult.SUCCESS;
    }


    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (!attacker.level().isClientSide()) {
            CompoundTag tag = getOrCreateCustomData(stack);

            if (attacker instanceof Player player && shouldDoSweep(player)) {
                double yaw = Math.toRadians(player.getYRot());
                double offsetX = -Mth.sin((float) yaw);
                double offsetZ = Mth.cos((float) yaw);

                ((ServerLevel) player.level()).sendParticles(
                        ModParticles.REAPERS_SCYTHE_SWEEP_PARTICLES,
                        player.getX() + offsetX, player.getY(0.5D), player.getZ() + offsetZ,
                        0, offsetX, 0.0, offsetZ, 0.0
                );

                player.level().playSound(
                        null,
                        player.blockPosition(),
                        SoundEvents.PLAYER_ATTACK_SWEEP,
                        player.getSoundSource(),
                        1.0F,
                        1.0F
                );
            }
        }
    }

    @Override
    public DamageSource getItemDamageSource(LivingEntity attacker) {
        if (!(attacker.level() instanceof ServerLevel serverLevel)) {
            return super.getItemDamageSource(attacker);
        }

        return ModDamageTypes.reaperScytheAttack(serverLevel, attacker);
    }


    public static void addSoulToScythe(Player player) {
        ItemStack stack =
                player.getMainHandItem().getItem() instanceof ReapersScytheItem ? player.getMainHandItem() : player.getOffhandItem();

        if (stack.getItem() instanceof ReapersScytheItem) {
            CompoundTag tag = getOrCreateCustomData(stack);

            int souls = tag.getInt("Souls").orElse(0);
            if (souls < 10) {
                tag.putInt("Souls", souls + 1);
                saveCustomData(stack, tag);
            }
        }
    }

    private boolean shouldDoSweep(Player player) {
        if (!(player.getAttackStrengthScale(0.5F) <= 0.9F)) return false;
        if (!player.onGround()) return false;
        if (player.isInWater() || player.onClimbable()) return false;
        return true;
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        CompoundTag tag = getOrCreateCustomData(stack);
        int souls = tag.getInt("Souls").orElse(0);

        textConsumer.accept(Component.literal("Souls: " + souls + "/10").withStyle(ChatFormatting.AQUA));
        textConsumer.accept(Component.literal("Nihil bonum a mortuis venit").withStyle(ChatFormatting.DARK_GRAY));

        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        CompoundTag tag = getOrCreateCustomData(stack);
        int souls = tag.getInt("Souls").orElse(0);
        return Math.round(13.0F * souls / 10.0F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return SOUL_BAR_RGB;
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

    private static void saveCustomData(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {

        boolean allowed = enchantment.is(ModEnchantments.LEECH) || enchantment.is(ModEnchantments.GRAVITY);

        boolean denied = enchantment.is(Enchantments.FORTUNE) || enchantment.is(Enchantments.SHARPNESS) ||
                enchantment.is(Enchantments.SMITE) || enchantment.is(Enchantments.BANE_OF_ARTHROPODS) ||
                enchantment.is(Enchantments.UNBREAKING) || enchantment.is(Enchantments.MENDING) ||
                enchantment.is(Enchantments.SWEEPING_EDGE) || enchantment.is(Enchantments.FIRE_ASPECT) ||
                enchantment.is(Enchantments.KNOCKBACK) || enchantment.is(Enchantments.VANISHING_CURSE) ||
                enchantment.is(Enchantments.LOOTING) ||
                enchantment.is(ModEnchantments.DASH) || enchantment.is(ModEnchantments.THUNDERING) ||
                enchantment.is(ModEnchantments.POISON_TIPPED) || enchantment.is(ModEnchantments.EXPERT)
                || enchantment.is(ModEnchantments.ILLAGER_BANE) || enchantment.is(ModEnchantments.RAGE);

        if (allowed) {
            return true;
        }
        if (denied) {
            return false;
        }

        return enchantment.value().canEnchant(stack);
    }
}