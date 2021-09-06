package com.nexorel.et.capabilities;

import com.google.common.collect.Maps;
import com.nexorel.et.Network.EasyTherePacketHandler;
import com.nexorel.et.Network.MiningSkillPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public class MiningSkill {

    private double xp;

    public MiningSkill() {
        this(0);
    }

    public MiningSkill(double points) {
        this.xp = points;
    }

    public static double calculateXpForLevel(int level) {
        // formula: base_xp + (base_xp * level ^ exponent)
        double exponent = 3.8;
        double base_xp = 500;
        return (60 * Math.pow(level, 2)) + level + base_xp + Math.pow(level, exponent);
    }

    public static double calculateFullTargetXp(int level) {
        double requiredXP = 0;
        for (int i = 0; i <= level; i++) {
            requiredXP += calculateXpForLevel(i);
        }
        return requiredXP;
    }

    public static int calculateLvlFromXp(double xp) {
        if (xp < 500) return 0;
        int level = 0;
        double maxXp = calculateXpForLevel(0);
        do {
            maxXp += calculateXpForLevel(++level);
        } while (maxXp < xp);
        return Math.min(level, 20);
    }

    public static double getXPProgress(MiningSkill miningSkill) {
        double xp = miningSkill.getXp();
        int current_lvl = miningSkill.getLevel();
        if (current_lvl == 0) {
            double xp_progress = (xp / ((MiningSkill.calculateXpForLevel(current_lvl + 1)))) * 100;
            return (double) Math.round(xp_progress * 100) / 100;
        }
        double crr_xp = MiningSkill.calculateFullTargetXp(current_lvl - 1);
        if (current_lvl + 1 <= 20) {
            double xp_fornext_level = MiningSkill.calculateFullTargetXp(current_lvl);
            double xp_progress = ((xp - crr_xp) / (xp_fornext_level - crr_xp)) * 100;
            return (double) Math.round(xp_progress * 100) / 100;
        }
        return -1;
    }

    public static StringBuilder getProgressBars(double xp_percent) {
        StringBuilder bars = new StringBuilder();
        if (xp_percent != -1) {
            int bar_no = Mth.clamp((int) Math.round(xp_percent / 5), 0, 18);
            bars.append("-".repeat(Math.max(0, bar_no)));
            return bars;
        } else {
            return bars;
        }
    }

    public int getLevel() {
        return calculateLvlFromXp(this.xp);
    }

    public void addXp(double points) {
//        cap = 482028;
        xp += points;
    }

    public double getXp() {
        return this.xp;
    }

    public void setXp(double points) {
        xp = points;
    }

    public void shareData(ServerPlayer playerEntity) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("xp", this.xp);
        EasyTherePacketHandler.sendDataToClient(new MiningSkillPacket(nbt), playerEntity);
    }

    public static Map<Block, Float> getMiningXp() {
        Map<Block, Float> map = Maps.newLinkedHashMap();
        map.put(Blocks.DIAMOND_ORE, 25.0F);
        return map;
    }

}
