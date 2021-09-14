package com.nexorel.et.capabilities.skills.Stats;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class StatsCapability {

    @CapabilityInject(Stats.class)
    public static Capability<Stats> STATS_CAP = null;

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(Stats.class);
    }

}
