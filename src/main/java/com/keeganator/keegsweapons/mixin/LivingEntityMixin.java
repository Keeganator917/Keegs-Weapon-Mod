package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.effects.ModEffects;
import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.entity.ReapersScytheAbility;
import com.keeganator.keegsweapons.item.ModItems;
import com.keeganator.keegsweapons.item.custom.ReapersScytheItem;
import com.keeganator.keegsweapons.util.DoubleJumpUser;
import com.keeganator.keegsweapons.util.ModTags;
import mezz.jei.api.constants.Tags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements DoubleJumpUser {

    //Leech
    @Inject(method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"))
    private void keegsweapons$leech(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() instanceof LivingEntity player) {
            LivingEntity target = (LivingEntity)(Object) this;
            RegistryEntry<Enchantment> poisonTipped =
                    player.getEntityWorld()
                            .getRegistryManager()
                            .getOrThrow(RegistryKeys.ENCHANTMENT)
                            .getOptional(ModEnchantments.LEECH)
                            .orElse(null);

            if (poisonTipped != null) {
                int level = EnchantmentHelper.getLevel(poisonTipped, player.getMainHandStack());

                if (level > 0) {
                    float damageAfterArmor = DamageUtil.getDamageLeft(target, amount, source, target.getArmor(),
                            (float)target.getAttributeValue(EntityAttributes.ARMOR_TOUGHNESS)) * 1.01f;
                    float healAmount = damageAfterArmor * 0.075f * level;
                    player.heal(healAmount);
                }
            }
        }
    }

    //Poison Tipped
    @Inject(method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("TAIL"))
    private void keegsweapons$applyPoisonTipped(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        Entity attacker = source.getAttacker();
        if (!(attacker instanceof LivingEntity player)) {
            return;
        }

        if (world.isClient()) {
            return;
        }

        RegistryEntry<Enchantment> poisonTipped =
                world.getRegistryManager()
                        .getOrThrow(RegistryKeys.ENCHANTMENT)
                        .getOptional(ModEnchantments.POISON_TIPPED)
                        .orElse(null);

        if (poisonTipped == null) {
            return;
        }

        int level = EnchantmentHelper.getLevel(poisonTipped, player.getMainHandStack());
        if (level > 0) {
            LivingEntity target = (LivingEntity) (Object) this;
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 80, level - 1), attacker);
        }
    }

    //Illager Bane
    @Unique
    boolean keegsweapons$alreadyApplied = false;

    @Inject(method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("TAIL"))
    private void keegsweapons$illagerBane(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() instanceof LivingEntity player) {
            RegistryEntry<Enchantment> poisonTipped =
                    player.getEntityWorld()
                            .getRegistryManager()
                            .getOrThrow(RegistryKeys.ENCHANTMENT)
                            .getOptional(ModEnchantments.ILLAGER_BANE)
                            .orElse(null);

            if (poisonTipped != null) {
                int level = EnchantmentHelper.getLevel(poisonTipped, player.getMainHandStack());

                if (level > 0) {
                    LivingEntity target = (LivingEntity) (Object) this;
                    if ((target.getEntity().getType().isIn(EntityTypeTags.ILLAGER))  && !keegsweapons$alreadyApplied) {
                        keegsweapons$alreadyApplied = true;
                        float extraDamage = amount * 0.5f * level;
                        System.out.println(extraDamage);
                        target.damage(world, source, extraDamage);
                    } else {
                        keegsweapons$alreadyApplied = false;
                    }
                }
            }
        }
    }

    //Rage
    @Unique
    private static final Identifier ATTACK_SPEED_BUFF_ID = Identifier.of("keegsweapons", "enchant_attack_speed_buff");

    @Inject(method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"))
    private void keegsweapons$rage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() instanceof LivingEntity player) {

            EntityAttributeInstance attackSpeed = player.getAttributeInstance(EntityAttributes.ATTACK_SPEED);
            if (attackSpeed == null) return;
            attackSpeed.removeModifier(ATTACK_SPEED_BUFF_ID);

            RegistryEntry<Enchantment> poisonTipped =
                    player.getEntityWorld()
                            .getRegistryManager()
                            .getOrThrow(RegistryKeys.ENCHANTMENT)
                            .getOptional(ModEnchantments.RAGE)
                            .orElse(null);

            if (poisonTipped != null) {
                int level = EnchantmentHelper.getLevel(poisonTipped, player.getMainHandStack());

                if (level > 0) {
                    float half_health = player.getMaxHealth() / 2;
                    if (player.getHealth() < half_health) {
                        EntityAttributeModifier modifier = new EntityAttributeModifier(ATTACK_SPEED_BUFF_ID, 0.1 * level, EntityAttributeModifier.Operation.ADD_VALUE);
                        attackSpeed.addTemporaryModifier(modifier);
                    }
                }
            }
        }
    }

    //Gravity
    @Inject(method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("TAIL"))
    private void keegsweapons$gravity(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        Entity attacker = source.getAttacker();
        if (!(attacker instanceof LivingEntity player)) {
            return;
        }

        if (world.isClient()) {
            return;
        }

        RegistryEntry<Enchantment> poisonTipped =
                world.getRegistryManager()
                        .getOrThrow(RegistryKeys.ENCHANTMENT)
                        .getOptional(ModEnchantments.GRAVITY)
                        .orElse(null);

        if (poisonTipped == null) {
            return;
        }

        int level = EnchantmentHelper.getLevel(poisonTipped, player.getMainHandStack());
        if (level > 0) {
            LivingEntity target = (LivingEntity) (Object) this;
            Vec3d pull = player.getEntityPos().subtract(target.getEntityPos());
            Vec3d direction = new Vec3d(pull.x, 0, pull.z).normalize();
            double strength = level * 0.80;
            target.addVelocity(direction.x * strength, direction.y * (strength * 1.5), direction.z * strength);
            target.velocityDirty = true;
        }
    }

    //Frostfall
    @Inject(method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("TAIL"))
    private void keegsweapons$frostfall(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        Entity attacker = source.getAttacker();
        if (!(attacker instanceof LivingEntity player)) {
            return;
        }

        if (world.isClient()) {
            return;
        }

        RegistryEntry<Enchantment> poisonTipped =
                world.getRegistryManager()
                        .getOrThrow(RegistryKeys.ENCHANTMENT)
                        .getOptional(ModEnchantments.FROSTFALL)
                        .orElse(null);

        if (poisonTipped == null) {
            return;
        }

        int level = EnchantmentHelper.getLevel(poisonTipped, player.getMainHandStack());
        LivingEntity target = (LivingEntity) (Object) this;
        if (level > 0 && level < 4) {
            target.addStatusEffect(new StatusEffectInstance(ModEffects.FREEZING, 60 * level, 0, false, false, true), attacker);
        }
        if (level >= 4) {
            target.addStatusEffect(new StatusEffectInstance(ModEffects.FREEZING, 60 * level, level / 4, false, false, true), attacker);
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

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void keegsweapons$resetDoubleJump(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;

        if (entity.isOnGround()) {
            ((DoubleJumpUser)entity).keegsweapons$setUsedDoubleJump(false);
        }
    }

    // Reapers Scythe
    @Inject(method = "onDeath", at = @At("TAIL"))
    private void keegsweapons$onDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity target = (LivingEntity) (Object) this;
        if (!(target.getEntityWorld() instanceof ServerWorld serverWorld)) return;

        Entity attacker = source.getAttacker();
        Entity directSource = source.getSource();
        boolean soulAdded = false;

        if (directSource instanceof ReapersScytheAbility ability) {
            serverWorld.spawnParticles(
                    ParticleTypes.SOUL, target.getX(), target.getY() + target.getHeight() / 2, target.getZ(),
                    4, 0.5, 0.5, 0.5, 0.02
            );

            if (ability.getOwner() instanceof PlayerEntity player) {
                ReapersScytheItem.addSoulToScythe(player);
                soulAdded = true;
            }
        }

        if (!soulAdded && attacker instanceof LivingEntity entity) {
            ItemStack weapon = entity.getMainHandStack();

            if (weapon.isOf(ModItems.REAPERS_SCYTHE)) {
                serverWorld.spawnParticles(
                        ParticleTypes.SOUL, target.getX(), target.getY() + target.getHeight() / 2, target.getZ(),
                        4, 0.5, 0.5, 0.5, 0.2
                );
                if (entity instanceof PlayerEntity player) {
                    ReapersScytheItem.addSoulToScythe(player);
                }
            }
        }
    }

    //Pathfinder
    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void pathfinderSpeed(CallbackInfo ci) {
        LivingEntity player = (LivingEntity) (Object) this;
        if (player.isOnGround()) {
            RegistryEntry<Enchantment> poisonTipped =
                    player.getEntityWorld()
                            .getRegistryManager()
                            .getOrThrow(RegistryKeys.ENCHANTMENT)
                            .getOptional(ModEnchantments.PATHFINDER)
                            .orElse(null);

            if (poisonTipped != null) {
                int level = EnchantmentHelper.getLevel(poisonTipped, player.getEquippedStack(EquipmentSlot.FEET));

                //BlockPos pos = player.getSteppingPos(); //Use this when updating mod
                BlockPos pos = player.getBlockPos();
                BlockPos pos2 = player.getBlockPos().down();
                if (player.getEntityWorld().getBlockState(pos).isIn(ModTags.Blocks.PATH_BLOCKS) || player.getEntityWorld().getBlockState(pos2).isIn(ModTags.Blocks.PATH_BLOCKS)) {
                    if (level > 0) {
                        player.setVelocity(player.getVelocity().multiply(1.0 + 0.09 * level, 1.0, 1.0 + 0.09 * level));
                    }
                }
            }
        }
    }

    // Projectile block for Shoguns Katana
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void keegsweapons$katanaDeflect(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if ((self instanceof PlayerEntity player)) {
            ItemStack stack = player.getMainHandStack();
            if (stack.isOf(ModItems.SHOGUNS_KATANA)) {
                Entity sourceEntity = source.getSource();
                if ((sourceEntity instanceof ProjectileEntity projectile)) {
                    if (!isBackstab((LivingEntity) projectile.getOwner(), self)) {
                        if (projectile instanceof PersistentProjectileEntity arrow && arrow.getPierceLevel() > 0) {
                            return;
                        }
                        if (player.getRandom().nextFloat() < 0.30f) {
                            cir.setReturnValue(false);

                            Vec3d look = player.getRotationVector();

                            projectile.setPosition(player.getX() + look.x * 1.5, player.getBodyY(0.5), player.getZ() + look.z * 1.5);
                            projectile.setVelocity(look.x * 1.8, 0.15, look.z * 1.8);
                            projectile.velocityDirty = true;
                            projectile.setOwner(player);

                            player.timeUntilRegen = 5;

                            if (player.getEntityWorld().isClient()) {
                                world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_SHIELD_BLOCK.value(),
                                        player.getSoundCategory(), 1.0f, 1.2f);
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
            Vec3d targetLook = new Vec3d(target.getRotationVector().x, 0, target.getRotationVector().z).normalize();
            Vec3d attackerVec = new Vec3d(attacker.getX() - target.getX(), 0, attacker.getZ() - target.getZ()).normalize();
            return targetLook.dotProduct(attackerVec) < -0.2;
        } else {
            return false;
        }
    }


}