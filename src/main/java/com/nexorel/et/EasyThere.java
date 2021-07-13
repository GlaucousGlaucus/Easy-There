package com.nexorel.et;

import com.nexorel.et.Registries.*;
import com.nexorel.et.content.Entity.boss.aura.AuraEntity;
import com.nexorel.et.content.Entity.boss.aura.AuraRenderer;
import com.nexorel.et.content.Entity.projectile.aura_blast.AuraBlastRenderer;
import com.nexorel.et.content.blocks.GemRefinery.GRS;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod("et")
public class EasyThere
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public EasyThere() {

        EntityInit.initialization();
        ItemInit.init();
        BlockInit.init();
        TileInit.init();
        ContainerInit.init();
        RecipeInit.init();

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code

        event.enqueueWork(() -> {
            GlobalEntityTypeAttributes.put(EntityInit.AURA.get(), AuraEntity.prepareAttributes().build());
        });
    }

    public static final ItemGroup EASY_THERE = new ItemGroup("easy_there") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.POTION);
        }
    };

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client

        ScreenManager.register(ContainerInit.GRC.get(), GRS::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.AURA.get(), AuraRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.AURA_BLAST.get(), AuraBlastRenderer::new);

        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
