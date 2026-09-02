package com.keeganator.keegsweapons.entity.client;

import com.keeganator.keegsweapons.entity.ModEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class ModEntityRenderers {

    public static void register() {
        EntityRendererRegistry.register(
                ModEntities.REAPERS_SCYTHE_ABILITY,
                ReapersScytheAbilityRenderer::new
        );
    }
}