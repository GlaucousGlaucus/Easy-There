package com.nexorel.et.content.items.talisBag;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TalismanBagCapability extends CapabilityProvider implements ICapabilitySerializable<CompoundTag> {

    private ItemStackHandler inventory = new ItemStackHandler(54) {
        @Override
        protected void onContentsChanged(int slot) {
            this.serializeNBT();
        }

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
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("inv", this.inventory.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.inventory.deserializeNBT(nbt);
    }
}
