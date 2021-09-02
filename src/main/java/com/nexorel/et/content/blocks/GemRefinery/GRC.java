package com.nexorel.et.content.blocks.GemRefinery;

import com.nexorel.et.Registries.BlockInit;
import com.nexorel.et.Registries.ContainerInit;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class GRC extends AbstractContainerMenu {

    public GemRefineryTile tileEntity;
    private Player player;
    private IItemHandler playerinv;
    private int progress;
    private int MaxTime;

    public GRC(int windowID, Inventory playerInventory, Player player, final GemRefineryTile tile) {
        super(ContainerInit.GRC.get(), windowID);

        this.tileEntity = tile;
        this.player = player;
        this.playerinv = new InvWrapper(playerInventory);
        this.progress = tileEntity.current_progress;
        this.MaxTime = tileEntity.Max_Time;

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 50, 31)); // Input 1
                addSlot(new SlotItemHandler(h, 1, 110, 57)); // Output
                addSlot(new SlotItemHandler(h, 2, 110, 31)); // Input 2
            });
        }
        layoutPlayerInventorySlots(8,84);

    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), player, BlockInit.GEM_REFINERY.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index == 0) {
                if (!this.moveItemStackTo(stack, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemstack);
            } else {
                if (index < 28) {
                    if (!this.moveItemStackTo(stack, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 37 && !this.moveItemStackTo(stack, 1, 28, false)) {
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



    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerinv, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerinv, 0, leftCol, topRow, 9, 18);
    }

    @OnlyIn(Dist.CLIENT)
    public int getSmeltProgressionScaled() {
        if (progress <= 0 || MaxTime <= 0) {
            return 0;
        }
        return (MaxTime - progress) * 24 / MaxTime;
    }
}
