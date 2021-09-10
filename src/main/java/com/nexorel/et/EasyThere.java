package com.nexorel.et;

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
import com.nexorel.et.setup.ClientSetup;
import com.nexorel.et.setup.ETConfig;
import com.nexorel.et.world.OreGeneration;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO: CRAFTING, Add Choppermerang that right click goes like a boomerang and chops tress it encounters and add skill lvl requrement to it
 */

@Mod("et")
public class EasyThere {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

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

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onAttributeCreate);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::capRegistry);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        OreGeneration.registerConfiguredFeatures();

        MinecraftForge.EVENT_BUS.register(AttachCap.class);
        MinecraftForge.EVENT_BUS.register(Interactions.class);
        MinecraftForge.EVENT_BUS.register(CommandInit.class);
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

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
//        ModelLoaderRegistry.registerLoader(new ResourceLocation(MOD_ID, "aura_infested_block"), new AuraInfestedBlockLoader());
    }
}
