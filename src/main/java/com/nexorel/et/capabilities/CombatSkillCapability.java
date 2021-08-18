package com.nexorel.et.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CombatSkillCapability {

    @CapabilityInject(CombatSkill.class)
    public static Capability<CombatSkill> COMBAT_CAP = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(CombatSkill.class, new CombatSkill.CombatSkillNBTStorage(), CombatSkill::createDefaultInstance);
    }

}
