package com.nexorel.et.content.items.talisBag;

import com.nexorel.et.content.items.Talismans.TalismanItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

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
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
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

        return super.finishUsingItem(stack, world, entity);
    }

    public int getOverallCC(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if (nbt != null) {
            return nbt.getInt("cc");
        }
        return 0;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, slot, isSelected);
//        AtomicInteger cc = new AtomicInteger();
        if (stack.getItem() instanceof TalismanBagItem) {
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                int slots = h.getSlots();
                for (int i = 0; i < slots; i++) {
                    ItemStack search_stack = h.getStackInSlot(i);
                    if (search_stack.getItem() instanceof TalismanItem) {
                        ((TalismanItem) search_stack.getItem()).getSpecialBuffs(world, entity);
//                        cc.addAndGet(((TalismanItem) search_stack.getItem()).getCc());
                    }
                }
            });
        }
//        stack.getOrCreateTag().putInt("cc", cc.get());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> iTextComponents, ITooltipFlag flag) {
        super.appendHoverText(stack, world, iTextComponents, flag);
        CompoundNBT nbt = stack.getTag();
        if (nbt != null) {
            iTextComponents.add(new StringTextComponent(TextFormatting.BLUE + "\u2726 " + "Crit Chance: " + nbt.getInt("cc") + " \u2726"));
        }
    }
}
