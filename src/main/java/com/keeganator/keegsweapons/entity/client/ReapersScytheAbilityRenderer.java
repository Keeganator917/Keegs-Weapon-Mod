package com.keeganator.keegsweapons.entity.client;

import com.keeganator.keegsweapons.entity.ReapersScytheAbility;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.state.FlyingItemEntityRenderState;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.item.ItemDisplayContext;

public class ReapersScytheAbilityRenderer extends FlyingItemEntityRenderer<ReapersScytheAbility> {

    public ReapersScytheAbilityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void updateRenderState(ReapersScytheAbility entity, FlyingItemEntityRenderState flyingItemEntityRenderState, float f) {
        super.updateRenderState(entity, flyingItemEntityRenderState, f);
    }

}