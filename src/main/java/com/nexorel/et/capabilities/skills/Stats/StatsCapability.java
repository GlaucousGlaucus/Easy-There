package com.nexorel.et.capabilities.skills.Stats;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class StatsCapability {

    public static Capability<Stats> STATS_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    ;

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(Stats.class);
    }

}
