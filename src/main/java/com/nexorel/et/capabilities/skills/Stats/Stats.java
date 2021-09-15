package com.nexorel.et.capabilities.skills.Stats;

import com.nexorel.et.Network.CombatSkillPacket;
import com.nexorel.et.Network.EasyTherePacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class Stats {

    private double accuracy;
    private double agility; /*Chance to not trigger traps*/
    private double strength;
    private double fortune;
    private int crit_chance;

    public Stats() {
        this(0, 0, 0, 0, 0);
    }

    public Stats(double accuracy, double agility, double strength, double fortune, int crit_chance) {
        this.accuracy = accuracy;
        this.agility = agility;
        this.strength = strength;
        this.fortune = fortune;
        this.crit_chance = crit_chance;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getAgility() {
        return agility;
    }

    public void setAgility(double agility) {
        this.agility = agility;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getFortune() {
        return fortune;
    }

    public void setFortune(double fortune) {
        this.fortune = fortune;
    }

    public int getCrit_chance() {
        return crit_chance;
    }

    public void setCrit_chance(int crit_chance) {
        this.crit_chance = crit_chance;
    }

    public void shareData(ServerPlayer playerEntity) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("accuracy", this.getAccuracy());
        nbt.putDouble("agility", this.getAgility());
        nbt.putDouble("fortune", this.getFortune());
        nbt.putDouble("strength", this.getStrength());
        nbt.putInt("crit_chance", this.getCrit_chance());
        EasyTherePacketHandler.sendDataToClient(new CombatSkillPacket(nbt), playerEntity);
    }
}
