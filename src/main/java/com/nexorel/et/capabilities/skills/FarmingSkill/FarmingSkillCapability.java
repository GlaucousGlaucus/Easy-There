package com.nexorel.et.capabilities.skills.FarmingSkill;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class FarmingSkillCapability {

    public static Capability<FarmingSkill> FARMING_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    ;

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(FarmingSkill.class);
    }

}
