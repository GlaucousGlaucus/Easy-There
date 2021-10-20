package com.nexorel.et.util;

import com.nexorel.et.EasyThere;
import com.nexorel.et.capabilities.chunk.SkillChunkCap;
import com.nexorel.et.capabilities.events.XPGainEvent;
import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkill;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkill;
import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkill;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkill;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.PistonEvent;

import java.util.Map;

public class XPAssignHelper {

    public static void assignCombatXP(LivingEntity target, CombatSkill combatSkill, Player player, Level world) {
        if (EasyThere.getCombatXpManager().getAllXps().containsKey(target.getType())) {
            float xp = CombatHelper.getCombatXpForMob(target.getType());
            MinecraftForge.EVENT_BUS.post(new XPGainEvent(player, combatSkill, xp));
        } else {
            int mob_level = CombatSkill.getMobLevel(target);
            float unknown_mob_xp = CombatSkill.getCombatXPForMob(mob_level);
            MinecraftForge.EVENT_BUS.post(new XPGainEvent(player, combatSkill, unknown_mob_xp));
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 3F, 2F);
    }

    public static void assignForagingXP(Block target, ForagingSkill foragingSkill, Player player, Level level) {
        if (player.isCreative()) return;
        Map<Block, Float> xps = ForagingSkill.getForagingXp();
        if (xps.get(target) != null) {
            float xp = xps.get(target);
            xp = (float) (xp + (xp * (foragingSkill.getLevel() * 0.05)));
            MinecraftForge.EVENT_BUS.post(new XPGainEvent(player, foragingSkill, xp));
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 3F, 2F);
    }

    public static void assignFarmingXP(Block target, FarmingSkill farmingSkill, Player player, Level world) {
        if (player.isCreative()) return;
        Map<Block, Float> xps = FarmingSkill.getFarmingXp();
        if (xps.get(target) != null) {
            float xp = xps.get(target);
            xp = (float) (xp + (xp * (farmingSkill.getLevel() * 0.05)));
            MinecraftForge.EVENT_BUS.post(new XPGainEvent(player, farmingSkill, xp));
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 3F, 2F);
    }

    public static void assignMiningXP(Block target, MiningSkill miningSkill, Player player, Level world) {
        if (player.isCreative()) return;
        Map<Block, Float> xps = MiningSkill.getMiningXp();
        float xp;
        if (xps.get(target) != null) {
            xp = xps.get(target);
            xp = (float) (xp + (xp * (miningSkill.getLevel() * 0.05)));
            MinecraftForge.EVENT_BUS.post(new XPGainEvent(player, miningSkill, xp));
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 3F, 2F);
    }

    public static void saveChunkPistonDataAcrossChunks(PistonEvent.PistonMoveType moveType, LevelChunk chunk, LevelChunk sec, BlockPos piston_pos, BlockState piston_state, BlockPos target_ex_pos, BlockState t_state) {
        if (moveType == PistonEvent.PistonMoveType.EXTEND) {
            chunk.getCapability(SkillChunkCap.BLOCK_CAP).ifPresent(skillChunk -> {
                sec.getCapability(SkillChunkCap.BLOCK_CAP).ifPresent(skillChunk1 -> {
                    if (!skillChunk1.getStoredPos().containsKey(target_ex_pos)) {
                        skillChunk1.addData(target_ex_pos, piston_state);
                    }
                    skillChunk.removeData(piston_pos);
                });
            });
        } else if (moveType == PistonEvent.PistonMoveType.RETRACT) {
            chunk.getCapability(SkillChunkCap.BLOCK_CAP).ifPresent(skillChunk -> {
                sec.getCapability(SkillChunkCap.BLOCK_CAP).ifPresent(skillChunk1 -> {
                    if (!skillChunk.getStoredPos().containsKey(piston_pos)) {
                        skillChunk.addData(piston_pos, t_state);
                    }
                    skillChunk1.removeData(target_ex_pos);
                });
            });
        }
        chunk.markUnsaved();
        sec.markUnsaved();
    }

    public static void saveChunkPistonData(PistonEvent.PistonMoveType moveType, LevelChunk chunk, BlockPos piston_pos, BlockState piston_state, BlockPos target_ex_pos, BlockState t_state) {
        if (moveType == PistonEvent.PistonMoveType.EXTEND) {
            chunk.getCapability(SkillChunkCap.BLOCK_CAP).ifPresent(skillChunk -> {
                if (!skillChunk.getStoredPos().containsKey(target_ex_pos)) {
                    skillChunk.addData(target_ex_pos, piston_state);
                }
                skillChunk.removeData(piston_pos);
            });
        } else if (moveType == PistonEvent.PistonMoveType.RETRACT) {
            chunk.getCapability(SkillChunkCap.BLOCK_CAP).ifPresent(skillChunk -> {
                if (!skillChunk.getStoredPos().containsKey(piston_pos)) {
                    skillChunk.addData(piston_pos, t_state);
                }
                skillChunk.removeData(target_ex_pos);
            });
        }
        chunk.markUnsaved();
    }

}
