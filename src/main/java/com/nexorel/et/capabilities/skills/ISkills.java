package com.nexorel.et.capabilities.skills;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

public interface ISkills {

    static double calculateXpForLevel(int level) {
        // formula: base_xp + (base_xp * level ^ exponent)
        double exponent = 3.8;
        double base_xp = 500;
        //base_xp + (base_xp * Math.pow(level, exponent))
        return (60 * Math.pow(level, 2)) + level + base_xp + Math.pow(level, exponent);
    }

    static double calculateFullTargetXp(int level) {
        double requiredXP = 0;
        for (int i = 0; i <= level; i++) {
            requiredXP += calculateXpForLevel(i);
        }
        return requiredXP;
    }

    static int calculateLvlFromXp(double xp) {
        if (xp < 500) return 0;
        int level = 0;
        double maxXp = calculateXpForLevel(0);
        do {
            maxXp += calculateXpForLevel(++level);
        } while (maxXp < xp);
        return Math.min(level, 20);
    }

    static double getXPProgress(ISkills skill) {
        double xp = skill.getXp();
        int current_lvl = skill.getLevel();
        if (current_lvl == 0) {
            double xp_progress = (xp / ((calculateXpForLevel(current_lvl + 1)))) * 100;
            return (double) Math.round(xp_progress * 100) / 100;
        }
        double crr_xp = calculateFullTargetXp(current_lvl - 1);
        if (current_lvl + 1 <= 20) {
            double xp_fornext_level = calculateFullTargetXp(current_lvl);
            double xp_progress = ((xp - crr_xp) / (xp_fornext_level - crr_xp)) * 100;
            return (double) Math.round(xp_progress * 100) / 100;
        }
        return -1;
    }

    static StringBuilder getProgressBars(double xp_percent) {
        StringBuilder bars = new StringBuilder();
        if (xp_percent != -1) {
            int bar_no = Mth.clamp((int) Math.round(xp_percent / 5), 0, 18);
            bars.append("-".repeat(Math.max(0, bar_no)));
        }
        return bars;
    }

    int getLevel();

    void addXp(double points);

    double getXp();

    void shareData(ServerPlayer playerEntity);

}
