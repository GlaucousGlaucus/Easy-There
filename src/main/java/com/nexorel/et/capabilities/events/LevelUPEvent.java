package com.nexorel.et.capabilities.events;

import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkill;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkill;
import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkill;
import com.nexorel.et.capabilities.skills.ISkills;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkill;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class LevelUPEvent extends Event {

    private final Player player;
    private final int new_lvl;
    private final int prev_lvl;
    private final ISkills skill;

    public LevelUPEvent(Player player, int new_lvl, int prev_lvl, ISkills skill) {
        this.player = player;
        this.new_lvl = new_lvl;
        this.prev_lvl = prev_lvl;
        this.skill = skill;
    }

    public Player getPlayer() {
        return player;
    }

    public int getNew_lvl() {
        return new_lvl;
    }

    public int getPrev_lvl() {
        return prev_lvl;
    }

    public ISkills getSkill() {
        return skill;
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
