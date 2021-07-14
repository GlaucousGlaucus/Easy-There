package com.nexorel.et.setup;


import com.nexorel.et.Registries.BlockInit;
import com.nexorel.et.Registries.ContainerInit;
import com.nexorel.et.Registries.EntityInit;
import com.nexorel.et.content.Entity.boss.aura.AuraRenderer;
import com.nexorel.et.content.Entity.projectile.aura_blast.AuraBlastRenderer;
import com.nexorel.et.content.blocks.AuraInfestedBlock.AuraInfestedBlockLoader;
import com.nexorel.et.content.blocks.GemRefinery.GRS;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.nexorel.et.Reference.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        // do something that can only be done on the client

        ScreenManager.register(ContainerInit.GRC.get(), GRS::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.AURA.get(), AuraRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.AURA_BLAST.get(), AuraBlastRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(TileInit.AURA_INFESTED_TILE.get(), AuraInfestedRender::new);

        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(BlockInit.AURA_INFESTED_BLOCK.get(), (RenderType) -> true);
        });
    }

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(MOD_ID, "aura_infested_block"), new AuraInfestedBlockLoader());
    }
}
