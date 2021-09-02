package com.nexorel.et.content.items.Talismans;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class VenomProtectionTalismanItem extends TalismanItem {

    boolean flag = false;

    public VenomProtectionTalismanItem() {
        this(5);
    }

    public VenomProtectionTalismanItem(int cc) {
        super(cc);
    }

    @Override
    protected void SpecialBuffs(Level world, Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.removeEffect(MobEffects.POISON);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> textComponentList, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, world, textComponentList, tooltipFlag);
        textComponentList.add(new TextComponent(ChatFormatting.GREEN + "Gain immunity from Poison"));
    }
}
