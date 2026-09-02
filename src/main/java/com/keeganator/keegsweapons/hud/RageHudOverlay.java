package com.keeganator.keegsweapons.hud;

import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class RageHudOverlay {
    private static final Identifier ELEMENT_ID = Identifier.fromNamespaceAndPath("keegsweapons", "rage_crosshair");

    private static final Identifier CROSSHAIR_TEXTURE =
            Identifier.fromNamespaceAndPath("keegsweapons", "textures/gui/hud/rage_crosshair.png");

    private static final Identifier INDICATOR_BG_TEXTURE =
            Identifier.fromNamespaceAndPath("keegsweapons", "textures/gui/hud/rage_crosshair_attack_indicator_background.png");

    private static final Identifier INDICATOR_PROGRESS_TEXTURE =
            Identifier.fromNamespaceAndPath("keegsweapons", "textures/gui/hud/rage_crosshair_attack_indicator_progress.png");

    private static final Identifier INDICATOR_FULL_TEXTURE =
            Identifier.fromNamespaceAndPath("keegsweapons", "textures/gui/hud/rage_crosshair_attack_indicator_full.png");

    public static void register() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, ELEMENT_ID,
                (graphics, tickCounter) -> {
                    Minecraft client = Minecraft.getInstance();
                    LocalPlayer player = client.player;
                    if (player == null || client.options.hideGui) return;
                    if (client.options.getCameraType() != CameraType.FIRST_PERSON) return;
                    if (!isRageActive()) return;

                    int screenWidth = client.getWindow().getGuiScaledWidth();
                    int screenHeight = client.getWindow().getGuiScaledHeight();

                    int crosshairX = (screenWidth / 2) - 8;
                    int crosshairY = (screenHeight / 2) - 7;
                    graphics.blit(RenderPipelines.GUI_TEXTURED, CROSSHAIR_TEXTURE,
                            crosshairX, crosshairY, 0, 0, 15, 15, 15, 15);

                    int barX = (screenWidth / 2) - 8;
                    int barY = (screenHeight / 2) + 9;
                    float cooldown = player.getAttackStrengthScale(0.0F);
                    boolean lookingAtAttackableEntity = client.crosshairPickEntity instanceof LivingEntity;

                    if (lookingAtAttackableEntity && cooldown >= 1.0F) {
                        graphics.blit(RenderPipelines.GUI_TEXTURED, INDICATOR_FULL_TEXTURE,
                                barX, barY, 0, 0, 16, 4, 16, 16);
                    } else if (cooldown < 1.0F) {
                        graphics.blit(RenderPipelines.GUI_TEXTURED, INDICATOR_BG_TEXTURE,
                                barX, barY, 0, 0, 16, 4, 16, 4);

                        int filledWidth = (int) (cooldown * 16.0F);
                        graphics.blit(RenderPipelines.GUI_TEXTURED, INDICATOR_PROGRESS_TEXTURE,
                                barX, barY, 0, 0, filledWidth, 4, 16, 4);
                    }
                });
    }

    private static boolean isRageActive() {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        if (player == null) return false;

        Holder<Enchantment> rage = player.level().registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(ModEnchantments.RAGE)
                .orElse(null);
        if (rage == null) return false;

        int level = EnchantmentHelper.getItemEnchantmentLevel(rage, player.getMainHandItem());
        return level > 0 && player.getHealth() < player.getMaxHealth() / 2;
    }
}
