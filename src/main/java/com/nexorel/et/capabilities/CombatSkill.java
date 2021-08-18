package com.nexorel.et.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class CombatSkill {

    public CombatSkill() {
        this(0);
    }

    public CombatSkill(int level) {
        this.xp = level;
    }

    public int getLevel() {
        return calculateLvlFromXp(this.xp);
    }

    public static double calculateXpForLevel(int level) {
        // formula: base_xp + (base_xp * level ^ exponent)
        double exponent = 1.04;
        double base_xp = 500;
        return base_xp + (base_xp * Math.pow(level, exponent));
    }

    public static double calculateFullTargetXp(int level) {
        double requiredXP = 0;
        for (int i = 0; i <= level; i++) {
            requiredXP += calculateXpForLevel(i);
        }
        return requiredXP;
    }

    public static int calculateLvlFromXp(double xp) {
        int level = 0;
        double maxXp = calculateXpForLevel(0);
        do {
            maxXp += calculateXpForLevel(level++);
        } while (maxXp < xp);
        return level;
    }

    public void addXp(double points) {
        xp += points;
    }

    private void setXp(int points) {
        xp = points;
    }

    private int xp;

    public static class CombatSkillNBTStorage implements Capability.IStorage<CombatSkill> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<CombatSkill> capability, CombatSkill instance, Direction side) {
            IntNBT intNBT = IntNBT.valueOf(instance.xp);
            return intNBT;
        }

        @Override
        public void readNBT(Capability<CombatSkill> capability, CombatSkill instance, Direction side, INBT nbt) {
            int xp = 0;
            if (nbt.getType() == IntNBT.TYPE) {
                xp = ((IntNBT) nbt).getAsInt();
            }
            instance.setXp(xp);
        }
    }

    public static CombatSkill createDefaultInstance() {
        return new CombatSkill();
    }

}
