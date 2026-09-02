package com.keeganator.keegsweapons.block.custom;

import com.keeganator.keegsweapons.effects.ModEffects;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class AntiAirBeaconBlock extends Block {
    public static final MapCodec<AntiAirBeaconBlock> CODEC = createCodec(AntiAirBeaconBlock::new);
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    private static final double RADIUS = 50.0;
    private static final double RADIUS_SQ = RADIUS * RADIUS;
    private static final int PULSE_INTERVAL_TICKS = 20;

    public AntiAirBeaconBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(LIT, false));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(LIT, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }


    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        boolean powered = world.isReceivingRedstonePower(pos);
        boolean lit = state.get(LIT);

        if (world.isClient()) {
            if (powered && !lit) {
                world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.BLOCKS, 0.6f, 1.8f);
                world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.6f);
            } else if (!powered && lit) {
                world.playSound(null, pos, SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.6f);
            }
            return;
        }

        if (powered && !lit) {
            world.setBlockState(pos, state.with(LIT, true), Block.NOTIFY_LISTENERS);
            dealDamage((ServerWorld) world, pos);
            world.scheduleBlockTick(pos, this, PULSE_INTERVAL_TICKS);
        } else if (!powered && lit) {
            world.setBlockState(pos, state.with(LIT, false), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.get(LIT) || !world.isReceivingRedstonePower(pos)) {
            return;
        }

        dealDamage(world, pos);
        world.scheduleBlockTick(pos, this, PULSE_INTERVAL_TICKS);
    }

    private void dealDamage(ServerWorld world, BlockPos pos) {
        Box box = new Box(pos).expand(RADIUS);
        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.5;
        double centerZ = pos.getZ() + 0.5;

        List<PhantomEntity> phantoms = world.getEntitiesByType(EntityType.PHANTOM, box,
                phantom -> phantom.squaredDistanceTo(centerX, centerY, centerZ) <= RADIUS_SQ);
        for (PhantomEntity phantom : phantoms) {
            phantom.addStatusEffect(new StatusEffectInstance(ModEffects.GROUNDED, 100, 0, false, false));
        }

        List<PlayerEntity> players = world.getEntitiesByType(EntityType.PLAYER, box,
                player -> player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA) && player.squaredDistanceTo(centerX, centerY, centerZ) <= RADIUS_SQ);
        for (PlayerEntity player : players) {
            player.addStatusEffect(new StatusEffectInstance(ModEffects.GROUNDED, 100, 0, false, false));
            if (player.isGliding()) {
                player.sendMessage(Text.translatable("block.keegsweapons.anti_air_beacon.warning").formatted(Formatting.RED), true);
            }
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(LIT)) return;

        if (random.nextFloat() < 0.75f) {
            double radius = 4.0;

            double x = pos.getX() + 0.5 + (random.nextDouble() * 2 * radius - radius);
            double y = pos.getY() + 0.5 + (random.nextDouble() * 2 * radius - radius);
            double z = pos.getZ() + 0.5 + (random.nextDouble() * 2 * radius - radius);

            double motionX = (random.nextDouble() - 0.5) * 0.01;
            double motionY = 0.01;
            double motionZ = (random.nextDouble() - 0.5) * 0.01;

            world.addParticleClient(ParticleTypes.ELECTRIC_SPARK, x, y, z, motionX, motionY, motionZ);
            world.addParticleClient(ParticleTypes.ELECTRIC_SPARK, x, y, z, motionX, motionY, motionZ);

            world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.BLOCKS, 0.2f, 1.2f);
            world.playSound(null, pos, SoundEvents.BLOCK_BEACON_AMBIENT, SoundCategory.BLOCKS, 0.6f, 1.8f);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}