package com.nexorel.et.util;

import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkillCapability;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkillCapability;
import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkillCapability;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkillCapability;
import net.minecraft.server.level.ServerPlayer;

public class CapabilityHelper {

    public static void cap_update(ServerPlayer serverPlayerEntity) {
        serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> combatSkill.shareData(serverPlayerEntity));
        serverPlayerEntity.getCapability(MiningSkillCapability.MINING_CAP).ifPresent(miningSkill -> miningSkill.shareData(serverPlayerEntity));
        serverPlayerEntity.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(foragingSkill -> foragingSkill.shareData(serverPlayerEntity));
        serverPlayerEntity.getCapability(FarmingSkillCapability.FARMING_CAP).ifPresent(farmingSkill -> farmingSkill.shareData(serverPlayerEntity));
    }

    public static void death_cap_update(ServerPlayer serverPlayerEntity_original, ServerPlayer serverPlayerEntity_new) {
        serverPlayerEntity_original.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> serverPlayerEntity_new.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill_new -> {
            combatSkill_new.setXp(combatSkill.getXp());
            combatSkill_new.setCrit_chance(combatSkill.getCrit_chance());
            combatSkill_new.shareData(serverPlayerEntity_new);
        }));
        serverPlayerEntity_original.getCapability(MiningSkillCapability.MINING_CAP).ifPresent(miningSkill -> serverPlayerEntity_new.getCapability(MiningSkillCapability.MINING_CAP).ifPresent(miningSkill_new -> {
            miningSkill_new.setXp(miningSkill.getXp());
            miningSkill.shareData(serverPlayerEntity_new);
        }));
        serverPlayerEntity_original.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(foragingSkill -> serverPlayerEntity_new.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(foragingSkill1 -> {
            foragingSkill1.setXp(foragingSkill.getXp());
            foragingSkill.shareData(serverPlayerEntity_new);
        }));
        serverPlayerEntity_original.getCapability(FarmingSkillCapability.FARMING_CAP).ifPresent(farmingSkill -> serverPlayerEntity_new.getCapability(FarmingSkillCapability.FARMING_CAP).ifPresent(farmingSkill1 -> {
            farmingSkill1.setXp(farmingSkill.getXp());
            farmingSkill.shareData(serverPlayerEntity_new);
        }));
    }

}
