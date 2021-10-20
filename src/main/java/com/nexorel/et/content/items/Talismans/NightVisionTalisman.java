package com.nexorel.et.content.items.Talismans;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class NightVisionTalisman extends TalismanItem {

    public NightVisionTalisman() {
        this(100, 10, 5, 100, 0);
    }

    public NightVisionTalisman(double accuracy, double agility, double strength, double fortune, int cc) {
        super(accuracy, agility, strength, fortune, cc);
    }

    @Override
    protected void SpecialBuffs(Level world, Entity entity) {
        if (entity instanceof Player player) {
            MobEffectInstance instance = new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 1, true, false);
            player.addEffect(instance);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> textComponentList, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, world, textComponentList, tooltipFlag);
        if (Screen.hasShiftDown()) {
            textComponentList.add(new TextComponent(ChatFormatting.BLUE + "Stats:"));
            textComponentList.add(new TextComponent(ChatFormatting.RED + "Accuracy" + " \u2609 " + ChatFormatting.WHITE + this.getAccuracy()));
            textComponentList.add(new TextComponent(ChatFormatting.WHITE + "Agility" + " \u2668 " + ChatFormatting.WHITE + this.getAgility()));
            textComponentList.add(new TextComponent(ChatFormatting.DARK_RED + "Strength" + " \u2694 " + ChatFormatting.WHITE + this.getStrength()));
            textComponentList.add(new TextComponent(ChatFormatting.GREEN + "Fortune" + " \u2618 " + ChatFormatting.WHITE + this.getFortune()));
            textComponentList.add(new TextComponent(ChatFormatting.BLUE + "Crit Chance" + " \u2623 " + this.getCc()));
        } else {
            textComponentList.add(new TextComponent(ChatFormatting.AQUA + "Gives Night Vision While Active"));
        }
    }

}
