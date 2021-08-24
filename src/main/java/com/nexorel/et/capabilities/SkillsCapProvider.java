package com.nexorel.et.capabilities;

import com.nexorel.et.EasyThere;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SkillsCapProvider implements ICapabilitySerializable<INBT> {

    private CombatSkill combat_skill = new CombatSkill();
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
    public INBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        INBT combat_skill = CombatSkillCapability.COMBAT_CAP.writeNBT(this.combat_skill, null);
        compoundNBT.put(COMBAT_SKILL_NBT, combat_skill);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        if (nbt.getId() != new CompoundNBT().getId()) {
            EasyThere.LOGGER.warn("Unexpected NBT type" + nbt);
            return;
        }
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        INBT combat_skill_nbt = compoundNBT.get(COMBAT_SKILL_NBT);

        CombatSkillCapability.COMBAT_CAP.readNBT(combat_skill, null, combat_skill_nbt);
    }
}
