package com.nexorel.et;

import com.nexorel.et.Registries.*;
import com.nexorel.et.content.Entity.boss.aura.AuraEntity;
import com.nexorel.et.setup.ClientSetup;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("et")
public class EasyThere {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public EasyThere() {

        EntityInit.initialization();
        ItemInit.init();
        BlockInit.init();
        TileInit.init();
        ContainerInit.init();
        RecipeInit.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
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

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
//        ModelLoaderRegistry.registerLoader(new ResourceLocation(MOD_ID, "aura_infested_block"), new AuraInfestedBlockLoader());
    }
}
