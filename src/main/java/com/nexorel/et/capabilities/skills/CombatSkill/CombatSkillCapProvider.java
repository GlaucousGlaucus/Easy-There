package com.nexorel.et.capabilities.skills.CombatSkill;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CombatSkillCapProvider implements ICapabilitySerializable<CompoundTag> {

    private final CombatSkill combat_skill = new CombatSkill();
    private final static String COMBAT_SKILL_NBT = "combat_skill";

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CombatSkillCapability.COMBAT_CAP) {
            return (LazyOptional<T>) LazyOptional.of(() -> combat_skill);
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        if (CombatSkillCapability.COMBAT_CAP == null) {
            return new CompoundTag();
        } else {
            CompoundTag tag = new CompoundTag();
            tag.putDouble("xp", combat_skill.getXp());
//            tag.putInt("crit_chance", combat_skill.getCrit_chance());
            return tag;
        }
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (CombatSkillCapability.COMBAT_CAP != null) {
//            combat_skill.setCrit_chance(nbt.getInt("crit_chance"));
            combat_skill.setXp(nbt.getDouble("xp"));
        }
    }
}
