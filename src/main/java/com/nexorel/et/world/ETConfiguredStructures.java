package com.nexorel.et.world;

import com.nexorel.et.Registries.StructureInit;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static com.nexorel.et.Reference.MODID;

public class ETConfiguredStructures {

    public static ConfiguredStructureFeature<?, ?> CONFIGURED_AURA_DUNGEON = StructureInit.AURA_DUNGEON.get().configured(NoneFeatureConfiguration.INSTANCE);

    public static void registerConfiguredStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(MODID, "configured_aura_dungeon"), CONFIGURED_AURA_DUNGEON);
    }

}
