package com.nexorel.et.capabilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AttachCap {

    @SubscribeEvent
    public static void attachCap(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Player) {
            event.addCapability(new ResourceLocation("et:skills_cap"), new SkillsCapProvider());
        }
    }

}

