package com.nexorel.et.content.skills;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static com.nexorel.et.Reference.MOD_ID;

public class SkillScreen extends Screen {

    public static final int WIDTH = 252;
    public static final int HEIGHT = 139;
    SkillTabGUI skillTabGUI = new SkillTabGUI(this);
    private boolean isScrolling;
    private ResourceLocation GUI = new ResourceLocation(MOD_ID, "textures/gui/skill_window.png");
    public static ResourceLocation SKILL_ASSETS_LOC = new ResourceLocation(MOD_ID, "textures/gui/skill_window.png");
    private Player player;

    public SkillScreen() {
        super(new TranslatableComponent("screen.et.skills"));
        this.player = Minecraft.getInstance().player;
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new SkillScreen());
    }

    @Override
    protected void init() {
        this.skillTabGUI.init();
    }

    @Override
    public void tick() {
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
//        long time = Util.getMillis();
//        EasyThere.LOGGER.info(time);
        long Time = Util.getMillis();

        this.renderBackground(matrixStack);
        renderInside(matrixStack, mouseX, mouseY, relX, relY, partialTicks, Time);
        renderWindow(matrixStack, relX, relY);
        renderTooltips(matrixStack, relX, relY, mouseX, mouseY, partialTicks, Time);
    }

    private void renderInside(PoseStack matrixStack, int mouseX, int mouseY, int x, int y, float partialTicks, long Time) {
        SkillTabGUI skillTabGUI = this.skillTabGUI;
        if (skillTabGUI == null) {
            fill(matrixStack, x + 9, y + 18, x + 9 + 234, y + 18 + 113, -16777216);
        } else {
            matrixStack.pushPose();
            matrixStack.translate((float) (x + 9), (float) (y + 18), 0.0F);
            skillTabGUI.drawContents(matrixStack, mouseX, mouseY, partialTicks, Time);
            matrixStack.popPose();
            RenderSystem.depthFunc(515);
            RenderSystem.disableDepthTest();
        }
    }

    public void renderWindow(PoseStack matrixStack, int p_238695_2_, int p_238695_3_) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, GUI);
        this.blit(matrixStack, p_238695_2_, p_238695_3_, 0, 0, 252, 140);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();

        this.font.draw(matrixStack, "Skills", (float) (p_238695_2_ + 8), (float) (p_238695_3_ + 6), 4210752);
    }

    public void renderTooltips(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks, long Time) {
        matrixStack.translate((float) (x + 9), (float) (y + 18), 0.0F);
        this.skillTabGUI.drawTooltips(matrixStack, x, y, mouseX, mouseY, partialTicks, Time);
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
        COMBAT("skill.combat", new ItemStack(Items.DIAMOND_SWORD)),
        MINING("skill.mining", new ItemStack(Items.GOLDEN_PICKAXE));
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
