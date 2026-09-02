package com.keeganator.keegsweapons.block.custom;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeScreenHandler;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponType;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.client.sound.Sound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jspecify.annotations.Nullable;

public class WeaponForgeBlock extends RedstoneLampBlock {
    public static final MapCodec<RedstoneLampBlock> LAMP_CODEC = createCodec(RedstoneLampBlock::new);
    private static final Text TITLE = Text.translatable("container.weapon_forge");
    private final WeaponType weaponType;

    public MapCodec<RedstoneLampBlock> getCodec() {
        return LAMP_CODEC;
    }

    public WeaponForgeBlock(WeaponType weaponType, AbstractBlock.Settings settings) {
        super(settings);
        this.weaponType = weaponType;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient() && state.get(LIT)) {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.BLOCKS, 0.04f, 0.7f);
            world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 0.3f, 0.4f);
            //Maybe make custom stat in the future?
            //player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);

        } else if (!world.isClient() && !state.get(LIT)) {
            int chance = Random.create().nextBetweenExclusive(0, 3);
            if (chance == 0) {
                player.sendMessage(Text.translatable("block.keegsweapons.unpowered_forge1"), true);
            } else if (chance == 1) {
                player.sendMessage(Text.translatable("block.keegsweapons.unpowered_forge2"), true);
            } else if (chance == 2) {
            player.sendMessage(Text.translatable("block.keegsweapons.unpowered_forge3"), true);
            } else {
                player.sendMessage(Text.translatable("block.keegsweapons.unpowered_forge1"), true);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory(
                (syncId, inventory, player) ->
                        new WeaponForgeScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos), weaponType),
                Text.translatable("container.weapon_forge." + weaponType.id())
        );
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        boolean powered = world.isReceivingRedstonePower(pos);

        if (powered != state.get(LIT)) {
            BlockState newState = state.with(LIT, powered);
            world.setBlockState(pos, newState, Block.NOTIFY_ALL);

            // Trigger burst when turning on
            if (powered && world instanceof ServerWorld serverWorld) {
                spawnActivationParticles(serverWorld, pos);

                world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 2.0f, 0.4f);
                world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.BLOCKS, 0.1f, 0.7f);
                world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 0.1f, 0.5f);
            }
        }
    }

    private void spawnActivationParticles(ServerWorld world, BlockPos pos) {
        var random = world.random;
        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.8;
        double centerZ = pos.getZ() + 0.5;
        int particles = 60;

        for (int i = 0; i < particles; i++) {
            double angle = i * 0.35;
            double radius = 0.3 + (i * 0.03);

            double x1 = centerX + Math.cos(angle) * radius;
            double z1 = centerZ + Math.sin(angle) * radius;

            double x2 = centerX + Math.cos(-angle) * radius;
            double z2 = centerZ + Math.sin(-angle) * radius;

            double y = centerY + random.nextDouble() * 1.1;

            //Skips some particles
            if (random.nextFloat() < 0.6f) continue;

            world.spawnParticles(ParticleTypes.FLAME, x1, y, z1, 1, 0, 0.01, 0, 0.01);
            world.spawnParticles(ParticleTypes.SMOKE, x1, y, z1, 1, 0, 0.01, 0, 0.01);
            world.spawnParticles(ParticleTypes.FLAME, x2, y, z2, 1, 0, 0.01, 0, 0.01);
            world.spawnParticles(ParticleTypes.SMOKE, x2, y, z2, 1, 0, 0.01, 0, 0.01);
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(LIT)) return;

        if (random.nextFloat() < 0.15f) {
            double radius = 4.0;

            double x = pos.getX() + 0.5 + (random.nextDouble() * 2 * radius - radius);
            double y = pos.getY() + 0.5 + (random.nextDouble() * 2 * radius - radius);
            double z = pos.getZ() + 0.5 + (random.nextDouble() * 2 * radius - radius);

            double motionX = (random.nextDouble() - 0.5) * 0.01;
            double motionY = 0.01;
            double motionZ = (random.nextDouble() - 0.5) * 0.01;

            world.addParticleClient(ParticleTypes.FLAME, x, y, z, motionX, motionY, motionZ);
            if (random.nextBoolean()) {
                world.addParticleClient(ParticleTypes.SMOKE, x, y, z, motionX, motionY, motionZ);
            }

            world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.BLOCKS, 0.2f, 0.7f);
            world.playSound(null, pos, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.4f, 0.7f);
        }
    }
}
