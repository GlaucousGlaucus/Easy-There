package com.nexorel.et.content.blocks.GemRefinery;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.nexorel.et.Reference.MODID;

public class GRS extends AbstractContainerScreen<GRC> {

    private final ResourceLocation GUI = new ResourceLocation(MODID, "textures/gui/gr.png");

    public GRS(GRC container, Inventory playerInventory, Component name) {
        super(container, playerInventory, name);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        final GemRefineryTile tile = menu.tileEntity;
        int l = this.menu.getSmeltProgressionScaled();
        this.blit(matrixStack, relX + 73, relY + 31, 176, 14, l + 1, 16);
    }
}
