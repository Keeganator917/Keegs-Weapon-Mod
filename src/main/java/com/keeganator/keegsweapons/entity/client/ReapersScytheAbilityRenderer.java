package com.keeganator.keegsweapons.entity.client;

import com.keeganator.keegsweapons.entity.ReapersScytheAbility;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;

public class ReapersScytheAbilityRenderer extends ThrownItemRenderer<ReapersScytheAbility> {

    public ReapersScytheAbilityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void extractRenderState(ReapersScytheAbility entity, final ThrownItemRenderState state, final float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
    }
}