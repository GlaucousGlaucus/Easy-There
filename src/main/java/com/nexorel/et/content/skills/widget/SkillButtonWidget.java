package com.nexorel.et.content.skills.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nexorel.et.capabilities.CombatSkill;
import com.nexorel.et.capabilities.CombatSkillCapability;
import com.nexorel.et.content.skills.SkillScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.math.MathHelper;

public class SkillButtonWidget extends AbstractGui {

    private final SkillScreen.Skills icon;
    private int X;
    private int Y;
    private SkillScreen skillScreen;
    private CombatSkill combatSkill = Minecraft.getInstance().player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);

    public SkillButtonWidget(SkillScreen skillScreen, SkillScreen.Skills icon, int x, int y) {
        this.icon = icon;
        this.X = MathHelper.floor(x);
        this.Y = MathHelper.floor(y);
        this.skillScreen = skillScreen;
    }

    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY) {
        Minecraft.getInstance().getTextureManager().bind(SkillScreen.SKILL_ASSETS_LOC);
        int relX = (skillScreen.width - 252) / 2;
        int relY = (skillScreen.height - 139) / 2;
        boolean isSelected = mouseX >= this.X && mouseY >= this.Y && mouseX < this.X + 25 && mouseY < this.Y + 25 && mouseX > relX + 11 && mouseX < relX + 129 && mouseY > relY + 20 && mouseY < relY + 250;
        if (isSelected) {
            this.renderHoverGUI(matrixStack);
        } else {
            this.blit(matrixStack, (this.X), (this.Y), 0, 143, 25, 25);
        }
        this.icon.drawIcon(Minecraft.getInstance().getItemRenderer(), this.X + 3, this.Y + 3);
    }

    private void renderHoverGUI(MatrixStack matrixStack) {
        this.blit(matrixStack, (this.X), (this.Y), 25, 143, 25, 25);
        String DCtion = "Test";
        int len = DCtion.length();
        // 4, 147
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.blit(matrixStack, (this.X) + 25 + (i * 25), (this.Y) + (j * 25), 0, 171, 25, 25);
            }
        }
        Minecraft.getInstance().font.draw(matrixStack, DCtion, this.X + 25, this.Y + 5, -5529046); //-5592406
        if (this.icon.getName().equals("skill.combat")) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().font.draw(matrixStack, Integer.toString(CombatSkill.calculateLvlFromXp(this.combatSkill.getXp())), this.X + 50, this.Y + 25, -5529046); //-5592406
            }
        }
    }

    public int getX() {
        return this.X;
    }

    public int getY() {
        return this.Y;
    }
}
