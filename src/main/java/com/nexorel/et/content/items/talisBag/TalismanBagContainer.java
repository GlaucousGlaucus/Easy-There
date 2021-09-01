package com.nexorel.et.content.items.talisBag;

import com.nexorel.et.Registries.ContainerInit;
import com.nexorel.et.content.items.Talismans.TalismanItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class TalismanBagContainer extends Container {

    private int blocked = -1;
    private ItemStack itemStack;
    private IItemHandler inventory;

    public TalismanBagContainer(int id, PlayerInventory playerInventory, PlayerEntity player) {
        super(ContainerInit.TBC.get(), id);
        this.itemStack = getHeldItem(player);
        this.itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            this.inventory = h;
        });
        final int rowCount = 54 / 9;
        final int yOffset = (rowCount - 4) * 18;
        ((ItemStackHandler) this.inventory).deserializeNBT(this.itemStack.getOrCreateTag().getCompound("inv"));

        for (int i = 0; i < 54; ++i) {
            int x = 8 + 18 * (i % 9);
            int y = 18 + 18 * (i / 9);
            addSlot(new SlotItemHandler(this.inventory, i, x, y));
        }

        // Player inventory
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 104 + y * 18 + yOffset));
            }
        }

        // Hotbar
        for (int x = 0; x < 9; ++x) {
            Slot slot = addSlot(new Slot(playerInventory, x, 8 + x * 18, 162 + yOffset) {
                @Override
                public boolean mayPickup(PlayerEntity playerIn) {
                    return getSlotIndex() != blocked;
                }
            });

            if (x == playerInventory.selected && ItemStack.isSame(playerInventory.getSelected(), itemStack)) {
                blocked = slot.getSlotIndex();
            }
        }
    }

    private static ItemStack getHeldItem(PlayerEntity player) {
        if (isTalisBag(player.getMainHandItem())) {
            return player.getMainHandItem();
        }
        if (isTalisBag(player.getOffhandItem())) {
            return player.getOffhandItem();
        }
        return ItemStack.EMPTY;
    }

    private static boolean isTalisBag(ItemStack stack) {
        return stack.getItem() instanceof TalismanBagItem;
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public void removed(PlayerEntity playerEntity) {
        super.removed(playerEntity);
        this.itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            this.itemStack.getOrCreateTag().put("inv", ((ItemStackHandler) h).serializeNBT());
            int slots = this.inventory.getSlots();
            AtomicInteger cc = new AtomicInteger();
            for (int i = 0; i < slots; i++) {
                ItemStack search_stack = h.getStackInSlot(i);
                if (search_stack.getItem() instanceof TalismanItem)
                    cc.addAndGet(((TalismanItem) search_stack.getItem()).getCc());
            }
            this.itemStack.getOrCreateTag().putInt("cc", cc.get());
        });
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index < 54) {
                if (!this.moveItemStackTo(stack, 54, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemstack);
            } else {
                if (!this.moveItemStackTo(stack, 0, 54, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }
}
