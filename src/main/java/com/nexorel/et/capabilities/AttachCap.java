package com.nexorel.et.capabilities;

import com.nexorel.et.capabilities.CombatSkill.CombatSkillCapProvider;
import com.nexorel.et.capabilities.FarmingSkill.FarmingSkillCapProvider;
import com.nexorel.et.capabilities.ForagingSkill.ForagingSkillCapProvider;
import com.nexorel.et.capabilities.MiningSkill.MiningSkillCapProvider;
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
            event.addCapability(new ResourceLocation("et:combat_skill_cap"), new CombatSkillCapProvider());
            event.addCapability(new ResourceLocation("et:mining_skill_cap"), new MiningSkillCapProvider());
            event.addCapability(new ResourceLocation("et:foraging_skill_cap"), new ForagingSkillCapProvider());
            event.addCapability(new ResourceLocation("et:farming_skill_cap"), new FarmingSkillCapProvider());
        }
    }

}

