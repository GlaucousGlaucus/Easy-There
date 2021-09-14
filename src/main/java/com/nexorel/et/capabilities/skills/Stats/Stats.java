package com.nexorel.et.capabilities.skills.Stats;

// TODO
public class Stats {

    private double accuracy;
    private double agility;
    private double strength;
    private double fortune;

    public Stats() {
        this(0, 0, 0, 0);
    }

    public Stats(double accuracy, double agility, double strength, double fortune) {
        this.accuracy = accuracy;
        this.agility = agility;
        this.strength = strength;
        this.fortune = fortune;
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
}
