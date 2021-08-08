package com.nexorel.et.content.items.talisBag;

import com.nexorel.et.content.items.Talismans.TalismanItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

import static com.nexorel.et.EasyThere.EASY_THERE;

public class TalismanBagItem extends Item {

    public TalismanBagItem() {
        super(new Item.Properties().tab(EASY_THERE).stacksTo(1));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new TalismanBagCapability(this.getClass());
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide) {
            player.openMenu(new SimpleNamedContainerProvider(TalismanBagContainer::new, new TranslationTextComponent("container.et.talisman_bag")));
        }
        return new ActionResult<>(ActionResultType.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, slot, isSelected);
        if (stack.getItem() instanceof TalismanBagItem) {
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                int slots = h.getSlots();
                for (int i = 0; i < slots; i++) {
                    ItemStack search_stack = h.getStackInSlot(i);
                    if (search_stack.getItem() instanceof TalismanItem) {
                        ((TalismanItem) search_stack.getItem()).getSpecialBuffs(world, entity);
                    }
                }
            });
        }
    }
}
