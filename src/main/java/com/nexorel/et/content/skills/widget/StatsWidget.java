package com.nexorel.et.content.skills.widget;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nexorel.et.capabilities.skills.Stats.Stats;
import com.nexorel.et.capabilities.skills.Stats.StatsCapability;
import com.nexorel.et.content.skills.SkillScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class StatsWidget extends GuiComponent {

    private int X;
    private int Y;
    private final SkillScreen skillScreen;
    private static final Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    private static final Font font = minecraft.font;
    private final Stats stats = player.getCapability(StatsCapability.STATS_CAP).orElse(null);
    public boolean isSelected;
    private long animationTime = -1L;

    public StatsWidget(SkillScreen skillScreen, int x, int y) {
        this.X = Mth.floor(x);
        this.Y = Mth.floor(y);
        this.skillScreen = skillScreen;
    }

    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, SkillScreen.SKILL_ASSETS_LOC);
        int relX = (skillScreen.width - 252) / 2;
        int relY = (skillScreen.height - 139) / 2;
        this.isSelected = mouseX >= this.X && mouseY >= this.Y && mouseX < this.X + 25 && mouseY < this.Y + 25 && mouseX > relX + 11 && mouseX < relX + 129 && mouseY > relY + 20 && mouseY < relY + 250;
        if (!isSelected) {
            this.blit(matrixStack, (this.X), (this.Y), 0, 143, 25, 25);
            this.animationTime = -1L;
        }
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
        CompoundTag tag = new CompoundTag();
        ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
        // Gets the head texture of the player who opened the gui,
        // and gives the head that texture
        GameProfile profile = player.getGameProfile();
        tag.put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), profile));
        stack.setTag(tag);
        minecraft.getItemRenderer().renderAndDecorateFakeItem(stack, this.X + 4, this.Y + 4);
    }

    public void renderHoverGUI(PoseStack matrixStack) {
        // Draws the corners of gui
        this.blit(matrixStack, (this.X) + 25, (this.Y), 0, 197, 25, 25); // TL
        this.blit(matrixStack, (this.X) + 25, (this.Y) + (3 * 25), 25, 197, 25, 25); // BL
        this.blit(matrixStack, (this.X) + 50 + (3 * 25), (this.Y), 0, 222, 25, 25); // TR
        this.blit(matrixStack, (this.X) + 50 + (3 * 25), (this.Y) + (3 * 25), 25, 222, 25, 25); // BR

        this.blit(matrixStack, (this.X) + 25, (this.Y) + (25), 26, 171, 25, 25); //Left
        this.blit(matrixStack, (this.X) + 25, (this.Y) + (2 * 25), 26, 171, 25, 25);//Left
        this.blit(matrixStack, (this.X) + 25 + (25), (this.Y) + (3 * 25), 77, 197, 25, 25); //Bottom
        this.blit(matrixStack, (this.X) + 25 + (2 * 25), (this.Y) + (3 * 25), 77, 197, 25, 25); //Bottom
        this.blit(matrixStack, (this.X) + 50 + (2 * 25), (this.Y) + (3 * 25), 77, 197, 25, 25); //Bottom

        this.blit(matrixStack, (this.X) + 50 + (3 * 25), (this.Y) + (25), 52, 171, 25, 25); //Right
        this.blit(matrixStack, (this.X) + 50 + (3 * 25), (this.Y) + (2 * 25), 52, 171, 25, 25); //Right
        this.blit(matrixStack, (this.X) + 50 + (25), (this.Y), 77, 171, 25, 25);// Bottom
        this.blit(matrixStack, (this.X) + 25 + (25), (this.Y), 77, 171, 25, 25);//Bottom
        this.blit(matrixStack, (this.X) + 50 + (2 * 25), (this.Y), 77, 171, 25, 25);//Bottom

        // Draws the gui middle
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                this.blit(matrixStack, (this.X) + 50 + (i * 25), (this.Y) + (j * 25) + 25, 51, 208, 25, 25); // Middle
            }
        }
        drawContents(matrixStack, this.stats, this.X, this.Y);
    }

    private static void drawContents(PoseStack matrixStack, Stats stats, int X, int Y) {
        int i = 1;
        int common_y = 20;
        font.draw(matrixStack, "\u2635 Stats \u2635", X + 65, Y + 5, 0x0fff7);
        font.draw(matrixStack, ChatFormatting.RED + "Accuracy" + " \u2609" + ChatFormatting.RESET, X + 35, Y + common_y, 0x0fff7);
        font.draw(matrixStack, ChatFormatting.WHITE + "Agility" + " \u2668" + ChatFormatting.RESET, X + 35, Y + common_y + (i++ * 15), 0x0fff7);
        font.draw(matrixStack, ChatFormatting.DARK_RED + "Strength" + " \u2694" + ChatFormatting.RESET, X + 35, Y + common_y + (i++ * 15), 0x0fff7);
        font.draw(matrixStack, ChatFormatting.GREEN + "Fortune" + " \u2618" + ChatFormatting.RESET, X + 35, Y + common_y + (i++ * 15), 0x0fff7);
        font.draw(matrixStack, ChatFormatting.BLUE + "Crit Chance" + " \u2623" + ChatFormatting.RESET, X + 35, Y + common_y + (i * 15), 0x0fff7);

        int string_font_length = font.width("Crit Chance :  ") + 48;

        int j = 1;
        font.draw(matrixStack, ChatFormatting.RED + Double.toString(stats.getAccuracy()), X + string_font_length, Y + common_y, 0x0fff7);
        font.draw(matrixStack, ChatFormatting.WHITE + Double.toString(stats.getAgility()), X + string_font_length, Y + common_y + (j++ * 15), 0x0fff7);
        font.draw(matrixStack, ChatFormatting.DARK_RED + Double.toString(stats.getStrength()), X + string_font_length, Y + common_y + (j++ * 15), 0x0fff7);
        font.draw(matrixStack, ChatFormatting.GREEN + Double.toString(stats.getFortune()), X + string_font_length, Y + common_y + (j++ * 15), 0x0fff7);
        font.draw(matrixStack, ChatFormatting.BLUE + Integer.toString(stats.getCrit_chance()), X + string_font_length, Y + common_y + (j * 15), 0x0fff7);
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
