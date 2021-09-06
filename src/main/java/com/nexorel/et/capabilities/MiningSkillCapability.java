package com.nexorel.et.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class MiningSkillCapability {

    @CapabilityInject(MiningSkill.class)
    public static Capability<MiningSkill> MINING_CAP = null;

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(MiningSkill.class);
    }

}
