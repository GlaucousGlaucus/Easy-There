package com.nexorel.et.content.skills;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import static com.nexorel.et.Reference.MOD_ID;

public class SkillScreen extends Screen {

    public static final int WIDTH = 252;
    public static final int HEIGHT = 139;
    SkillTabGUI skillTabGUI = new SkillTabGUI(this);
    private boolean isScrolling;
    private ResourceLocation GUI = new ResourceLocation(MOD_ID, "textures/gui/skill_window.png");
    public static ResourceLocation SKILL_ASSETS_LOC = new ResourceLocation(MOD_ID, "textures/gui/skill_window.png");
    private PlayerEntity player;

    public SkillScreen() {
        super(new TranslationTextComponent("screen.et.skills"));
        this.player = Minecraft.getInstance().player;
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new SkillScreen());
    }

    @Override
    protected void init() {
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;

        this.renderBackground(matrixStack);
        renderInside(matrixStack, mouseX, mouseY, relX, relY, partialTicks);
        renderWindow(matrixStack, relX, relY);
    }

    private void renderInside(MatrixStack matrixStack, int mouseX, int mouseY, int x, int y, float partialTicks) {
        SkillTabGUI skillTabGUI = this.skillTabGUI;
        if (skillTabGUI == null) {
            fill(matrixStack, x + 9, y + 18, x + 9 + 234, y + 18 + 113, -16777216);
        } else {
            matrixStack.pushPose();
            matrixStack.translate((float) (x + 9), (float) (y + 18), 0.0F);
            skillTabGUI.drawContents(matrixStack, mouseX, mouseY, partialTicks);
            matrixStack.popPose();
            RenderSystem.depthFunc(515);
            RenderSystem.disableDepthTest();
        }
    }

    public void renderWindow(MatrixStack matrixStack, int p_238695_2_, int p_238695_3_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        this.minecraft.getTextureManager().bind(GUI);
        this.blit(matrixStack, p_238695_2_, p_238695_3_, 0, 0, 252, 140);

        RenderSystem.enableRescaleNormal();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();

        this.font.draw(matrixStack, "screen.et.skills", (float) (p_238695_2_ + 8), (float) (p_238695_3_ + 6), 4210752);
    }

    @Override
    public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
        if (p_231045_5_ != 0) {
            this.isScrolling = false;
            return false;
        } else {
            if (!this.isScrolling) {
                this.isScrolling = true;
            } else {
                this.skillTabGUI.scroll(p_231045_6_, p_231045_8_);
            }

            return true;
        }
    }

    public enum Skills {
        COMBAT("skill.combat", new ItemStack(Items.DIAMOND_SWORD));
        protected static final SkillScreen.Skills[] VALUES = values();
        final String name;
        final ItemStack renderStack;

        Skills(String name, ItemStack renderStack) {
            this.name = name;
            this.renderStack = renderStack;
        }

        public String getName() {
            return this.name;
        }

        public void drawIcon(ItemRenderer itemRenderer, int x, int y) {
            itemRenderer.renderAndDecorateFakeItem(this.renderStack, x, y);
        }
    }
}
