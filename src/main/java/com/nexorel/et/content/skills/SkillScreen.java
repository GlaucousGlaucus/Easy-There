package com.nexorel.et.content.skills;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import static com.nexorel.et.Reference.MOD_ID;

public class SkillScreen extends Screen {

    private static final int WIDTH = 252;
    private static final int HEIGHT = 139;
    SkillTabGUI skillTabGUI = new SkillTabGUI();
    private boolean isScrolling;
    private ResourceLocation GUI = new ResourceLocation(MOD_ID, "textures/gui/skill_window.png");
    private static ResourceLocation SKILL_ASSETS_LOC = new ResourceLocation(MOD_ID, "textures/gui/skill_window.png");
    public static final int ALL_SLOTS_WIDTH = SkillScreen.Skills.values().length * 30 - 5;

    public SkillScreen() {
        super(new TranslationTextComponent("screen.et.skills"));
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new SkillScreen());
    }

    @Override
    protected void init() {
        for (int m = 0; m < SkillScreen.Skills.VALUES.length; m++) {
            SkillScreen.Skills skill = SkillScreen.Skills.VALUES[m];
            skillTabGUI.addWidget(new SkillScreen.SkillButtonWidget(skill, this.width - ALL_SLOTS_WIDTH / 2 - 30, this.height / 2 + m * 30));
        }
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
            for (SkillScreen.SkillButtonWidget widget : skillTabGUI.widgets) {
                widget.renderButton(matrixStack, mouseX, mouseY, partialTicks);
            }
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

    static enum Skills {
        COMBAT(new TranslationTextComponent("skill.combat"), new ItemStack(Items.DIAMOND_SWORD)),
        MINING(new TranslationTextComponent("skill.mining"), new ItemStack(Items.DIAMOND_PICKAXE));

        protected static final SkillScreen.Skills[] VALUES = values();
        final ITextComponent name;
        final ItemStack renderStack;

        Skills(ITextComponent name, ItemStack renderStack) {
            this.name = name;
            this.renderStack = renderStack;
        }

        private ITextComponent getName() {
            return this.name;
        }

        private void drawIcon(ItemRenderer itemRenderer, int p_238729_2_, int p_238729_3_) {
            itemRenderer.renderAndDecorateItem(this.renderStack, p_238729_2_, p_238729_3_);
        }
    }

    public class SkillButtonWidget extends Widget {

        private final SkillScreen.Skills icon;
        private boolean isSelected;
        private int X;
        private int Y;

        public SkillButtonWidget(SkillScreen.Skills icon, int x, int y) {
            super(x, y, 25, 25, icon.getName());
            this.icon = icon;
            this.X = MathHelper.floor(this.x);
            this.Y = MathHelper.floor(this.y);
        }

        @Override
        public void renderButton(MatrixStack matrixStack, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
            matrixStack.translate((double) this.x, (double) this.y, 0.0D);
            Minecraft minecraft = Minecraft.getInstance();
            this.drawSlot(matrixStack, minecraft.getTextureManager(), x + this.x + 3, y + this.y);
            this.icon.drawIcon(SkillScreen.this.itemRenderer, x + this.x + 3, y + this.y);
            if (this.isHovered) {
                this.drawSelection(matrixStack, minecraft.getTextureManager());
            }
        }

        private void drawSlot(MatrixStack matrixStack, TextureManager textureManager, int x, int y) {
            textureManager.bind(SkillScreen.SKILL_ASSETS_LOC);
            matrixStack.translate((double) x + this.x, (double) y + this.y, 0.0D);
            blit(matrixStack, 0, 0, 0.0F, 143.0F, 25, 25, 256, 256);
        }

        private void drawSelection(MatrixStack matrixStack, TextureManager textureManager) {
            textureManager.bind(SkillScreen.SKILL_ASSETS_LOC);
            matrixStack.translate((double) this.x, (double) this.y, 0.0D);
            blit(matrixStack, 0, 0, 25.0F, 143.0F, 25, 25, 256, 256);
        }

        public int getX() {
            return this.X;
        }

        public int getY() {
            return this.Y;
        }
    }
}
