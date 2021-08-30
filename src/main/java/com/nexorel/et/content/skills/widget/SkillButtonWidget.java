package com.nexorel.et.content.skills.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nexorel.et.capabilities.CombatSkill;
import com.nexorel.et.capabilities.CombatSkillCapability;
import com.nexorel.et.content.skills.SkillScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class SkillButtonWidget extends AbstractGui {

    private final SkillScreen.Skills icon;
    private int X;
    private int Y;
    private SkillScreen skillScreen;
    private CombatSkill combatSkill = Minecraft.getInstance().player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
    public boolean isSelected;
    private long animationTime = -1L;

    public SkillButtonWidget(SkillScreen skillScreen, SkillScreen.Skills icon, int x, int y) {
        this.icon = icon;
        this.X = MathHelper.floor(x);
        this.Y = MathHelper.floor(y);
        this.skillScreen = skillScreen;
    }

    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, long Time) {
        Minecraft.getInstance().getTextureManager().bind(SkillScreen.SKILL_ASSETS_LOC);
        int relX = (skillScreen.width - 252) / 2;
        int relY = (skillScreen.height - 139) / 2;
        this.isSelected = mouseX >= this.X && mouseY >= this.Y && mouseX < this.X + 25 && mouseY < this.Y + 25 && mouseX > relX + 11 && mouseX < relX + 129 && mouseY > relY + 20 && mouseY < relY + 250;
        if (!isSelected) {
            this.blit(matrixStack, (this.X), (this.Y), 0, 143, 25, 25);
            this.animationTime = -1L;
        }
        this.icon.drawIcon(Minecraft.getInstance().getItemRenderer(), this.X + 3, this.Y + 3);
    }

    private float getVisibility(long Time) {
        float f = MathHelper.clamp((float) (Time - this.animationTime) / 300, 0.0F, 1.0F);
        f = f * f;
        return f;
    }

    public void animateAndRender(MatrixStack matrixStack, long TimeE) {
        long Time = Util.getMillis();

        Minecraft.getInstance().getTextureManager().bind(SkillScreen.SKILL_ASSETS_LOC);

        if (this.animationTime == -1L) {
            this.animationTime = Time;
        }

        float a = 15 * getVisibility(Time);
        float b = 0F;
        float c = 0F;

        matrixStack.pushPose();
        RenderSystem.enableBlend();
        this.blit(matrixStack, (this.X), (this.Y), 25, 143, 25, 25);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.3F + MathHelper.clamp((getVisibility(Time) * 2), 0F, 0.7F));
        matrixStack.translate(a, b, c);
        renderHoverGUI(matrixStack);
        matrixStack.translate(-a, -b, -c);
        RenderSystem.disableBlend();
        matrixStack.popPose();
        this.icon.drawIcon(Minecraft.getInstance().getItemRenderer(), this.X + 3, this.Y + 3);
    }

    private int getLevelColor(int level) {
        if (level == 0) {
            return 0xff0000;
        } else if (level <= 5) {
            return 0xffa600;
        } else if (level <= 10) {
            return 0xc3ff00;
        } else if (level <= 15) {
            return 0x6aff00;
        } else if (level <= 20) {
            return 0x00ff3c;
        }
        return 0;
    }

    private int getXp_percent_color(double xp_percent) {
        if (xp_percent <= 25) {
            return 0xff1500;
        } else if (xp_percent <= 50) {
            return 0xff9100;
        } else if (xp_percent <= 75) {
            return 0xfffb00;
        } else if (xp_percent <= 100) {
            return 0x77ff00;
        }
        return 0;
    }

    public void renderHoverGUI(MatrixStack matrixStack) {
        // 4, 147
        this.blit(matrixStack, (this.X) + 25, (this.Y), 0, 197, 25, 25); // TL
        this.blit(matrixStack, (this.X) + 25, (this.Y) + (3 * 25), 25, 197, 25, 25); // BL
        this.blit(matrixStack, (this.X) + 50 + (3 * 25), (this.Y), 0, 222, 25, 25); // TR
        this.blit(matrixStack, (this.X) + 50 + (3 * 25), (this.Y) + (3 * 25), 25, 222, 25, 25); // BR

        this.blit(matrixStack, (this.X) + 25, (this.Y) + (25), 26, 171, 25, 25);
        this.blit(matrixStack, (this.X) + 25, (this.Y) + (2 * 25), 26, 171, 25, 25);
        this.blit(matrixStack, (this.X) + 25 + (25), (this.Y) + (3 * 25), 77, 197, 25, 25);
        this.blit(matrixStack, (this.X) + 25 + (2 * 25), (this.Y) + (3 * 25), 77, 197, 25, 25);
        this.blit(matrixStack, (this.X) + 50 + (2 * 25), (this.Y) + (3 * 25), 77, 197, 25, 25);

        this.blit(matrixStack, (this.X) + 50 + (3 * 25), (this.Y) + (25), 52, 171, 25, 25);
        this.blit(matrixStack, (this.X) + 50 + (3 * 25), (this.Y) + (2 * 25), 52, 171, 25, 25);
        this.blit(matrixStack, (this.X) + 50 + (25), (this.Y), 77, 171, 25, 25);
        this.blit(matrixStack, (this.X) + 25 + (25), (this.Y), 77, 171, 25, 25);
        this.blit(matrixStack, (this.X) + 50 + (2 * 25), (this.Y), 77, 171, 25, 25);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                this.blit(matrixStack, (this.X) + 50 + (i * 25), (this.Y) + (j * 25) + 25, 51, 208, 25, 25);
            }
        }

        if (this.icon.getName().equals("skill.combat") && Minecraft.getInstance().player != null) {
            Minecraft.getInstance().font.draw(matrixStack, "\u2694 Combat \u2694", this.X + 58, this.Y + 5, 0x0fff7); //-5592406
            Minecraft.getInstance().font.draw(matrixStack, "Level:", this.X + 68, this.Y + 20, 0x0fff7); //-5592406
            Minecraft.getInstance().font.draw(matrixStack, Integer.toString(CombatSkill.calculateLvlFromXp(this.combatSkill.getXp())), this.X + 98, this.Y + 20, getLevelColor(CombatSkill.calculateLvlFromXp(this.combatSkill.getXp())));

            double xp_percent = CombatSkill.getXPProgress(this.combatSkill);

            Minecraft.getInstance().font.draw(matrixStack, xp_percent == -1 ? "" : "Progress To Level " + (CombatSkill.calculateLvlFromXp(this.combatSkill.getXp()) + 1), this.X + 35, this.Y + 35, 0x0fff7); //-5592406
            if (xp_percent != -1)
                Minecraft.getInstance().font.draw(matrixStack, xp_percent + " %", this.X + 50, this.Y + 53, getXp_percent_color(xp_percent)); //-5592406

            StringBuilder bars = CombatSkill.getProgressBars(xp_percent);
            if (xp_percent != -1)
                Minecraft.getInstance().font.draw(matrixStack, "------------------", this.X + 35, this.Y + 45, 0);
            Minecraft.getInstance().font.draw(matrixStack, bars.toString(), this.X + 35, this.Y + 45, xp_percent != -1 ? getXp_percent_color(xp_percent) : 0x00ff11); //-5592406
            Minecraft.getInstance().font.draw(matrixStack, "XP: ", this.X + 35, this.Y + 65, 0x0fff7); //-5592406
            Minecraft.getInstance().font.draw(matrixStack, Double.toString((double) Math.round(combatSkill.getXp() * 100) / 100), this.X + 55, this.Y + 65, 0x00f2ff); //-5592406
            Minecraft.getInstance().font.draw(matrixStack, "Damage Multiplier: ", this.X + 35, this.Y + 80, 0x0fff7); //-5592406
            Minecraft.getInstance().font.draw(matrixStack, Double.toString((double) Math.round(0.015 * (combatSkill.getLevel() ^ 2) * 100) / 100), this.X + 35 + Minecraft.getInstance().font.width("Damage Multiplier: "), this.Y + 80, 0x00f2ff); //-5592406
        }
    }

    public int getX() {
        return this.X;
    }

    public int getY() {
        return this.Y;
    }

    public void setX(int x) {
        this.X = x;
    }

    public void setY(int y) {
        this.Y = y;
    }
}
