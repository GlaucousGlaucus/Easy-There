package com.nexorel.et.content.blocks.dungeon.traps.ArrowTrap;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CustomTrapArrowRenderer extends ArrowRenderer<CustomTrapArrow> {

    public static final ResourceLocation NORMAL_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/arrow.png");

    public CustomTrapArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(CustomTrapArrow arrow) {
        return NORMAL_ARROW_LOCATION;
    }
}
