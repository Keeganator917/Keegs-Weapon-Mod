package com.keeganator.keegsweapons.screen;

import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeScreenHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.ShapedRecipe;


public class WeaponForgeScreen extends AbstractContainerScreen<WeaponForgeScreenHandler> {

    public WeaponForgeScreen(WeaponForgeScreenHandler handler, Inventory inv, Component title) {
        super(handler, inv, title, 222, 246);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.fill(x + 12, y + 17, x + 16, y + 21, 0xFF00FF00);
        graphics.blit(RenderPipelines.GUI_TEXTURED, ModScreens.TEXTURE, x, y, 0, 0,
                this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }
}