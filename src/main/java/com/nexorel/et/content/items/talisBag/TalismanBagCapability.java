package com.nexorel.et.content.items.talisBag;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TalismanBagCapability extends CapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

    private ItemStackHandler inventory = new ItemStackHandler(54) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return !(stack.getItem() instanceof TalismanBagItem);
        }
    };

    public TalismanBagCapability(Class baseClass) {
        super(baseClass);
    }

    @Nonnull
    @Override
    public LazyOptional getCapability(@Nonnull Capability cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> this.inventory);
        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.put("inv", this.inventory.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.inventory.deserializeNBT(nbt);
    }
}
