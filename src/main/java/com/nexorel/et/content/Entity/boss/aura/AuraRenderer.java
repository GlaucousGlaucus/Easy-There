package com.nexorel.et.content.Entity.boss.aura;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class AuraRenderer extends MobRenderer<AuraEntity, AuraEntityModel> {

    private static final ResourceLocation LOC = new ResourceLocation("et:textures/entity/aura.png");

    public AuraRenderer(EntityRendererManager manager) {
        super(manager, new AuraEntityModel(), 0.5f);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(AuraEntity auraEntity) {
        return LOC;
    }

}
