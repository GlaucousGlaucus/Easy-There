package com.nexorel.et;

import com.mojang.serialization.Codec;
import com.nexorel.et.Network.EasyTherePacketHandler;
import com.nexorel.et.Registries.*;
import com.nexorel.et.capabilities.AttachCap;
import com.nexorel.et.capabilities.chunk.SkillChunkCap;
import com.nexorel.et.capabilities.interaction.Interactions;
import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkillCapability;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkillCapability;
import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkillCapability;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkillCapability;
import com.nexorel.et.content.Entity.boss.aura.AuraEntity;
import com.nexorel.et.content.blocks.dungeon.puzzles.quiz.QuestionManager;
import com.nexorel.et.setup.ClientSetup;
import com.nexorel.et.setup.ETConfig;
import com.nexorel.et.world.ETConfiguredStructures;
import com.nexorel.et.world.OreGeneration;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.nexorel.et.Reference.MODID;

@Mod("et")
public class EasyThere {
    // TODO One day correct the spelling of the legendary chest loot table
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public EasyThere() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ETConfig.clientSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ETConfig.commonSpec);

        EasyTherePacketHandler.register();
        EntityInit.initialization();
        ItemInit.init();
        BlockInit.init();
        BlockEntityInit.init();
        ContainerInit.init();
        RecipeInit.init();
        LootInit.init();
        StructureInit.DEFERRED_REGISTRY_STRUCTURE.register(FMLJavaModLoadingContext.get().getModEventBus());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onAttributeCreate);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::capRegistry);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::biomeModification);
        MinecraftForge.EVENT_BUS.addListener(this::reload);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        OreGeneration.registerConfiguredFeatures();

        MinecraftForge.EVENT_BUS.register(AttachCap.class);
        MinecraftForge.EVENT_BUS.register(Interactions.class);
        MinecraftForge.EVENT_BUS.register(CommandInit.class);
        event.enqueueWork(() -> {
            StructureInit.setupStructures();
            ETConfiguredStructures.registerConfiguredStructures();
            ProcessorInit.init();
        });
    }

    public void biomeModification(final BiomeLoadingEvent event) {
        Biome.BiomeCategory category = event.getCategory();
        if (category.equals(Biome.BiomeCategory.PLAINS)) {
            event.getGeneration().getStructures().add(() -> ETConfiguredStructures.CONFIGURED_AURA_DUNGEON);
        }
    }

    private static Method GETCODEC_METHOD;

    public void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel serverWorld) {

            try {
                if (GETCODEC_METHOD == null)
                    GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
                if (cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            } catch (Exception e) {
                LOGGER.error("Was unable to check if " + serverWorld.dimension().location() + " is using Terraforged's ChunkGenerator.");
            }

            if ((serverWorld.getChunkSource().getGenerator() instanceof FlatLevelSource &&
                    serverWorld.dimension().equals(Level.OVERWORLD))) {
                return;
            }
            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
            tempMap.putIfAbsent(StructureInit.AURA_DUNGEON.get(), StructureSettings.DEFAULTS.get(StructureInit.AURA_DUNGEON.get()));
            serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }

    private static final QuestionManager manager = new QuestionManager();

    public void reload(AddReloadListenerEvent event) {
        event.addListener(manager);
        LOGGER.info(MODID + ": Reloading!");
        LOGGER.info(manager.getName());
        LOGGER.info(MODID + ": Reload Complete");
    }

    public static QuestionManager getQNAManager() {
        return manager;
    }

    public void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(EntityInit.AURA.get(), AuraEntity.prepareAttributes().build());
    }

    public void capRegistry(RegisterCapabilitiesEvent event) {
        CombatSkillCapability.registerCapabilities(event);
        MiningSkillCapability.registerCapabilities(event);
        ForagingSkillCapability.registerCapabilities(event);
        FarmingSkillCapability.registerCapabilities(event);
        SkillChunkCap.registerCapabilities(event);
    }

    public static final CreativeModeTab EASY_THERE = new CreativeModeTab("easy_there") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.POTION);
        }
    };

    /*@SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(MOD_ID, "aura_infested_block"), new AuraInfestedBlockLoader());
    }*/
}
