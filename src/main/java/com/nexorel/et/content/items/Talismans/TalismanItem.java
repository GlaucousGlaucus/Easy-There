package com.nexorel.et.content.items.Talismans;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.nexorel.et.EasyThere.EASY_THERE;

public class TalismanItem extends Item {

    private int cc;

    public TalismanItem(int cc) {
        super(new Item.Properties().stacksTo(1).tab(EASY_THERE));
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
        if (entity instanceof Player) {
            BlockPos pos = entity.blockPosition();
            world.destroyBlock(pos.offset(0, 0, 0), false);
        }
    }

    public int getCc() {
        return cc;
    }

    public void setCc(int cc) {
        this.cc = cc;
    }
}
