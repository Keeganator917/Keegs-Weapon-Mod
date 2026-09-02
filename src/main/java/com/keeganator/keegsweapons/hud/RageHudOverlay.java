package com.keeganator.keegsweapons.hud;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class RageHudOverlay {
    private static final Identifier ELEMENT_ID = Identifier.of("keegsweapons", "rage_crosshair");

    private static final Identifier CROSSHAIR_TEXTURE =
            Identifier.of("keegsweapons", "textures/gui/hud/rage_crosshair.png");

    private static final Identifier INDICATOR_BG_TEXTURE =
            Identifier.of("keegsweapons", "textures/gui/hud/rage_crosshair_attack_indicator_background.png");

    private static final Identifier INDICATOR_PROGRESS_TEXTURE =
            Identifier.of("keegsweapons", "textures/gui/hud/rage_crosshair_attack_indicator_progress.png");

    private static final Identifier INDICATOR_FULL_TEXTURE =
            Identifier.of("keegsweapons", "textures/gui/hud/rage_crosshair_attack_indicator_full.png");

    public static void register() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, ELEMENT_ID,
                (graphics, tickCounter) -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    ClientPlayerEntity player = client.player;
                    if (player == null || client.options.hudHidden) return;
                    if (client.options.getPerspective() != Perspective.FIRST_PERSON) return;
                    if (!isRageActive()) return;

                    int screenWidth = client.getWindow().getScaledWidth();
                    int screenHeight = client.getWindow().getScaledHeight();

                    int crosshairX = (screenWidth / 2) - 8;
                    int crosshairY = (screenHeight / 2) - 7;
                    graphics.drawTexture(RenderPipelines.GUI_TEXTURED, CROSSHAIR_TEXTURE,
                            crosshairX, crosshairY, 0, 0, 15, 15, 15, 15);

                    int barX = (screenWidth / 2) - 8;
                    int barY = (screenHeight / 2) + 9;
                    float cooldown = player.getAttackCooldownProgress(0.0F);
                    boolean lookingAtAttackableEntity = client.targetedEntity instanceof LivingEntity;

                    if (lookingAtAttackableEntity && cooldown >= 1.0F) {
                        graphics.drawTexture(RenderPipelines.GUI_TEXTURED, INDICATOR_FULL_TEXTURE,
                                barX, barY, 0, 0, 16, 4, 16, 16);
                    } else if (cooldown < 1.0F) {
                        graphics.drawTexture(RenderPipelines.GUI_TEXTURED, INDICATOR_BG_TEXTURE,
                                barX, barY, 0, 0, 16, 4, 16, 4);

                        int filledWidth = (int) (cooldown * 16.0F);
                        graphics.drawTexture(RenderPipelines.GUI_TEXTURED, INDICATOR_PROGRESS_TEXTURE,
                                barX, barY, 0, 0, filledWidth, 4, 16, 4);
                    }
                });
    }

    private static boolean isRageActive() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return false;

        RegistryEntry<Enchantment> rage = player.getEntityWorld().getRegistryManager()
                .getOrThrow(RegistryKeys.ENCHANTMENT)
                .getOptional(ModEnchantments.RAGE)
                .orElse(null);
        if (rage == null) return false;

        int level = EnchantmentHelper.getLevel(rage, player.getMainHandStack());
        return level > 0 && player.getHealth() < player.getMaxHealth() / 2;
    }
}
