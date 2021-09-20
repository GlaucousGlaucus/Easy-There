package com.nexorel.et.content.items.talisBag;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.nexorel.et.Reference.MODID;

public class TalismanBagScreen extends AbstractContainerScreen<TalismanBagContainer> {

    private final ResourceLocation GUI = new ResourceLocation(MODID, "textures/gui/talisman_bag.png");

    public TalismanBagScreen(TalismanBagContainer container, Inventory playerInventory, Component component) {
        super(container, playerInventory, component);
        this.imageHeight = 222;
        this.imageWidth = 176;
        this.inventoryLabelY = 128 /*222-94*/;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }
}
