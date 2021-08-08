package com.nexorel.et.content.items.Talismans;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.nexorel.et.EasyThere.EASY_THERE;

public class TalismanItem extends Item {

    public TalismanItem() {
        super(new Item.Properties().stacksTo(1).tab(EASY_THERE));
    }

    public void getSpecialBuffs(World world, Entity entity) {
        SpecialBuffs(world, entity);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        SpecialBuffs(world, entity);
    }

    protected void SpecialBuffs(World world, Entity entity) {
        if (entity instanceof PlayerEntity) {
            BlockPos pos = entity.blockPosition();
            world.destroyBlock(pos.offset(0, 0, 0), false);
        }
    }
}
