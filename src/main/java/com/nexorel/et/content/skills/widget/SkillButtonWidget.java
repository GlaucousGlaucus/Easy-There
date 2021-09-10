package com.nexorel.et.content.skills.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkill;
import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkillCapability;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkill;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkillCapability;
import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkill;
import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkillCapability;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkill;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkillCapability;
import com.nexorel.et.content.skills.SkillScreen;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.util.Mth;

public class SkillButtonWidget extends GuiComponent {

    private final SkillScreen.Skills icon;
    private int X;
    private int Y;
    private static int r = 255;
    private static int g = 0;
    private static int b = 0;
    private final SkillScreen skillScreen;
    private final CombatSkill combatSkill = Minecraft.getInstance().player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
    private final MiningSkill miningSkill = Minecraft.getInstance().player.getCapability(MiningSkillCapability.MINING_CAP).orElse(null);
    private final ForagingSkill foragingSkill = Minecraft.getInstance().player.getCapability(ForagingSkillCapability.FORAGING_CAP).orElse(null);
    private final FarmingSkill farmingSkill = Minecraft.getInstance().player.getCapability(FarmingSkillCapability.FARMING_CAP).orElse(null);
    public boolean isSelected;
    private long animationTime = -1L;

    public SkillButtonWidget(SkillScreen skillScreen, SkillScreen.Skills icon, int x, int y) {
        this.icon = icon;
        this.X = Mth.floor(x);
        this.Y = Mth.floor(y);
        this.skillScreen = skillScreen;
    }

    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks, long Time) {
        RenderSystem.setShaderTexture(0, SkillScreen.SKILL_ASSETS_LOC);
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
        float f = Mth.clamp((float) (Time - this.animationTime) / 300, 0.0F, 1.0F);
        f = f * f;
        return f;
    }

    public void animateAndRender(PoseStack matrixStack) {
        long Time = Util.getMillis();

        RenderSystem.setShaderTexture(0, SkillScreen.SKILL_ASSETS_LOC);

        if (this.animationTime == -1L) {
            this.animationTime = Time;
        }

        float a = 15 * getVisibility(Time);
        float b = 0F;
        float c = 0F;

        matrixStack.pushPose();
        RenderSystem.enableBlend();
        this.blit(matrixStack, (this.X), (this.Y), 25, 143, 25, 25);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.3F + Mth.clamp((getVisibility(Time) * 2), 0F, 0.7F));
        matrixStack.translate(a, b, c);
        renderHoverGUI(matrixStack);
        matrixStack.translate(-a, -b, -c);
        RenderSystem.disableBlend();
        matrixStack.popPose();
        this.icon.drawIcon(Minecraft.getInstance().getItemRenderer(), this.X + 3, this.Y + 3);
    }

    private static int getLevelColor(int level) {
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

    private static int getXp_percent_color(double xp_percent) {
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

    public void renderHoverGUI(PoseStack matrixStack) {
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

        rgbINC();
        if (this.icon.getName().equals("skill.combat") && Minecraft.getInstance().player != null) {
            drawForCombat(matrixStack, this.combatSkill, this.X, this.Y);
        }
        if (this.icon.getName().equals("skill.mining") && Minecraft.getInstance().player != null) {
            drawForMining(matrixStack, this.miningSkill, this.X, this.Y);
        }
        if (this.icon.getName().equals("skill.foraging") && Minecraft.getInstance().player != null) {
            drawForForaging(matrixStack, this.foragingSkill, this.X, this.Y);
        }
        if (this.icon.getName().equals("skill.farming") && Minecraft.getInstance().player != null) {
            drawForFarming(matrixStack, this.farmingSkill, this.X, this.Y);
        }
    }

    private static void drawForFarming(PoseStack matrixStack, FarmingSkill farmingSkill, int X, int Y) {
        Minecraft.getInstance().font.draw(matrixStack, "\u2692 Farming \u2692", X + 48, Y + 5, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, "Level:", X + 68, Y + 20, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, Integer.toString(FarmingSkill.calculateLvlFromXp(farmingSkill.getXp())), X + 98, Y + 20, getLevelColor(FarmingSkill.calculateLvlFromXp(farmingSkill.getXp())));

        double xp_percent = FarmingSkill.getXPProgress(farmingSkill);

        Minecraft.getInstance().font.draw(matrixStack, xp_percent == -1 ? "" : "Progress To Level " + (FarmingSkill.calculateLvlFromXp(farmingSkill.getXp()) + 1), X + 35, Y + 35, 0x0fff7); //-5592406
        if (xp_percent != -1)
            Minecraft.getInstance().font.draw(matrixStack, xp_percent + " %", X + 60, Y + 53, getXp_percent_color(xp_percent)); //-5592406

        StringBuilder bars = FarmingSkill.getProgressBars(xp_percent);
        if (xp_percent != -1) Minecraft.getInstance().font.draw(matrixStack, "------------------", X + 35, Y + 45, 0);
        Minecraft.getInstance().font.draw(matrixStack, bars.toString(), X + 35, Y + 45, xp_percent != -1 ? getXp_percent_color(xp_percent) : 0x00ff11); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, "XP: ", X + 35, Y + 65, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, Double.toString((double) Math.round(farmingSkill.getXp() * 100) / 100), X + 55, Y + 65, 0x77ff00); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, "XP MULTIPLIER: ", X + 35, Y + 80, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, Math.round(farmingSkill.getLevel() * 0.05 * 100) / 100 + "x", X + 35 + Minecraft.getInstance().font.width("XP MULTIPLIER: "), Y + 80, 0x77ff00); //-5592406
        if (xp_percent == -1) {
            String hex = String.format("%02x%02x%02x", r, g, b);
            int Hex = Integer.parseInt(hex, 16);
            Minecraft.getInstance().font.draw(matrixStack, "SKILL MAXED", X + (57), Y + 42, Hex);
        }
    }

    private static void drawForForaging(PoseStack matrixStack, ForagingSkill foragingSkill, int X, int Y) {
        Minecraft.getInstance().font.draw(matrixStack, "\u2692 Foraging \u2692", X + 48, Y + 5, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, "Level:", X + 68, Y + 20, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, Integer.toString(ForagingSkill.calculateLvlFromXp(foragingSkill.getXp())), X + 98, Y + 20, getLevelColor(ForagingSkill.calculateLvlFromXp(foragingSkill.getXp())));

        double xp_percent = ForagingSkill.getXPProgress(foragingSkill);

        Minecraft.getInstance().font.draw(matrixStack, xp_percent == -1 ? "" : "Progress To Level " + (ForagingSkill.calculateLvlFromXp(foragingSkill.getXp()) + 1), X + 35, Y + 35, 0x0fff7); //-5592406
        if (xp_percent != -1)
            Minecraft.getInstance().font.draw(matrixStack, xp_percent + " %", X + 60, Y + 53, getXp_percent_color(xp_percent)); //-5592406

        StringBuilder bars = ForagingSkill.getProgressBars(xp_percent);
        if (xp_percent != -1) Minecraft.getInstance().font.draw(matrixStack, "------------------", X + 35, Y + 45, 0);
        Minecraft.getInstance().font.draw(matrixStack, bars.toString(), X + 35, Y + 45, xp_percent != -1 ? getXp_percent_color(xp_percent) : 0x00ff11); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, "XP: ", X + 35, Y + 65, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, Double.toString((double) Math.round(foragingSkill.getXp() * 100) / 100), X + 55, Y + 65, 0x77ff00); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, "XP MULTIPLIER: ", X + 35, Y + 80, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, Math.round(foragingSkill.getLevel() * 0.05 * 100) / 100 + "x", X + 35 + Minecraft.getInstance().font.width("XP MULTIPLIER: "), Y + 80, 0x77ff00); //-5592406
        if (xp_percent == -1) {
            String hex = String.format("%02x%02x%02x", r, g, b);
            int Hex = Integer.parseInt(hex, 16);
            Minecraft.getInstance().font.draw(matrixStack, "SKILL MAXED", X + (57), Y + 42, Hex);
        }
    }

    private static void drawForMining(PoseStack matrixStack, MiningSkill miningSkill, int X, int Y) {
        Minecraft.getInstance().font.draw(matrixStack, "\u2692 Mining \u2692", X + 58, Y + 5, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, "Level:", X + 68, Y + 20, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, Integer.toString(MiningSkill.calculateLvlFromXp(miningSkill.getXp())), X + 98, Y + 20, getLevelColor(MiningSkill.calculateLvlFromXp(miningSkill.getXp())));

        double xp_percent = MiningSkill.getXPProgress(miningSkill);

        Minecraft.getInstance().font.draw(matrixStack, xp_percent == -1 ? "" : "Progress To Level " + (MiningSkill.calculateLvlFromXp(miningSkill.getXp()) + 1), X + 35, Y + 35, 0x0fff7); //-5592406
        if (xp_percent != -1)
            Minecraft.getInstance().font.draw(matrixStack, xp_percent + " %", X + 60, Y + 53, getXp_percent_color(xp_percent)); //-5592406

        StringBuilder bars = MiningSkill.getProgressBars(xp_percent);
        if (xp_percent != -1) Minecraft.getInstance().font.draw(matrixStack, "------------------", X + 35, Y + 45, 0);
        Minecraft.getInstance().font.draw(matrixStack, bars.toString(), X + 35, Y + 45, xp_percent != -1 ? getXp_percent_color(xp_percent) : 0x00ff11); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, "XP: ", X + 35, Y + 65, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, Double.toString((double) Math.round(miningSkill.getXp() * 100) / 100), X + 55, Y + 65, 0x77ff00); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, "XP MULTIPLIER: ", X + 35, Y + 80, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, Math.round(miningSkill.getLevel() * 0.05 * 100) / 100 + "x", X + 35 + Minecraft.getInstance().font.width("XP MULTIPLIER: "), Y + 80, 0x77ff00); //-5592406
        if (xp_percent == -1) {
            String hex = String.format("%02x%02x%02x", r, g, b);
            int Hex = Integer.parseInt(hex, 16);
            Minecraft.getInstance().font.draw(matrixStack, "SKILL MAXED", X + (57), Y + 42, Hex);
        }
    }

    private static void drawForCombat(PoseStack matrixStack, CombatSkill combatSkill, int X, int Y) {
        Minecraft.getInstance().font.draw(matrixStack, "\u2694 Combat \u2694", X + 58, Y + 5, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, "Level:", X + 68, Y + 20, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, Integer.toString(CombatSkill.calculateLvlFromXp(combatSkill.getXp())), X + 98, Y + 20, getLevelColor(CombatSkill.calculateLvlFromXp(combatSkill.getXp())));

        double xp_percent = CombatSkill.getXPProgress(combatSkill);

        Minecraft.getInstance().font.draw(matrixStack, xp_percent == -1 ? "" : "Progress To Level " + (CombatSkill.calculateLvlFromXp(combatSkill.getXp()) + 1), X + 35, Y + 35, 0x0fff7); //-5592406
        if (xp_percent != -1)
            Minecraft.getInstance().font.draw(matrixStack, xp_percent + " %", X + 50, Y + 53, getXp_percent_color(xp_percent)); //-5592406

        StringBuilder bars = CombatSkill.getProgressBars(xp_percent);
        if (xp_percent != -1) Minecraft.getInstance().font.draw(matrixStack, "------------------", X + 35, Y + 45, 0);
        Minecraft.getInstance().font.draw(matrixStack, bars.toString(), X + 35, Y + 45, xp_percent != -1 ? getXp_percent_color(xp_percent) : 0x00ff11); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, "XP: ", X + 35, Y + 65, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, Double.toString((double) Math.round(combatSkill.getXp() * 100) / 100), X + 55, Y + 65, 0x77ff00); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, "Crit Chance: ", X + 35, Y + 80, 0x0fff7); //-5592406
        Minecraft.getInstance().font.draw(matrixStack, combatSkill.getCrit_chance() + " %", X + 35 + Minecraft.getInstance().font.width("Crit Chance: "), Y + 80, 0x77ff00); //-5592406
        if (xp_percent == -1) {
            String hex = String.format("%02x%02x%02x", r, g, b);
            int Hex = Integer.parseInt(hex, 16);
            Minecraft.getInstance().font.draw(matrixStack, "SKILL MAXED", X + (57), Y + 42, Hex);
        }
    }

    private static void rgbINC() {
        if (r > 0 && g < 255 && b == 0) {
            g += 3;
        } else if (r > 0 && g == 255 && b == 0) {
            r -= 3;
        } else if (r == 0 && g == 255 && b < 255) {
            b += 3;
        } else if (r == 0 && g > 0 && b == 255) {
            g -= 3;
        } else if (r < 255 && g == 0 && b == 255) {
            r += 3;
        } else if (r == 255 && g == 0 && b > 0) {
            b -= 3;
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
