package com.keeganator.keegsweapons.mixin.enchantments;

import com.keeganator.keegsweapons.effects.ModEffects;
import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.entity.ReapersScytheAbility;
import com.keeganator.keegsweapons.item.ModItems;
import com.keeganator.keegsweapons.item.custom.ReapersScytheItem;
import com.keeganator.keegsweapons.util.DoubleJumpUser;
import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements DoubleJumpUser {

    //Leech
    @Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"))
    private void keegsweapons$leech(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getEntity() instanceof LivingEntity player) {
            LivingEntity target = (LivingEntity)(Object) this;
            Holder<Enchantment> poisonTipped =
                    player.level()
                            .registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .get(ModEnchantments.LEECH)
                            .orElse(null);

            if (poisonTipped != null) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, player.getMainHandItem());

                if (level > 0) {
                    float damageAfterArmor = CombatRules.getDamageAfterAbsorb(target, amount, source, target.getArmorValue(),
                            (float)target.getAttributeValue(Attributes.ARMOR_TOUGHNESS)) * 1.01f;
                    float healAmount = damageAfterArmor * 0.075f * level;
                    player.heal(healAmount);
                }
            }
        }
    }

    //Poison Tipped
    @Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("TAIL"))
    private void keegsweapons$applyPoisonTipped(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        Entity attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity player)) {
            return;
        }

        if (world.isClientSide()) {
            return;
        }

        Holder<Enchantment> poisonTipped =
                world.registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(ModEnchantments.POISON_TIPPED)
                        .orElse(null);

        if (poisonTipped == null) {
            return;
        }

        int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, player.getMainHandItem());
        if (level > 0) {
            LivingEntity target = (LivingEntity) (Object) this;
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 80, level - 1), attacker);
        }
    }

    //Illager Bane
    @Unique
    boolean keegsweapons$alreadyApplied = false;

    @Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("TAIL"))
    private void keegsweapons$illagerBane(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getEntity() instanceof LivingEntity player) {
            Holder<Enchantment> poisonTipped =
                    player.level()
                            .registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .get(ModEnchantments.ILLAGER_BANE)
                            .orElse(null);

            if (poisonTipped != null) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, player.getMainHandItem());

                if (level > 0) {
                    LivingEntity target = (LivingEntity) (Object) this;
                    if ((target.asLivingEntity().is(EntityTypeTags.ILLAGER))  && !keegsweapons$alreadyApplied) {
                        keegsweapons$alreadyApplied = true;
                        float extraDamage = amount * 0.5f * level;
                        System.out.println(extraDamage);
                        target.hurtServer(world, source, extraDamage);
                    } else {
                        keegsweapons$alreadyApplied = false;
                    }
                }
            }
        }
    }

    //Rage
    @Unique
    private static final Identifier ATTACK_SPEED_BUFF_ID = Identifier.fromNamespaceAndPath("keegsweapons", "enchant_attack_speed_buff");

    @Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"))
    private void keegsweapons$rage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getEntity() instanceof LivingEntity player) {

            AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
            if (attackSpeed == null) return;
            attackSpeed.removeModifier(ATTACK_SPEED_BUFF_ID);

            Holder<Enchantment> poisonTipped =
                    player.level()
                            .registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .get(ModEnchantments.RAGE)
                            .orElse(null);

            if (poisonTipped != null) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, player.getMainHandItem());

                if (level > 0) {
                    float half_health = player.getMaxHealth() / 2;
                    if (player.getHealth() < half_health) {
                        AttributeModifier modifier = new AttributeModifier(ATTACK_SPEED_BUFF_ID, 0.1 * level, AttributeModifier.Operation.ADD_VALUE);
                        attackSpeed.addTransientModifier(modifier);
                    }
                }
            }
        }
    }

    //Gravity
    @Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("TAIL"))
    private void keegsweapons$gravity(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        Entity attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity player)) {
            return;
        }

        if (world.isClientSide()) {
            return;
        }

        Holder<Enchantment> poisonTipped =
                world.registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(ModEnchantments.GRAVITY)
                        .orElse(null);

        if (poisonTipped == null) {
            return;
        }

        int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, player.getMainHandItem());
        if (level > 0) {
            LivingEntity target = (LivingEntity) (Object) this;
            Vec3 pull = player.position().subtract(target.position());
            Vec3 direction = new Vec3(pull.x, 0, pull.z).normalize();
            double strength = level * 0.80;
            target.push(direction.x * strength, direction.y * (strength * 1.5), direction.z * strength);
            target.needsSync = true;
        }
    }

    //Frostfall
    @Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("TAIL"))
    private void keegsweapons$frostfall(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        Entity attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity player)) {
            return;
        }

        if (world.isClientSide()) {
            return;
        }

        Holder<Enchantment> poisonTipped =
                world.registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(ModEnchantments.FROSTFALL)
                        .orElse(null);

        if (poisonTipped == null) {
            return;
        }

        int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, player.getMainHandItem());
        LivingEntity target = (LivingEntity) (Object) this;
        if (level > 0 && level < 4) {
            target.addEffect(new MobEffectInstance(ModEffects.FREEZING, 60 * level, 0, false, false, true), attacker);
        }
        if (level >= 4) {
            target.addEffect(new MobEffectInstance(ModEffects.FREEZING, 60 * level, level / 4, false, false, true), attacker);
        }
    }

    //Double Jump
    @Unique
    private boolean atlanweaponry$usedDoubleJump;

    @Override
    public boolean keegsweapons$usedDoubleJump() {
        return atlanweaponry$usedDoubleJump;
    }

    @Override
    public void keegsweapons$setUsedDoubleJump(boolean used) {
        atlanweaponry$usedDoubleJump = used;
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void keegsweapons$resetDoubleJump(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;

        if (entity.onGround()) {
            ((DoubleJumpUser)entity).keegsweapons$setUsedDoubleJump(false);
        }
    }

    // Reapers Scythe
    @Inject(method = "die", at = @At("TAIL"))
    private void keegsweapons$onDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity target = (LivingEntity) (Object) this;
        if (!(target.level() instanceof ServerLevel serverWorld)) return;

        Entity attacker = source.getEntity();
        Entity directSource = source.getDirectEntity();
        boolean soulAdded = false;

        if (directSource instanceof ReapersScytheAbility ability) {
            serverWorld.sendParticles(
                    ParticleTypes.SOUL, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(),
                    4, 0.5, 0.5, 0.5, 0.02
            );

            if (ability.getOwner() instanceof Player player) {
                ReapersScytheItem.addSoulToScythe(player);
                soulAdded = true;
            }
        }

        if (!soulAdded && attacker instanceof LivingEntity entity) {
            ItemStack weapon = entity.getMainHandItem();

            if (weapon.is(ModItems.REAPERS_SCYTHE)) {
                serverWorld.sendParticles(
                        ParticleTypes.SOUL, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(),
                        4, 0.5, 0.5, 0.5, 0.2
                );
                if (entity instanceof Player player) {
                    ReapersScytheItem.addSoulToScythe(player);
                }
            }
        }
    }

    //Pathfinder
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void pathfinderSpeed(CallbackInfo ci) {
        LivingEntity player = (LivingEntity) (Object) this;
        if (player.onGround()) {
            Holder<Enchantment> poisonTipped =
                    player.level()
                            .registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .get(ModEnchantments.PATHFINDER)
                            .orElse(null);

            if (poisonTipped != null) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, player.getItemBySlot(EquipmentSlot.FEET));

                //BlockPos pos = player.getSteppingPos(); //Use this when updating mod
                BlockPos pos = player.blockPosition();
                BlockPos pos2 = player.blockPosition().below();
                if (player.level().getBlockState(pos).is(ModTags.Blocks.PATH_BLOCKS) || player.level().getBlockState(pos2).is(ModTags.Blocks.PATH_BLOCKS)) {
                    if (level > 0) {
                        player.setDeltaMovement(player.getDeltaMovement().multiply(1.0 + 0.09 * level, 1.0, 1.0 + 0.09 * level));
                    }
                }
            }
        }
    }

    // Projectile block for Shoguns Katana
    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void keegsweapons$katanaDeflect(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if ((self instanceof Player player)) {
            ItemStack stack = player.getMainHandItem();
            if (stack.is(ModItems.SHOGUNS_KATANA)) {
                Entity sourceEntity = source.getDirectEntity();
                if ((sourceEntity instanceof Projectile projectile)) {
                    if (!isBackstab((LivingEntity) projectile.getOwner(), self)) {
                        if (projectile instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0) {
                            return;
                        }
                        if (player.getRandom().nextFloat() < 0.30f) {
                            cir.setReturnValue(false);

                            Vec3 look = player.getLookAngle();

                            projectile.setPos(player.getX() + look.x * 1.5, player.getY(0.5), player.getZ() + look.z * 1.5);
                            projectile.setDeltaMovement(look.x * 1.8, 0.15, look.z * 1.8);
                            projectile.needsSync = true;
                            projectile.setOwner(player);

                            player.invulnerableTime = 5;

                            if (player.level().isClientSide()) {
                                world.playSound(null, player.blockPosition(), SoundEvents.SHIELD_BLOCK.value(),
                                        player.getSoundSource(), 1.0f, 1.2f);
                            }
                        }
                    }
                }
            }
        }
    }
    @Unique
    private boolean isBackstab(LivingEntity attacker, LivingEntity target) {
        if (attacker != null) {
            Vec3 targetLook = new Vec3(target.getLookAngle().x, 0, target.getLookAngle().z).normalize();
            Vec3 attackerVec = new Vec3(attacker.getX() - target.getX(), 0, attacker.getZ() - target.getZ()).normalize();
            return targetLook.dot(attackerVec) < -0.2;
        } else {
            return false;
        }
    }


}