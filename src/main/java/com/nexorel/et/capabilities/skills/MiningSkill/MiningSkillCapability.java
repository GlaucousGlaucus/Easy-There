package com.nexorel.et.capabilities.skills.MiningSkill;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class MiningSkillCapability {

    public static Capability<MiningSkill> MINING_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    ;

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(MiningSkill.class);
    }

}
