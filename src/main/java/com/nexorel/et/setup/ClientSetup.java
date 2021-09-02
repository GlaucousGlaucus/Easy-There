package com.nexorel.et.setup;


import com.nexorel.et.Registries.ContainerInit;
import com.nexorel.et.Registries.EntityInit;
import com.nexorel.et.content.Entity.boss.aura.AuraEntityModel;
import com.nexorel.et.content.Entity.boss.aura.AuraRenderer;
import com.nexorel.et.content.Entity.damage_ind.DamageIndicatorRenderer;
import com.nexorel.et.content.Entity.projectile.aura_blast.AuraBlastRenderer;
import com.nexorel.et.content.blocks.GemRefinery.GRS;
import com.nexorel.et.content.items.talisBag.TalismanBagScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import static com.nexorel.et.Reference.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static final KeyMapping TALISMAN_BAG_KEY = new KeyMapping("key.talis_bag", GLFW.GLFW_KEY_Y, "key.categories.inventory");

    public static void init(final FMLClientSetupEvent event) {
        // do something that can only be done on the client

        MenuScreens.register(ContainerInit.GRC.get(), GRS::new);
        MenuScreens.register(ContainerInit.TBC.get(), TalismanBagScreen::new);
        ClientRegistry.registerKeyBinding(TALISMAN_BAG_KEY);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AuraEntityModel.CUBE_LAYER, AuraEntityModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.AURA.get(), AuraRenderer::new);
        event.registerEntityRenderer(EntityInit.AURA_BLAST.get(), AuraBlastRenderer::new);
        event.registerEntityRenderer(EntityInit.DMG_IND.get(), DamageIndicatorRenderer::new);
    }
}
