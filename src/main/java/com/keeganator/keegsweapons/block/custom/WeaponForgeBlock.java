package com.keeganator.keegsweapons.block.custom;

import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeScreenHandler;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponType;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class WeaponForgeBlock extends RedstoneLampBlock {
    public static final MapCodec<RedstoneLampBlock> LAMP_CODEC = simpleCodec(RedstoneLampBlock::new);
    private static final Component TITLE = Component.translatable("container.weapon_forge");
    private final WeaponType weaponType;

    public MapCodec<RedstoneLampBlock> codec() {
        return LAMP_CODEC;
    }

    public WeaponForgeBlock(WeaponType weaponType, BlockBehaviour.Properties settings) {
        super(settings);
        this.weaponType = weaponType;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide() && state.getValue(LIT)) {
            player.openMenu(state.getMenuProvider(world, pos));
            world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 0.04f, 0.7f);
            world.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 0.3f, 0.4f);
            //Maybe make custom stat in the future?
            //player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);

        } else if (!world.isClientSide() && !state.getValue(LIT)) {
            int chance = RandomSource.create().nextInt(0, 3);
            if (chance == 0) {
                player.sendOverlayMessage(Component.translatable("block.keegsweapons.unpowered_forge1"));
            } else if (chance == 1) {
                player.sendOverlayMessage(Component.translatable("block.keegsweapons.unpowered_forge2"));
            } else if (chance == 2) {
            player.sendOverlayMessage(Component.translatable("block.keegsweapons.unpowered_forge3"));
            } else {
                player.sendOverlayMessage(Component.translatable("block.keegsweapons.unpowered_forge1"));
            }
        }
        return InteractionResult.SUCCESS;
    }


    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
        return new SimpleMenuProvider(
                (syncId, inventory, player) ->
                        new WeaponForgeScreenHandler(syncId, inventory, ContainerLevelAccess.create(world, pos), weaponType),
                Component.translatable("container.weapon_forge." + weaponType.id())
        );
    }


    public WeaponType getWeaponType() {
        return weaponType;
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
        boolean powered = world.hasNeighborSignal(pos);

        if (powered != state.getValue(LIT)) {
            BlockState newState = state.setValue(LIT, powered);
            world.setBlock(pos, newState, Block.UPDATE_ALL);

            // Trigger burst when turning on
            if (powered && world instanceof ServerLevel serverWorld) {
                spawnActivationParticles(serverWorld, pos);

                world.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 2.0f, 0.4f);
                world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 0.1f, 0.7f);
                world.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 0.1f, 0.5f);
            }
        }
    }

    private void spawnActivationParticles(ServerLevel world, BlockPos pos) {
        var random = world.getRandom();
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

            world.sendParticles(ParticleTypes.FLAME, x1, y, z1, 1, 0, 0.01, 0, 0.01);
            world.sendParticles(ParticleTypes.SMOKE, x1, y, z1, 1, 0, 0.01, 0, 0.01);
            world.sendParticles(ParticleTypes.FLAME, x2, y, z2, 1, 0, 0.01, 0, 0.01);
            world.sendParticles(ParticleTypes.SMOKE, x2, y, z2, 1, 0, 0.01, 0, 0.01);
        }
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) return;

        if (random.nextFloat() < 0.15f) {
            double radius = 4.0;

            double x = pos.getX() + 0.5 + (random.nextDouble() * 2 * radius - radius);
            double y = pos.getY() + 0.5 + (random.nextDouble() * 2 * radius - radius);
            double z = pos.getZ() + 0.5 + (random.nextDouble() * 2 * radius - radius);

            double motionX = (random.nextDouble() - 0.5) * 0.01;
            double motionY = 0.01;
            double motionZ = (random.nextDouble() - 0.5) * 0.01;

            world.addParticle(ParticleTypes.FLAME, x, y, z, motionX, motionY, motionZ);
            if (random.nextBoolean()) {
                world.addParticle(ParticleTypes.SMOKE, x, y, z, motionX, motionY, motionZ);
            }

            world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 0.2f, 0.7f);
            world.playSound(null, pos, SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.4f, 0.7f);
        }
    }
}
