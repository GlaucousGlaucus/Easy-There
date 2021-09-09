package com.nexorel.et.capabilities.FarmingSkill;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class FarmingSkillCapability {

    @CapabilityInject(FarmingSkill.class)
    public static Capability<FarmingSkill> FARMING_CAP = null;

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(FarmingSkill.class);
    }

}
