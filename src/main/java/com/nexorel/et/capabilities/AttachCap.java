package com.nexorel.et.capabilities;

import com.nexorel.et.capabilities.chunk.SkillChunkProvider;
import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkillCapProvider;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkillCapProvider;
import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkillCapProvider;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkillCapProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AttachCap {

    @SubscribeEvent
    public static void attachCapToEntity(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Player) {
            event.addCapability(new ResourceLocation("et:combat_skill_cap"), new CombatSkillCapProvider());
            event.addCapability(new ResourceLocation("et:mining_skill_cap"), new MiningSkillCapProvider());
            event.addCapability(new ResourceLocation("et:foraging_skill_cap"), new ForagingSkillCapProvider());
            event.addCapability(new ResourceLocation("et:farming_skill_cap"), new FarmingSkillCapProvider());
        }
    }

    @SubscribeEvent
    public static void attachCapToChunk(AttachCapabilitiesEvent<LevelChunk> event) {
        event.addCapability(new ResourceLocation("et:skill_chunk_cap"), new SkillChunkProvider());
    }

}

