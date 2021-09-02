package com.nexorel.et.content.Entity.boss.aura;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class AuraRenderer extends MobRenderer<AuraEntity, AuraEntityModel> {

    private static final ResourceLocation LOC = new ResourceLocation("et:textures/entity/aura.png");

    public AuraRenderer(EntityRendererProvider.Context context) {
        super(context, new AuraEntityModel(context.getModelSet().bakeLayer(AuraEntityModel.CUBE_LAYER)), 0.5f);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(AuraEntity auraEntity) {
        return LOC;
    }

}
