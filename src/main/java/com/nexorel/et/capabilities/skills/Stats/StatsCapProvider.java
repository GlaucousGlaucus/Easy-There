package com.nexorel.et.capabilities.skills.Stats;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StatsCapProvider implements ICapabilitySerializable<CompoundTag> {

    private final Stats stats = new Stats();
    private final static String STATS_NBT = "stats";

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == StatsCapability.STATS_CAP) {
            return (LazyOptional<T>) LazyOptional.of(() -> stats);
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        if (StatsCapability.STATS_CAP == null) {
            return new CompoundTag();
        } else {
            CompoundTag tag = new CompoundTag();
            tag.putDouble("accuracy", stats.getAccuracy());
            tag.putDouble("agility", stats.getAgility());
            tag.putDouble("fortune", stats.getFortune());
            tag.putDouble("strength", stats.getStrength());
            tag.putInt("crit_chance", stats.getCrit_chance());
            return tag;
        }
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (StatsCapability.STATS_CAP != null) {
            stats.setAccuracy(nbt.getDouble("accuracy"));
            stats.setAgility(nbt.getDouble("agility"));
            stats.setFortune(nbt.getDouble("fortune"));
            stats.setStrength(nbt.getDouble("strength"));
            stats.setCrit_chance(nbt.getInt("crit_chance"));
        }
    }
}
