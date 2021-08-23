package com.nexorel.et.capabilities;

import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class CombatSkill {

    public CombatSkill() {
        this(0);
    }

    public CombatSkill(double points) {
        this.xp = points;
    }

    public int getLevel() {
        return calculateLvlFromXp(this.xp);
    }

    public static double calculateXpForLevel(int level) {
        // formula: base_xp + (base_xp * level ^ exponent)
        double exponent = 3.8;
        double base_xp = 500;
        double alpha_xp = (60 * Math.pow(level, 2)) + level + base_xp + Math.pow(level, exponent);
        //base_xp + (base_xp * Math.pow(level, exponent))
        return alpha_xp;
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
        double maxXp = 500;
        if (xp < maxXp) return 0;
        do {
            level += 1;
            maxXp += calculateXpForLevel(level);
        } while (maxXp < xp);
        return level;
    }

    public void addXp(double points) {
        double cap = 482028;
        if (xp != cap) {
            if (xp + points <= cap) {
                xp += points;
            } else if (xp + points > cap) {
                xp = cap;
            }
        }
    }

    public void setXp(double points) {
        xp = points;
    }

    private double xp;

    public static class CombatSkillNBTStorage implements Capability.IStorage<CombatSkill> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<CombatSkill> capability, CombatSkill instance, Direction side) {
            DoubleNBT intNBT = DoubleNBT.valueOf(instance.xp);
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
