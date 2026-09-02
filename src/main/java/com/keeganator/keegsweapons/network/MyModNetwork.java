package com.keeganator.keegsweapons.network;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.Vec3d;

public class MyModNetwork {
    private static boolean previousJumpState = false;
    private static boolean releasedJumpInAir = false;

    public static void register() {

        // C2S
        PayloadTypeRegistry.playC2S().register(
                DashC2SPacket.PACKET_ID,
                DashC2SPacket.CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(
                DashC2SPacket.PACKET_ID,
                (payload, context) -> {
                    DashHandler.tryDash(context.player());
                }
        );

        PayloadTypeRegistry.playC2S().register(
                DoubleJumpPayload.ID,
                PacketCodec.unit(new DoubleJumpPayload())
        );

        // S2C
        PayloadTypeRegistry.playS2C().register(
                DashCooldownS2CPacket.PACKET_ID,
                DashCooldownS2CPacket.CODEC
        );

        //Double Jump Networking
        ServerPlayNetworking.registerGlobalReceiver(
                DoubleJumpPayload.ID,
                (payload, context) -> {
                    DoubleJumpHandler.tryDoubleJump(context.player());
                }
        );

    }

    public static void registerClient() {

        ClientPlayNetworking.registerGlobalReceiver(
                DashCooldownS2CPacket.PACKET_ID,
                (payload, context) -> {
                    context.client().execute(() -> {
                        if (context.client().player == null) return;

                        var player = context.client().player;
                        var stack = player.getMainHandStack();

                        if (!stack.isEmpty()) {
                            player.getItemCooldownManager()
                                    .set(stack, payload.cooldown());
                        }
                    });
                }
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            boolean jumping = client.options.jumpKey.isPressed();
            boolean pressedThisTick = jumping && !previousJumpState;

            if (client.player.isOnGround()) {
                releasedJumpInAir = false;
            } else {
                if (!jumping) {
                    releasedJumpInAir = true;
                }
            }

            if (pressedThisTick && !client.player.isOnGround() && releasedJumpInAir) {
                ClientPlayNetworking.send(new DoubleJumpPayload());
            }

            previousJumpState = jumping;
        });
    }


    public static void sendDash() {
        ClientPlayNetworking.send(new DashC2SPacket());
    }
}