package com.nexorel.et.content.items.talisBag;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import static com.nexorel.et.Reference.MOD_ID;

public class TalismanBagScreen extends ContainerScreen<TalismanBagContainer> {

    private ResourceLocation GUI = new ResourceLocation(MOD_ID, "textures/gui/talisman_bag.png");

    public TalismanBagScreen(TalismanBagContainer container, PlayerInventory playerInventory, ITextComponent component) {
        super(container, playerInventory, component);
        this.imageHeight = 222;
        this.imageWidth = 176;
        this.inventoryLabelY = 128 /*222-94*/;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }
}
