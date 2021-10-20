package com.nexorel.et.capabilities.skills.ForagingSkill;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class ForagingSkillCapability {

    public static Capability<ForagingSkill> FORAGING_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    ;

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ForagingSkill.class);
    }

}
