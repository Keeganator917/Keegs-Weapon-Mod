package com.keeganator.keegsweapons.entity;

import com.keeganator.keegsweapons.damagetypes.ModDamageTypes;
import com.keeganator.keegsweapons.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ReapersScytheAbility extends ThrowableItemProjectile {

    private static final EntityDataAccessor<ItemStack> ITEM_STACK =
            SynchedEntityData.defineId(ReapersScytheAbility.class, EntityDataSerializers.ITEM_STACK);

    private int life;
    private float damage;

    @Nullable
    private LivingEntity owner;

    private static final float BASE_DAMAGE = 5.0F;
    private static final float EXPONENT = 1.156F;
    private static final float SCALE = 1.315F;

    private float totalDamage;

    private Vec3 origin = Vec3.ZERO;
    private Vec3 direction = Vec3.ZERO;
    private boolean initialized = false;
    private double distanceTravelled;

    public ReapersScytheAbility(EntityType<? extends ReapersScytheAbility> type, Level world) {
        super(type, world);
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    public ReapersScytheAbility(Level world, LivingEntity owner, ItemStack stack, float damage) {
        super(ModEntities.REAPERS_SCYTHE_ABILITY, world);
        this.setOwner(owner);
        this.owner = owner;
        this.damage = damage;

        if (!stack.isEmpty()) {
            this.setItem(stack);
        }

        this.totalDamage = (float) (BASE_DAMAGE * Math.pow(this.damage + 1, EXPONENT) / SCALE);

        this.setNoGravity(true);
        this.noPhysics = true;

        this.origin = owner.getEyePosition();
        this.direction = owner.getViewVector(1.0F).normalize();
        this.initialized = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ITEM_STACK, ItemStack.EMPTY);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.REAPERS_SCYTHE;
    }

    @Override
    public void tick() {
        if (!initialized) {
            return;
        }
        distanceTravelled += 2.5;

        Vec3 pos = origin.add(direction.scale(distanceTravelled));
        this.setPos(pos);

        // Fixes rotation to face direction of travel
        if (this.getXRot() == 0.0F && this.getYRot() == 0.0F) {
            Vec3 vec3d = this.getMovementToShoot(this.getX(), this.getY(), this.getZ(), 1, 0.0f);
            this.setDeltaMovement(vec3d);
            this.needsSync = true;
            double d = vec3d.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
            this.setXRot((float)(Mth.atan2(vec3d.y, d) * 57.2957763671875));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        if (!this.level().isClientSide()) {
            AABB hitBox = this.getBoundingBox().inflate(0.7D, 0.35D, 0.7D);

            List<LivingEntity> targets = this.level().getEntitiesOfClass(
                    LivingEntity.class,
                    hitBox,
                    entity -> entity != this.owner && entity.isAlive()
            );

            for (LivingEntity target : targets) {
                target.hurtServer(
                        (ServerLevel) this.level(), ModDamageTypes.reaperScytheAbility((ServerLevel) this.level(), this, this.getOwner()), totalDamage
                );
            }

            if (++life > 120) {
                this.discard();
            }
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            super.onHit(hitResult);
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {
        view.putInt("Life", life);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {
        life = view.getIntOr("Life", 1);
    }



}