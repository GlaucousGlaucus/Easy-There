package com.nexorel.et.content.blocks.GemRefinery;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import static com.nexorel.et.Reference.MOD_ID;

public class GRS extends ContainerScreen<GRC> {

    private ResourceLocation GUI = new ResourceLocation(MOD_ID, "textures/gui/gr.png");

    public GRS(GRC container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    /**
     @Override
     protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
     super.renderLabels(p_230451_1_, p_230451_2_, p_230451_3_);
     }*/

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        final GemRefineryTile tile = menu.tileEntity;
        int l = this.menu.getSmeltProgressionScaled();
        this.blit(matrixStack, relX + 73, relY + 31, 176, 14, l + 1, 16);
    }
}
