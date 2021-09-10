package com.nexorel.et.capabilities.skills.MiningSkill;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MiningSkillCapProvider implements ICapabilitySerializable<CompoundTag> {

    private final MiningSkill mining_skill = new MiningSkill();
    private final static String MINING_SKILL_NBT = "mining_skill";

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == MiningSkillCapability.MINING_CAP) {
            return (LazyOptional<T>) LazyOptional.of(() -> mining_skill);
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        if (MiningSkillCapability.MINING_CAP == null) {
            return new CompoundTag();
        } else {
            CompoundTag tag = new CompoundTag();
            tag.putDouble("xp", mining_skill.getXp());
            return tag;
        }
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (MiningSkillCapability.MINING_CAP != null) {
            mining_skill.setXp(nbt.getDouble("xp"));
        }
    }
}
