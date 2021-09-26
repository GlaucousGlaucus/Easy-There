package com.nexorel.et.Registries;

import com.nexorel.et.world.processors.AntiWaterProcessor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import static com.nexorel.et.Reference.MODID;

public class ProcessorInit {

    public static StructureProcessorType<AntiWaterProcessor> ANTI_WATER_PROCESSOR = () -> AntiWaterProcessor.CODEC;

    public static void init() {
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(MODID, "anti_water_processor"), ANTI_WATER_PROCESSOR);
    }

}
