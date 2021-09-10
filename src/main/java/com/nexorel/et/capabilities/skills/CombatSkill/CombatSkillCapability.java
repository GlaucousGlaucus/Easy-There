package com.nexorel.et.capabilities.skills.CombatSkill;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CombatSkillCapability {

    @CapabilityInject(CombatSkill.class)
    public static Capability<CombatSkill> COMBAT_CAP = null;

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(CombatSkill.class);
    }

}
