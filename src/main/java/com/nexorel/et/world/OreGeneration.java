package com.nexorel.et.world;

import com.google.common.collect.ImmutableList;
import com.nexorel.et.Registries.BlockInit;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.nexorel.et.Reference.MOD_ID;

// TODO
@Mod.EventBusSubscriber
public class OreGeneration {

    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_AGRIYELITE_BLOCK_TARGET_LIST;

    public static ConfiguredFeature<?, ?> AGRIYELITE_BLOCK;

    public static void registerConfiguredFeatures() {

        ORE_AGRIYELITE_BLOCK_TARGET_LIST = ImmutableList.of(
                OreConfiguration.target(
                        OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES,
                        BlockInit.AGRIYELITE_BLOCK.get().defaultBlockState()));

        AGRIYELITE_BLOCK = Feature.ORE.configured(
                        new OreConfiguration(ORE_AGRIYELITE_BLOCK_TARGET_LIST, 8))
                .rangeUniform(VerticalAnchor.bottom(),
                        VerticalAnchor.aboveBottom(15)).squared().count(8);

        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MOD_ID, "agriyelite_block"), AGRIYELITE_BLOCK);
    }

    @SubscribeEvent
    public static void registerBiomeModification(final BiomeLoadingEvent event) {
        registerConfiguredFeatures();
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> AGRIYELITE_BLOCK);
    }

}
