package com.nexorel.et.capabilities.skills.CombatSkill;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CombatSkillCapability {

    public static Capability<CombatSkill> COMBAT_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(CombatSkill.class);
    }

}
