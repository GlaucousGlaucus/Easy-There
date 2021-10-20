package com.nexorel.et.capabilities.chunk;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class SkillChunkCap {

    public static Capability<SkillChunk> BLOCK_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    ;

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(SkillChunk.class);
    }

}
