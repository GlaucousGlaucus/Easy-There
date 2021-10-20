package com.nexorel.et.util;

import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkillCapability;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkillCapability;
import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkillCapability;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkillCapability;
import com.nexorel.et.capabilities.skills.Stats.StatsCapability;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CapabilityHelper {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void cap_update(ServerPlayer serverPlayerEntity) {
        serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> combatSkill.deliverDataToClient(serverPlayerEntity));
        serverPlayerEntity.getCapability(MiningSkillCapability.MINING_CAP).ifPresent(miningSkill -> miningSkill.deliverDataToClient(serverPlayerEntity));
        serverPlayerEntity.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(foragingSkill -> foragingSkill.deliverDataToClient(serverPlayerEntity));
        serverPlayerEntity.getCapability(FarmingSkillCapability.FARMING_CAP).ifPresent(farmingSkill -> farmingSkill.deliverDataToClient(serverPlayerEntity));
        serverPlayerEntity.getCapability(StatsCapability.STATS_CAP).ifPresent(stats -> stats.shareData(serverPlayerEntity));
    }

    public static void death_cap_update(ServerPlayer serverPlayerEntity_original, ServerPlayer serverPlayerEntity_new) {
        serverPlayerEntity_original.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> serverPlayerEntity_new.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill_new -> {
            combatSkill_new.setXp(combatSkill.getXp());
        }));
        serverPlayerEntity_original.getCapability(MiningSkillCapability.MINING_CAP).ifPresent(miningSkill -> serverPlayerEntity_new.getCapability(MiningSkillCapability.MINING_CAP).ifPresent(miningSkill_new -> {
            miningSkill_new.setXp(miningSkill.getXp());
        }));
        serverPlayerEntity_original.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(foragingSkill -> serverPlayerEntity_new.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(foragingSkill1 -> {
            foragingSkill1.setXp(foragingSkill.getXp());
        }));
        serverPlayerEntity_original.getCapability(FarmingSkillCapability.FARMING_CAP).ifPresent(farmingSkill -> serverPlayerEntity_new.getCapability(FarmingSkillCapability.FARMING_CAP).ifPresent(farmingSkill1 -> {
            farmingSkill1.setXp(farmingSkill.getXp());
        }));
        serverPlayerEntity_original.getCapability(StatsCapability.STATS_CAP).ifPresent(stats -> serverPlayerEntity_new.getCapability(StatsCapability.STATS_CAP).ifPresent(stats_new -> {
            stats_new.setAccuracy(stats.getAccuracy());
            stats_new.setAgility(stats.getAgility());
            stats_new.setStrength(stats.getStrength());
            stats_new.setFortune(stats.getFortune());
            stats_new.setCrit_chance(stats_new.getCrit_chance());
        }));
    }

}
