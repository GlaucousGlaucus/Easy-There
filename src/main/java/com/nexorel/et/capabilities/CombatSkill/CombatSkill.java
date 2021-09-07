package com.nexorel.et.capabilities.CombatSkill;

import com.google.common.collect.Maps;
import com.nexorel.et.Network.CombatSkillPacket;
import com.nexorel.et.Network.EasyTherePacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;

public class CombatSkill {

    private double xp;
    private int crit_chance;

    public CombatSkill() {
        this(0, 0);
    }

    public CombatSkill(double points, int crit_chance) {
        this.xp = points;
        this.crit_chance = crit_chance;
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
        if (xp < 500) return 0;
        int level = 0;
        double maxXp = calculateXpForLevel(0);
        do {
            maxXp += calculateXpForLevel(++level);
        } while (maxXp < xp);
        return Math.min(level, 20);
    }

    public static int getMobLevel(LivingEntity mob) {
        double a = (mob.getMaxHealth() + mob.getArmorValue());
        int mob_level = (int) Math.ceil(Math.pow(a / Double.toString(a).length(), 0.65));
        return Math.max(mob_level, 1);
    }

    public boolean canCrit() {
        return Math.random() < ((float) this.crit_chance / 100);
    }

    public static float getCombatXPForMob(int mob_level) {
        return (float) (mob_level * 6.25);
    }

    public static double getXPProgress(CombatSkill combatSkill) {
        double xp = combatSkill.getXp();
        int current_lvl = combatSkill.getLevel();
        if (current_lvl == 0) {
            double xp_progress = (xp / ((CombatSkill.calculateXpForLevel(current_lvl + 1)))) * 100;
            return (double) Math.round(xp_progress * 100) / 100;
        }
        double crr_xp = CombatSkill.calculateFullTargetXp(current_lvl - 1);
        if (current_lvl + 1 <= 20) {
            double xp_fornext_level = CombatSkill.calculateFullTargetXp(current_lvl);
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

    public static Map<EntityType<?>, Float> getCombatXp() {
        Map<EntityType<?>, Float> map = Maps.newLinkedHashMap();
        map.put(EntityType.BAT, getCombatXPForMob(1));
        map.put(EntityType.BEE, getCombatXPForMob(1));
        map.put(EntityType.BLAZE, getCombatXPForMob(5));
        map.put(EntityType.CAT, getCombatXPForMob(1));
        map.put(EntityType.CAVE_SPIDER, getCombatXPForMob(3));
        map.put(EntityType.CHICKEN, getCombatXPForMob(1));
        map.put(EntityType.COD, getCombatXPForMob(1));
        map.put(EntityType.COW, getCombatXPForMob(1));
        map.put(EntityType.CREEPER, getCombatXPForMob(4));
        map.put(EntityType.DOLPHIN, getCombatXPForMob(4));
        map.put(EntityType.DONKEY, getCombatXPForMob(1));
        map.put(EntityType.DROWNED, getCombatXPForMob(5));
        map.put(EntityType.ELDER_GUARDIAN, getCombatXPForMob(8));
        map.put(EntityType.ENDER_DRAGON, getCombatXPForMob(100));
        map.put(EntityType.ENDERMAN, getCombatXPForMob(5));
        map.put(EntityType.ENDERMITE, getCombatXPForMob(2));
        map.put(EntityType.EVOKER, getCombatXPForMob(8));
        map.put(EntityType.FOX, getCombatXPForMob(1));
        map.put(EntityType.GHAST, getCombatXPForMob(6));
        map.put(EntityType.GUARDIAN, getCombatXPForMob(4));
        map.put(EntityType.HOGLIN, getCombatXPForMob(5));
        map.put(EntityType.HORSE, getCombatXPForMob(1));
        map.put(EntityType.HUSK, getCombatXPForMob(4));
        map.put(EntityType.ILLUSIONER, getCombatXPForMob(6));
        map.put(EntityType.IRON_GOLEM, getCombatXPForMob(4));
        map.put(EntityType.LLAMA, getCombatXPForMob(2));
        map.put(EntityType.MAGMA_CUBE, getCombatXPForMob(5));
        map.put(EntityType.MULE, getCombatXPForMob(1));
        map.put(EntityType.MOOSHROOM, getCombatXPForMob(1));
        map.put(EntityType.OCELOT, getCombatXPForMob(1));
        map.put(EntityType.PANDA, getCombatXPForMob(4));
        map.put(EntityType.PARROT, getCombatXPForMob(1));
        map.put(EntityType.PHANTOM, getCombatXPForMob(5));
        map.put(EntityType.PIG, getCombatXPForMob(1));
        map.put(EntityType.PIGLIN, getCombatXPForMob(10));
//        map.put(EntityType.PIGLIN_BRUTE, getCombatXPForMob(15));
        map.put(EntityType.PIGLIN_BRUTE, (float) 10);
        map.put(EntityType.PILLAGER, getCombatXPForMob(9));
        map.put(EntityType.POLAR_BEAR, getCombatXPForMob(5));
        map.put(EntityType.PUFFERFISH, getCombatXPForMob(3));
        map.put(EntityType.RABBIT, getCombatXPForMob(2));
        map.put(EntityType.RAVAGER, getCombatXPForMob(12));
        map.put(EntityType.SALMON, getCombatXPForMob(1));
        map.put(EntityType.SHEEP, getCombatXPForMob(1));
        map.put(EntityType.SHULKER, getCombatXPForMob(7));
        map.put(EntityType.SILVERFISH, getCombatXPForMob(3));
        map.put(EntityType.SKELETON, getCombatXPForMob(4));
        map.put(EntityType.SKELETON_HORSE, getCombatXPForMob(1));
        map.put(EntityType.SLIME, getCombatXPForMob(3));
        map.put(EntityType.SNOW_GOLEM, getCombatXPForMob(3));
        map.put(EntityType.SPIDER, getCombatXPForMob(4));
        map.put(EntityType.SQUID, getCombatXPForMob(1));
        map.put(EntityType.STRAY, getCombatXPForMob(6));
        map.put(EntityType.STRIDER, getCombatXPForMob(1));
        map.put(EntityType.TRADER_LLAMA, getCombatXPForMob(1));
        map.put(EntityType.TROPICAL_FISH, getCombatXPForMob(1));
        map.put(EntityType.TURTLE, getCombatXPForMob(1));
        map.put(EntityType.VEX, getCombatXPForMob(4));
        map.put(EntityType.VILLAGER, getCombatXPForMob(1));
        map.put(EntityType.VINDICATOR, getCombatXPForMob(7));
        map.put(EntityType.WANDERING_TRADER, getCombatXPForMob(2));
        map.put(EntityType.WITCH, getCombatXPForMob(5));
        map.put(EntityType.WITHER, getCombatXPForMob(55));
        map.put(EntityType.WITHER_SKELETON, getCombatXPForMob(9));
        map.put(EntityType.WOLF, getCombatXPForMob(1));
        map.put(EntityType.ZOGLIN, getCombatXPForMob(4));
        map.put(EntityType.ZOMBIE, getCombatXPForMob(4));
        map.put(EntityType.ZOMBIE_HORSE, getCombatXPForMob(1));
        map.put(EntityType.ZOMBIE_VILLAGER, getCombatXPForMob(4));
        map.put(EntityType.ZOMBIFIED_PIGLIN, getCombatXPForMob(7));
        return map;
    }

    public int getLevel() {
        return calculateLvlFromXp(this.xp);
    }

    public void addXp(double points) {
//        double cap = 482028;
        xp += points;
    }

    public double getXp() {
        return this.xp;
    }

    public int getCrit_chance() {
        return this.crit_chance;
    }

    public void setXp(double points) {
        xp = points;
    }

    public void setCrit_chance(int cc) {
        crit_chance = cc;
    }

    public void addCC(double amount) {
        this.crit_chance += amount;
    }

    public void shareData(ServerPlayer playerEntity) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("xp", this.xp);
        nbt.putInt("crit_chance", this.crit_chance);
        EasyTherePacketHandler.sendDataToClient(new CombatSkillPacket(nbt), playerEntity);
    }

}
