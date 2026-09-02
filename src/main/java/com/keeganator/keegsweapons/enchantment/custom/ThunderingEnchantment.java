package com.keeganator.keegsweapons.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ThunderingEnchantment implements EnchantmentEntityEffect {
    public static final MapCodec<ThunderingEnchantment> CODEC = MapCodec.unit(ThunderingEnchantment::new);

    Random rand = new Random();

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        int strikeChance = rand.nextInt(20);
        BlockPos position = BlockPos.ofFloored(pos);
        if(level == 1) {
            if (strikeChance == 1) {
                summonNoFireLightning(world, position);
            }
        }

        if(level == 2) {
            if (strikeChance == 1 || strikeChance == 2) {
                summonNoFireLightning(world, position);
            }
        }
        if(level > 5 && level <= 15) {
            if (strikeChance == 1 || strikeChance == 2 || strikeChance == 3 || strikeChance == 4 || strikeChance == 5) {
                summonNoFireLightning(world, position);
            }
        }
        if(level > 15 && level <= 30) {
            if (strikeChance == 1 || strikeChance == 2 || strikeChance == 3 || strikeChance == 4 || strikeChance == 5) {
                summonNoFireLightning(world, position);
                summonNoFireLightning(world, position);
            }
        }
        if(level > 30) {
            if (strikeChance == 1 || strikeChance == 2 || strikeChance == 3 || strikeChance == 4 || strikeChance == 5) {
                summonNoFireLightning(world, position);
                summonNoFireLightning(world, position);
                summonNoFireLightning(world, position);
                summonNoFireLightning(world, position);
            }
        }
    }

    public static void summonNoFireLightning(ServerWorld world, BlockPos pos) {
        LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world, SpawnReason.TRIGGERED);
        if (lightning == null) return;

        lightning.refreshPositionAfterTeleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        lightning.addCommandTag("keegsweapons:no_fire");
        world.spawnEntity(lightning);
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}