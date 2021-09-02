package com.nexorel.et.content.items.talisBag;

import com.nexorel.et.content.items.Talismans.TalismanItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new TalismanBagCapability(this.getClass());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            player.openMenu(new SimpleMenuProvider(TalismanBagContainer::new, new TranslatableComponent("container.et.talisman_bag")));
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
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
        CompoundTag nbt = stack.getTag();
        if (nbt != null) {
            return nbt.getInt("cc");
        }
        return 0;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean isSelected) {
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
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> iTextComponents, TooltipFlag flag) {
        super.appendHoverText(stack, world, iTextComponents, flag);
        CompoundTag nbt = stack.getTag();
        if (nbt != null) {
            iTextComponents.add(new TextComponent(ChatFormatting.BLUE + "\u2726 " + "Crit Chance: " + ChatFormatting.WHITE + nbt.getInt("cc") + ChatFormatting.BLUE + " \u2726"));
        }
    }
}
