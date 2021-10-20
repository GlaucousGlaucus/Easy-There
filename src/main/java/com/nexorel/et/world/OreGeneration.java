package com.nexorel.et.world;

import com.google.common.collect.ImmutableList;
import com.nexorel.et.Registries.BlockInit;
import com.nexorel.et.setup.ETConfig;
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

import static com.nexorel.et.Reference.MODID;

@Mod.EventBusSubscriber
public class OreGeneration {

    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_AGRIYELITE_BLOCK_TARGET_LIST;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_AQUOMITE_BLOCK_TARGET_LIST;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_GOLD_ALMAO_BLOCK_TARGET_LIST;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_ORANGE_RED_TEMARELITE_BLOCK_TARGET_LIST;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_CRYOPHANITE_BLOCK_TARGET_LIST;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_PEACH_EKANESIA_BLOCK_TARGET_LIST;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_CRIMSON_PECTENE_BLOCK_TARGET_LIST;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_BLUE_VIOLET_AEGIDONYX_BLOCK_TARGET_LIST;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_ELECTRIC_BLUE_CYPBERITE_BLOCK_TARGET_LIST;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_TWINKLING_BREADITE_BLOCK_TARGET_LIST;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_SALMON_LINADINGERITE_BLOCK_TARGET_LIST;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_BLUE_RAPMONY_BLOCK_TARGET_LIST;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_VIOLET_TUNORADOITE_BLOCK_TARGET_LIST;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_MAGENTA_ROSE_LOLLNIC_BLOCK_TARGET_LIST;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_JADE_PETAOGOPITE_BLOCK_TARGET_LIST;

    public static ConfiguredFeature<?, ?> AGRIYELITE_BLOCK;
    public static ConfiguredFeature<?, ?> AQUOMITE_BLOCK;
    public static ConfiguredFeature<?, ?> GOLD_ALMAO_BLOCK;
    public static ConfiguredFeature<?, ?> ORANGE_RED_TEMARELITE_BLOCK;
    public static ConfiguredFeature<?, ?> CRYOPHANITE_BLOCK;
    public static ConfiguredFeature<?, ?> PEACH_EKANESIA_BLOCK;
    public static ConfiguredFeature<?, ?> CRIMSON_PECTENE_BLOCK;
    public static ConfiguredFeature<?, ?> BLUE_VIOLET_AEGIDONYX_BLOCK;
    public static ConfiguredFeature<?, ?> ELECTRIC_BLUE_CYPBERITE_BLOCK;
    public static ConfiguredFeature<?, ?> TWINKLING_BREADITE_BLOCK;
    public static ConfiguredFeature<?, ?> SALMON_LINADINGERITE_BLOCK;
    public static ConfiguredFeature<?, ?> BLUE_RAPMONY_BLOCK;
    public static ConfiguredFeature<?, ?> VIOLET_TUNORADOITE_BLOCK;
    public static ConfiguredFeature<?, ?> MAGENTA_ROSE_LOLLNIC_BLOCK;
    public static ConfiguredFeature<?, ?> JADE_PETAOGOPITE_BLOCK;

    public static void registerConfiguredFeatures() {

        ORE_AGRIYELITE_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.AGRIYELITE_BLOCK.get().defaultBlockState()));
        ORE_AQUOMITE_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.AQUOMITE_BLOCK.get().defaultBlockState()));
        ORE_GOLD_ALMAO_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.GOLD_ALMAO_BLOCK.get().defaultBlockState()));
        ORE_ORANGE_RED_TEMARELITE_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.ORANGE_RED_TEMARELITE_BLOCK.get().defaultBlockState()));
        ORE_CRYOPHANITE_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.CRYOPHANITE_BLOCK.get().defaultBlockState()));
        ORE_PEACH_EKANESIA_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.PEACH_EKANESIA_BLOCK.get().defaultBlockState()));
        ORE_CRIMSON_PECTENE_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.CRIMSON_PECTENE_BLOCK.get().defaultBlockState()));
        ORE_BLUE_VIOLET_AEGIDONYX_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.BLUE_VIOLET_AEGIDONYX_BLOCK.get().defaultBlockState()));
        ORE_ELECTRIC_BLUE_CYPBERITE_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.ELECTRIC_BLUE_CYPBERITE_BLOCK.get().defaultBlockState()));
        ORE_TWINKLING_BREADITE_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.TWINKLING_BREADITE_BLOCK.get().defaultBlockState()));
        ORE_SALMON_LINADINGERITE_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.SALMON_LINADINGERITE_BLOCK.get().defaultBlockState()));
        ORE_BLUE_RAPMONY_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.BLUE_RAPMONY_BLOCK.get().defaultBlockState()));
        ORE_VIOLET_TUNORADOITE_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.VIOLET_TUNORADOITE_BLOCK.get().defaultBlockState()));
        ORE_MAGENTA_ROSE_LOLLNIC_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.MAGENTA_ROSE_LOLLNIC_BLOCK.get().defaultBlockState()));
        ORE_JADE_PETAOGOPITE_BLOCK_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.JADE_PETAOGOPITE_BLOCK.get().defaultBlockState()));

        AGRIYELITE_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_AGRIYELITE_BLOCK_TARGET_LIST, ETConfig.COMMON.AGRIYELITE_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();
        AQUOMITE_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_AQUOMITE_BLOCK_TARGET_LIST, ETConfig.COMMON.AQUOMITE_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();
        GOLD_ALMAO_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_GOLD_ALMAO_BLOCK_TARGET_LIST, ETConfig.COMMON.GOLD_ALMAO_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();
        ORANGE_RED_TEMARELITE_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_ORANGE_RED_TEMARELITE_BLOCK_TARGET_LIST, ETConfig.COMMON.ORANGE_RED_TEMARELITE_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();
        CRYOPHANITE_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_CRYOPHANITE_BLOCK_TARGET_LIST, ETConfig.COMMON.CRYOPHANITE_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();
        PEACH_EKANESIA_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_PEACH_EKANESIA_BLOCK_TARGET_LIST, ETConfig.COMMON.PEACH_EKANESIA_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();
        CRIMSON_PECTENE_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_CRIMSON_PECTENE_BLOCK_TARGET_LIST, ETConfig.COMMON.CRIMSON_PECTENE_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();
        BLUE_VIOLET_AEGIDONYX_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_BLUE_VIOLET_AEGIDONYX_BLOCK_TARGET_LIST, ETConfig.COMMON.BLUE_VIOLET_AEGIDONYX_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();
        ELECTRIC_BLUE_CYPBERITE_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_ELECTRIC_BLUE_CYPBERITE_BLOCK_TARGET_LIST, ETConfig.COMMON.ELECTRIC_BLUE_CYPBERITE_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();
        TWINKLING_BREADITE_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_TWINKLING_BREADITE_BLOCK_TARGET_LIST, ETConfig.COMMON.TWINKLING_BREADITE_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();
        SALMON_LINADINGERITE_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_SALMON_LINADINGERITE_BLOCK_TARGET_LIST, ETConfig.COMMON.SALMON_LINADINGERITE_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();
        BLUE_RAPMONY_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_BLUE_RAPMONY_BLOCK_TARGET_LIST, ETConfig.COMMON.BLUE_RAPMONY_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();
        VIOLET_TUNORADOITE_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_VIOLET_TUNORADOITE_BLOCK_TARGET_LIST, ETConfig.COMMON.VIOLET_TUNORADOITE_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();
        MAGENTA_ROSE_LOLLNIC_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_MAGENTA_ROSE_LOLLNIC_BLOCK_TARGET_LIST, ETConfig.COMMON.MAGENTA_ROSE_LOLLNIC_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();
        JADE_PETAOGOPITE_BLOCK = Feature.ORE.configured(new OreConfiguration(ORE_JADE_PETAOGOPITE_BLOCK_TARGET_LIST, ETConfig.COMMON.JADE_PETAOGOPITE_ORE_SIZE.get())).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(15)).squared();

        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "agriyelite_block"), AGRIYELITE_BLOCK);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "aquomite_block"), AQUOMITE_BLOCK);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "gold_almao_block"), GOLD_ALMAO_BLOCK);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "orange_red_temarelite_block"), ORANGE_RED_TEMARELITE_BLOCK);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "cryophanite_block"), CRYOPHANITE_BLOCK);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "peach_ekanesia_block"), PEACH_EKANESIA_BLOCK);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "crimson_pectene_block"), CRIMSON_PECTENE_BLOCK);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "blue_violet_aegidonyx_block"), BLUE_VIOLET_AEGIDONYX_BLOCK);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "electric_blue_cypberite_block"), ELECTRIC_BLUE_CYPBERITE_BLOCK);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "twinkling_breadite_block"), TWINKLING_BREADITE_BLOCK);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "salmon_linadingerite_block"), SALMON_LINADINGERITE_BLOCK);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "blue_rapmony_block"), BLUE_RAPMONY_BLOCK);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "violet_tunoradoite_block"), VIOLET_TUNORADOITE_BLOCK);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "magenta_rose_lollnic_block"), MAGENTA_ROSE_LOLLNIC_BLOCK);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, "jade_petaogopite_block"), JADE_PETAOGOPITE_BLOCK);
    }

    @SubscribeEvent
    public static void registerBiomeModification(final BiomeLoadingEvent event) {
        registerConfiguredFeatures();
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> AGRIYELITE_BLOCK);
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> AQUOMITE_BLOCK);
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> GOLD_ALMAO_BLOCK);
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> ORANGE_RED_TEMARELITE_BLOCK);
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> CRYOPHANITE_BLOCK);
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> PEACH_EKANESIA_BLOCK);
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> CRIMSON_PECTENE_BLOCK);
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> BLUE_VIOLET_AEGIDONYX_BLOCK);
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> ELECTRIC_BLUE_CYPBERITE_BLOCK);
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> TWINKLING_BREADITE_BLOCK);
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> SALMON_LINADINGERITE_BLOCK);
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> BLUE_RAPMONY_BLOCK);
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> VIOLET_TUNORADOITE_BLOCK);
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> MAGENTA_ROSE_LOLLNIC_BLOCK);
        event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> JADE_PETAOGOPITE_BLOCK);
    }

}
