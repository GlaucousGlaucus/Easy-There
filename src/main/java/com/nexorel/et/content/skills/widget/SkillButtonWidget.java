package com.nexorel.et.content.skills.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nexorel.et.content.skills.SkillScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.math.MathHelper;

public class SkillButtonWidget extends AbstractGui {

    private final SkillScreen.Skills icon;
    private boolean isSelected;
    private int X;
    private int Y;
    private int XX = 0;
    private int YY = 0;
    private int w = 234;
    private int h = 113;
    private SkillScreen skillScreen;

    public SkillButtonWidget(SkillScreen screen, SkillScreen.Skills icon, int x, int y) {
        this.icon = icon;
        this.skillScreen = screen;
        int relX = (screen.width - w) / 2;
        int relY = (screen.height - h) / 2;
        this.X = MathHelper.floor(x);
        this.Y = MathHelper.floor(y);
    }

    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().getTextureManager().bind(SkillScreen.SKILL_ASSETS_LOC);
        this.isSelected = mouseX >= this.X && mouseY >= this.Y && mouseX < this.X + 25 && mouseY < this.Y + 25;
        if (this.isSelected) {
            this.blit(matrixStack, (this.X), (this.Y), 25, 143, 25, 25);
        } else {
            this.blit(matrixStack, (this.X), (this.Y), 0, 143, 25, 25);
        }
        this.icon.drawIcon(Minecraft.getInstance().getItemRenderer(), this.X + 3, this.Y + 3);
    }

    public int getX() {
        return this.X;
    }

    public int getY() {
        return this.Y;
    }
}
