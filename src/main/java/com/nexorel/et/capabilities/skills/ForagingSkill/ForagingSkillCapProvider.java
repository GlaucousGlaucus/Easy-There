package com.nexorel.et.capabilities.skills.ForagingSkill;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ForagingSkillCapProvider implements ICapabilitySerializable<CompoundTag> {

    private final ForagingSkill foragingSkill = new ForagingSkill();
    private final static String FORAGING_SKILL_NBT = "foraging_skill";

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForagingSkillCapability.FORAGING_CAP) {
            return (LazyOptional<T>) LazyOptional.of(() -> foragingSkill);
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        if (ForagingSkillCapability.FORAGING_CAP == null) {
            return new CompoundTag();
        } else {
            CompoundTag tag = new CompoundTag();
            tag.putDouble("xp", foragingSkill.getXp());
            return tag;
        }
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (ForagingSkillCapability.FORAGING_CAP != null) {
            foragingSkill.setXp(nbt.getDouble("xp"));
        }
    }

}
