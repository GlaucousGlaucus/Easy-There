package com.nexorel.et.capabilities.skills.ForagingSkill;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class ForagingSkillCapability {

    @CapabilityInject(ForagingSkill.class)
    public static Capability<ForagingSkill> FORAGING_CAP = null;

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ForagingSkill.class);
    }

}
