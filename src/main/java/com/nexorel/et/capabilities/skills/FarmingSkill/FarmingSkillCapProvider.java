package com.nexorel.et.capabilities.skills.FarmingSkill;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FarmingSkillCapProvider implements ICapabilitySerializable<CompoundTag> {

    private final FarmingSkill farmingSkill = new FarmingSkill();
    private final static String FARMING_SKILL_NBT = "farming_skill";

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == FarmingSkillCapability.FARMING_CAP) {
            return (LazyOptional<T>) LazyOptional.of(() -> farmingSkill);
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        if (FarmingSkillCapability.FARMING_CAP == null) {
            return new CompoundTag();
        } else {
            CompoundTag tag = new CompoundTag();
            tag.putDouble("xp", farmingSkill.getXp());
            return tag;
        }
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (FarmingSkillCapability.FARMING_CAP != null) {
            farmingSkill.setXp(nbt.getDouble("xp"));
        }
    }

}
