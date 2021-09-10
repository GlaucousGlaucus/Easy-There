package com.nexorel.et.capabilities.chunk;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class SkillChunkCap {

    @CapabilityInject(SkillChunk.class)
    public static Capability<SkillChunk> BLOCK_CAP = null;

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(SkillChunk.class);
    }

}
