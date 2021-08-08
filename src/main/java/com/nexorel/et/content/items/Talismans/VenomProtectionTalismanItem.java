package com.nexorel.et.content.items.Talismans;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class VenomProtectionTalismanItem extends TalismanItem {

    @Override
    protected void SpecialBuffs(World world, Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.removeEffect(Effects.POISON);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> textComponentList, ITooltipFlag tooltipFlag) {
        super.appendHoverText(stack, world, textComponentList, tooltipFlag);
        textComponentList.add(new StringTextComponent(TextFormatting.GREEN + "Gain immunity from Poison"));
    }
}
