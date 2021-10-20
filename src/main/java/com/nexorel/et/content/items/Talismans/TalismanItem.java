package com.nexorel.et.content.items.Talismans;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.nexorel.et.EasyThere.EASY_THERE;

public class TalismanItem extends Item {

    private double accuracy;
    private double agility;
    private double strength;
    private double fortune;
    private int cc;

    public TalismanItem(double accuracy, double agility, double strength, double fortune, int cc) {
        super(new Item.Properties().stacksTo(1).tab(EASY_THERE));
        this.agility = agility;
        this.accuracy = accuracy;
        this.strength = strength;
        this.fortune = fortune;
        this.cc = cc;
    }

    public void getSpecialBuffs(Level world, Entity entity) {
        SpecialBuffs(world, entity);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        SpecialBuffs(world, entity);
    }

    protected void SpecialBuffs(Level world, Entity entity) {
        /*if (entity instanceof Player) {
            BlockPos pos = entity.blockPosition();
            world.destroyBlock(pos.offset(0, 0, 0), false);
        }*/
    }

    public int getCc() {
        return cc;
    }

    public void setCc(int cc) {
        this.cc = cc;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getAgility() {
        return agility;
    }

    public void setAgility(double agility) {
        this.agility = agility;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getFortune() {
        return fortune;
    }

    public void setFortune(double fortune) {
        this.fortune = fortune;
    }
}
