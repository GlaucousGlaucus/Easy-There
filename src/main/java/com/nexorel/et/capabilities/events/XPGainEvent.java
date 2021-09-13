package com.nexorel.et.capabilities.events;

import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkill;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkill;
import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkill;
import com.nexorel.et.capabilities.skills.ISkills;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkill;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class XPGainEvent extends Event {

    private final Player player;
    private final ISkills skill;
    private final double xp_gain;

    public XPGainEvent(Player player, ISkills skill, double xp_gain) {
        this.player = player;
        this.skill = skill;
        this.xp_gain = xp_gain;
    }

    public Player getPlayer() {
        return player;
    }

    public ISkills getSkill() {
        return skill;
    }

    public double getXp_gain() {
        return xp_gain;
    }

    public String getSkillName() {
        String name = "";
        if (this.skill instanceof CombatSkill) name = "Combat";
        if (this.skill instanceof MiningSkill) name = "Mining";
        if (this.skill instanceof FarmingSkill) name = "Farming";
        if (this.skill instanceof ForagingSkill) name = "Foraging";
        return name;
    }
}
