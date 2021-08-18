package com.nexorel.et.content.skills;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.List;

import static com.nexorel.et.Reference.MOD_ID;

public class SkillTabGUI extends AbstractGui {

    public double scrollX;
    public double scrollY;
    private int minX = Integer.MAX_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int maxY = Integer.MIN_VALUE;
    private boolean centered;
    public final List<SkillScreen.SkillButtonWidget> widgets = Lists.newArrayList();

    public SkillTabGUI() {

    }

    public void drawContents(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (!this.centered) {
            this.scrollX = (double) (117 - (this.maxX + this.minX) / 2);
            this.scrollY = (double) (56 - (this.maxY + this.minY) / 2);
            this.centered = true;
        }

        matrixStack.pushPose();
        RenderSystem.enableDepthTest();
        matrixStack.translate(0.0F, 0.0F, 950.0F);
        RenderSystem.colorMask(false, false, false, false);
        fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        matrixStack.translate(0.0F, 0.0F, -950.0F);
        RenderSystem.depthFunc(518);
        fill(matrixStack, 234, 113, 0, 0, -16777216);
        RenderSystem.depthFunc(515);
        ResourceLocation resourcelocation = new ResourceLocation(MOD_ID, "textures/gui/skill_bg.png");
        Minecraft.getInstance().getTextureManager().bind(resourcelocation);

        int i = MathHelper.floor(this.scrollX);
        int j = MathHelper.floor(this.scrollY);
        int k = i % 16;
        int l = j % 16;

        for (int i1 = -1; i1 <= 15; ++i1) {
            for (int j1 = -1; j1 <= 8; ++j1) {
                blit(matrixStack, k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }
        RenderSystem.depthFunc(518);
        matrixStack.translate(0.0F, 0.0F, -950.0F);
        RenderSystem.colorMask(false, false, false, false);
        fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        matrixStack.translate(0.0F, 0.0F, 950.0F);
        RenderSystem.depthFunc(515);
        matrixStack.popPose();
    }

    public void scroll(double x, double y) {
        if (this.maxX - this.minX > 234) {
            this.scrollX = MathHelper.clamp(this.scrollX + x, (double) (-(this.maxX - 234)), 0.0D);
        }

        if (this.maxY - this.minY > 113) {
            this.scrollY = MathHelper.clamp(this.scrollY + y, (double) (-(this.maxY - 113)), 0.0D);
        }

    }

    public void addWidget(SkillScreen.SkillButtonWidget widget) {
        this.widgets.add(widget);
        int i = widget.getX();
        int j = i + 28;
        int k = widget.getY();
        int l = k + 27;
        this.minX = Math.min(this.minX, i);
        this.maxX = Math.max(this.maxX, j);
        this.minY = Math.min(this.minY, k);
        this.maxY = Math.max(this.maxY, l);
    }
}
