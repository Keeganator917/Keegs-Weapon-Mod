package com.keeganator.keegsweapons.block.custom;

import com.keeganator.keegsweapons.effects.ModEffects;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityTypes;
import org.jspecify.annotations.Nullable;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.AABB;

public class AntiAirBeaconBlock extends Block {
    public static final MapCodec<AntiAirBeaconBlock> CODEC = simpleCodec(AntiAirBeaconBlock::new);
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    private static final double RADIUS = 50.0;
    private static final double RADIUS_SQ = RADIUS * RADIUS;
    private static final int PULSE_INTERVAL_TICKS = 20;

    public AntiAirBeaconBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(LIT, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
    }


    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
        boolean powered = world.hasNeighborSignal(pos);
        boolean lit = state.getValue(LIT);

        if (world.isClientSide()) {
            if (powered && !lit) {
                world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 0.6f, 1.8f);
                world.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0f, 1.6f);
            } else if (!powered && lit) {
                world.playSound(null, pos, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1.0f, 1.6f);
            }
            return;
        }

        if (powered && !lit) {
            world.setBlock(pos, state.setValue(LIT, true), Block.UPDATE_CLIENTS);
            dealDamage((ServerLevel) world, pos);
            world.scheduleTick(pos, this, PULSE_INTERVAL_TICKS);
        } else if (!powered && lit) {
            world.setBlock(pos, state.setValue(LIT, false), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT) || !world.hasNeighborSignal(pos)) {
            return;
        }

        dealDamage(world, pos);
        world.scheduleTick(pos, this, PULSE_INTERVAL_TICKS);
    }

    private void dealDamage(ServerLevel world, BlockPos pos) {
        AABB box = new AABB(pos).inflate(RADIUS);
        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.5;
        double centerZ = pos.getZ() + 0.5;

        List<Phantom> phantoms = world.getEntities(EntityTypes.PHANTOM, box,
                phantom -> phantom.distanceToSqr(centerX, centerY, centerZ) <= RADIUS_SQ);
        for (Phantom phantom : phantoms) {
            phantom.addEffect(new MobEffectInstance(ModEffects.GROUNDED, 100, 0, false, false));
        }

        List<Player> players = world.getEntities(EntityTypes.PLAYER, box,
                player -> player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA) && player.distanceToSqr(centerX, centerY, centerZ) <= RADIUS_SQ);
        for (Player player : players) {
            player.addEffect(new MobEffectInstance(ModEffects.GROUNDED, 100, 0, false, false));
            if (player.isFallFlying()) {
                player.sendOverlayMessage(Component.translatable("block.keegsweapons.anti_air_beacon.warning").withStyle(ChatFormatting.RED));
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) return;

        if (random.nextFloat() < 0.75f) {
            double radius = 4.0;

            double x = pos.getX() + 0.5 + (random.nextDouble() * 2 * radius - radius);
            double y = pos.getY() + 0.5 + (random.nextDouble() * 2 * radius - radius);
            double z = pos.getZ() + 0.5 + (random.nextDouble() * 2 * radius - radius);

            double motionX = (random.nextDouble() - 0.5) * 0.01;
            double motionY = 0.01;
            double motionZ = (random.nextDouble() - 0.5) * 0.01;

            world.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, motionX, motionY, motionZ);
            world.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, motionX, motionY, motionZ);

            world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 0.2f, 1.2f);
            world.playSound(null, pos, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 0.6f, 1.8f);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}