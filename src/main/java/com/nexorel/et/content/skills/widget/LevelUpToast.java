package com.nexorel.et.content.skills.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkill;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkill;
import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkill;
import com.nexorel.et.capabilities.skills.ISkills;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkill;
import com.nexorel.et.content.skills.SkillScreen;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class LevelUpToast implements Toast {

    private final ISkills skill;
    private final int NL;
    private final int OL;

    public LevelUpToast(ISkills skill, int nl, int ol) {
        this.skill = skill;
        NL = nl;
        OL = ol;
    }

    @Override
    public Visibility render(PoseStack stack, ToastComponent component, long VisTime) {
        Component title = new TextComponent(getName() + " Level Up!");
        Component message = new TextComponent("Level: " + OL + " \u2192 " + NL);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        component.blit(stack, 0, 0, 0, 0, this.width(), this.height());
        component.getMinecraft().font.draw(stack, title, 30.0F, 7.0F, 0xfbff00);
        component.getMinecraft().font.draw(stack, message, 30.0F, 18.0F, 0xffffff);
        ItemStack itemstack = SkillScreen.Skills.valueOf(getName().toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT)).getRenderStack();
        component.getMinecraft().getItemRenderer().renderAndDecorateFakeItem(itemstack, 8, 8);
        return VisTime >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }

    private String getName() {
        if (this.skill instanceof CombatSkill) {
            return "Combat";
        }
        if (this.skill instanceof MiningSkill) {
            return "Mining";
        }
        if (this.skill instanceof ForagingSkill) {
            return "Foraging";
        }
        if (this.skill instanceof FarmingSkill) {
            return "Farming";
        }
        return "";
    }
}
