package com.keeganator.keegsweapons.entity;

import com.keeganator.keegsweapons.damagetypes.ModDamageTypes;
import com.keeganator.keegsweapons.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReapersScytheAbility extends ThrownItemEntity {

    private static final TrackedData<ItemStack> ITEM_STACK =
            DataTracker.registerData(ReapersScytheAbility.class, TrackedDataHandlerRegistry.ITEM_STACK);

    private int life;
    private float damage;

    @Nullable
    private LivingEntity owner;

    private static final float BASE_DAMAGE = 5.0F;
    private static final float EXPONENT = 1.156F;
    private static final float SCALE = 1.315F;

    private float totalDamage;

    private Vec3d origin = Vec3d.ZERO;
    private Vec3d direction = Vec3d.ZERO;
    private boolean initialized = false;
    private double distanceTravelled;

    public ReapersScytheAbility(EntityType<? extends ReapersScytheAbility> type, World world) {
        super(type, world);
        this.setNoGravity(true);
        this.noClip = true;
    }

    public ReapersScytheAbility(World world, LivingEntity owner, ItemStack stack, float damage) {
        super(ModEntities.REAPERS_SCYTHE_ABILITY, world);
        this.setOwner(owner);
        this.owner = owner;
        this.damage = damage;

        if (!stack.isEmpty()) {
            this.setItem(stack);
        }

        this.totalDamage = (float) (BASE_DAMAGE * Math.pow(this.damage + 1, EXPONENT) / SCALE);

        this.setNoGravity(true);
        this.noClip = true;

        this.origin = owner.getEyePos();
        this.direction = owner.getRotationVec(1.0F).normalize();
        this.initialized = true;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ITEM_STACK, ItemStack.EMPTY);
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

        Vec3d pos = origin.add(direction.multiply(distanceTravelled));
        this.setPosition(pos);

        // Fix rotation to face direction of travel
        if (this.getPitch() == 0.0F && this.getYaw() == 0.0F) {
            Vec3d vec3d = this.calculateVelocity(this.getX(), this.getY(), this.getZ(), 1, 0.0f);
            this.setVelocity(vec3d);
            this.velocityDirty = true;
            double d = vec3d.horizontalLength();
            this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
            this.setPitch((float)(MathHelper.atan2(vec3d.y, d) * 57.2957763671875));
            this.lastYaw = this.getYaw();
            this.lastPitch = this.getPitch();
        }

        if (!this.getEntityWorld().isClient()) {
            Box hitBox = this.getBoundingBox().expand(0.7D, 0.35D, 0.7D);

            List<LivingEntity> targets = this.getEntityWorld().getEntitiesByClass(
                    LivingEntity.class,
                    hitBox,
                    entity -> entity != this.owner && entity.isAlive()
            );

            for (LivingEntity target : targets) {
                target.damage(
                        (ServerWorld) this.getEntityWorld(), ModDamageTypes.reaperScytheAbility((ServerWorld) this.getEntityWorld(), this, this.getOwner()), totalDamage
                );
            }

            if (++life > 120) {
                this.discard();
            }
        }
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    public boolean isInsideWall() {
        return false;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            super.onCollision(hitResult);
        }
    }

    @Override
    public boolean canHit() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void writeCustomData(WriteView view) {
        view.putInt("Life", life);
    }

    @Override
    protected void readCustomData(ReadView view) {
        life = view.getInt("Life", 1);
    }

}