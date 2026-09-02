package com.keeganator.keegsweapons.network;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.StreamCodec;

public class MyModNetwork {
    private static boolean previousJumpState = false;
    private static boolean releasedJumpInAir = false;

    public static void register() {

        // C2S
        PayloadTypeRegistry.serverboundPlay().register(
                DashC2SPacket.PACKET_ID,
                DashC2SPacket.CODEC
        );
        PayloadTypeRegistry.clientboundPlay().register(
                DashC2SPacket.PACKET_ID,
                DashC2SPacket.CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(
                DashC2SPacket.PACKET_ID,
                (payload, context) -> {
                    DashHandler.tryDash(context.player());
                }
        );

        PayloadTypeRegistry.serverboundPlay().register(
                DoubleJumpPayload.ID,
                StreamCodec.unit(new DoubleJumpPayload())
        );
        PayloadTypeRegistry.clientboundPlay().register(
                DoubleJumpPayload.ID,
                StreamCodec.unit(new DoubleJumpPayload())
        );

        // S2C
        PayloadTypeRegistry.serverboundPlay().register(
                DashCooldownS2CPacket.PACKET_ID,
                DashCooldownS2CPacket.CODEC
        );
        PayloadTypeRegistry.clientboundPlay().register(
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
                        var stack = player.getMainHandItem();

                        if (!stack.isEmpty()) {
                            player.getCooldowns()
                                    .addCooldown(stack, payload.cooldown());
                        }
                    });
                }
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            boolean jumping = client.options.keyJump.isDown();
            boolean pressedThisTick = jumping && !previousJumpState;

            if (client.player.onGround()) {
                releasedJumpInAir = false;
            } else {
                if (!jumping) {
                    releasedJumpInAir = true;
                }
            }

            if (pressedThisTick && !client.player.onGround() && releasedJumpInAir) {
                ClientPlayNetworking.send(new DoubleJumpPayload());
            }

            previousJumpState = jumping;
        });
    }


    public static void sendDash() {
        ClientPlayNetworking.send(new DashC2SPacket());
    }
}