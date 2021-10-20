package com.nexorel.et.content.skills;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nexorel.et.content.skills.widget.SkillButtonWidget;
import com.nexorel.et.content.skills.widget.StatsWidget;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.List;

import static com.nexorel.et.Reference.MODID;

public class SkillTabGUI extends GuiComponent {

    public double scrollX;
    public double scrollY;
    private final int a = 260;//234
    private final int b = 160;
    private final int w = 234;
    private final int h = 113;
    private boolean centered;
    public final List<SkillButtonWidget> widgets = Lists.newArrayList();
    public final StatsWidget statsWidget;
    private final SkillScreen skillScreen;
    private float fade;

    public SkillTabGUI(SkillScreen skillScreen) {
        this.skillScreen = skillScreen;
        this.statsWidget = new StatsWidget(this.skillScreen, (this.skillScreen.width - 252) / 2 + 120, ((this.skillScreen.height - 139) / 2) + 50);
    }

    public void init() {
        int i = 0;
        int j = 0;
        for (int m = 0; m < SkillScreen.Skills.VALUES.length; m++) {
            SkillScreen.Skills skill = SkillScreen.Skills.VALUES[m];
            addWidget(new SkillButtonWidget(this.skillScreen, skill, (this.skillScreen.width - 252) / 2 + i + 75, ((this.skillScreen.height - 139) / 2) + (j + (m * 35) + 30)));
        }
    }

    public void drawContents(PoseStack matrixStack, int mouseX, int mouseY) {
        if (!this.centered) {
            this.scrollX = (double) (w / 2) - (double) (a) / 2;
            this.scrollY = (double) (h / 2) - (double) (b) / 2;
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
        ResourceLocation resourcelocation = new ResourceLocation(MODID, "textures/gui/skill_bg.png");
        RenderSystem.setShaderTexture(0, resourcelocation);


        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
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

        int X = (this.skillScreen.width - 252) / 2 + i + 120;
        int Y = ((this.skillScreen.height - 139) / 2) + j + (50);

        this.widgets.forEach((widget) -> {
            int m = this.widgets.indexOf(widget);
            widget.setX((this.skillScreen.width - 252) / 2 + i + 75);
            widget.setY(((this.skillScreen.height - 139) / 2) + (j + (m * 35) + 30));
            widget.renderButton(matrixStack, mouseX, mouseY);
        });
        this.statsWidget.setX(X);
        this.statsWidget.setY(Y);
        this.statsWidget.renderButton(matrixStack, mouseX, mouseY);

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

    public void drawTooltips(PoseStack matrixStack, int RelX, int RelY) {
        matrixStack.pushPose();
        matrixStack.translate(0.0F, 0.0F, 200.0F);
        fill(matrixStack, 0, 0, 234, 113, Mth.floor(this.fade * 255.0F) << 24);
        matrixStack.translate(-(float) (RelX + 9), -(float) (RelY + 18), 0.0F);
        boolean flag = false;
        for (SkillButtonWidget widget : this.widgets) {
            if (widget.isSelected) {
                flag = true;
                widget.animateAndRender(matrixStack);
                break;
            }
        }
        if (this.statsWidget.isSelected) {
            flag = true;
            this.statsWidget.animateAndRender(matrixStack);
        }

        matrixStack.popPose();
        if (flag) {
            this.fade = Mth.clamp(this.fade + 0.009F, 0.0F, 0.45F);
        } else {
            this.fade = Mth.clamp(this.fade - 0.04F, 0.0F, 1.0F);
        }
    }

    public void scroll(double x, double y) {
        this.scrollX = Mth.clamp(this.scrollX + x, -(a - w), 0.0D);
        this.scrollY = Mth.clamp(this.scrollY + y, -(b - h), 0.0D);

    }

    public void addWidget(SkillButtonWidget widget) {
        this.widgets.add(widget);
    }
}
