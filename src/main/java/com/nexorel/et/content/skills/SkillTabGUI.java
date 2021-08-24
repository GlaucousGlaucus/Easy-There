package com.nexorel.et.content.skills;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nexorel.et.content.skills.widget.SkillButtonWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.List;

import static com.nexorel.et.Reference.MOD_ID;

public class SkillTabGUI extends AbstractGui {

    public double scrollX;
    public double scrollY;
    private int a = 260;//234
    private int b = 160;
    private int w = 234;
    private int h = 113;
    private boolean centered;
    public final List<SkillButtonWidget> widgets = Lists.newArrayList();
    private SkillScreen skillScreen;

    public SkillTabGUI(SkillScreen skillScreen) {
        this.skillScreen = skillScreen;
    }

    public void drawContents(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (!this.centered) {
            this.scrollX = (double) ((w / 2) - (a) / 2);
            this.scrollY = (double) ((h / 2) - (b) / 2);
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

        int x = (this.skillScreen.width - 252) / 2;
        int y = (this.skillScreen.height - 139) / 2;

        matrixStack.translate(-(float) (x + 9), -(float) (y + 18), 0.0F);

        this.widgets.clear();
        for (int m = 0; m < SkillScreen.Skills.VALUES.length; m++) {
            SkillScreen.Skills skill = SkillScreen.Skills.VALUES[m];
            addWidget(new SkillButtonWidget(this.skillScreen, skill, (this.skillScreen.width - 252) / 2 + i + 75, ((this.skillScreen.height - 139) / 2) + (j + (m * 35) + 30)));
        }
        this.widgets.forEach((widget) -> widget.renderButton(matrixStack, mouseX, mouseY));

        matrixStack.translate(-(float) (x + 9), -(float) (y + 18), 0.0F);

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
        this.scrollX = MathHelper.clamp(this.scrollX + x, (double) (-(a - w)), 0.0D);
        this.scrollY = MathHelper.clamp(this.scrollY + y, (double) (-(b - h)), 0.0D);

    }

    public void addWidget(SkillButtonWidget widget) {
        this.widgets.add(widget);
    }
}
