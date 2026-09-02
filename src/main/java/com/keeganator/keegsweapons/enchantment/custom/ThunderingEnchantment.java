package com.keeganator.keegsweapons.enchantment.custom;

import com.mojang.serialization.MapCodec;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public class ThunderingEnchantment implements EnchantmentEntityEffect {
    public static final MapCodec<ThunderingEnchantment> CODEC = MapCodec.unit(ThunderingEnchantment::new);

    Random rand = new Random();

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity user, Vec3 pos) {
        int strikeChance = rand.nextInt(20);
        BlockPos position = BlockPos.containing(pos);
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

    public static void summonNoFireLightning(ServerLevel world, BlockPos pos) {
        LightningBolt lightning = EntityTypes.LIGHTNING_BOLT.create(world, EntitySpawnReason.TRIGGERED);
        if (lightning == null) return;

        lightning.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        lightning.addTag("keegsweapons:no_fire");
        world.addFreshEntity(lightning);
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}