package com.nexorel.et.capabilities.interaction;

import com.nexorel.et.capabilities.chunk.SkillChunkCap;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkill;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkillCapability;
import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkill;
import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkillCapability;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkill;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkillCapability;
import com.nexorel.et.util.XPAssignHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.world.BlockEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SkillBreakInteraction {

    public static void onBreakInteration(BlockEvent.BreakEvent event, Block target_block, BlockPos target_block_pos, Player player, MiningSkill miningSkill, ForagingSkill foragingSkill, FarmingSkill farmingSkill) {
        if (MiningSkill.getMiningXp().containsKey(target_block)) {
            SkillBreakInteraction.mining(event, player, target_block, target_block_pos, miningSkill);
        } else if (ForagingSkill.getForagingXp().containsKey(target_block)) {
            SkillBreakInteraction.foraging(player, target_block, target_block_pos, foragingSkill);
        } else if (FarmingSkill.getFarmingXp().containsKey(target_block)) {
            SkillBreakInteraction.farming(player, target_block, target_block_pos, farmingSkill);
        }
    }

    public static void mining(BlockEvent.BreakEvent event, Player player, Block target_block, BlockPos target_block_pos, MiningSkill miningSkill) {
        event.setExpToDrop(event.getExpToDrop() + event.getExpToDrop() * miningSkill.getLevel());
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player);
        if (i == 0) {
            XPAssignHelper.assignMiningXP(event.getState().getBlock(), miningSkill, player, player.level);
            if (player instanceof ServerPlayer serverPlayer) {
                if (!serverPlayer.level.isClientSide) {
                    serverPlayer.getCapability(MiningSkillCapability.MINING_CAP).ifPresent(miningSkill1 -> miningSkill.deliverDataToClient(serverPlayer));
                }
            }
        }
    }

    public static void foraging(Player player, Block target_block, BlockPos target_block_pos, ForagingSkill foragingSkill) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (!serverPlayer.level.isClientSide) {
                LevelChunk chunk = serverPlayer.level.getChunkAt(target_block_pos);
                chunk.getCapability(SkillChunkCap.BLOCK_CAP).ifPresent(skillChunk -> {
                    LOGGER.info(!skillChunk.getStoredPos().containsKey(target_block_pos));
                    if (!skillChunk.getStoredPos().containsKey(target_block_pos)) {
                        XPAssignHelper.assignForagingXP(target_block, foragingSkill, player, player.level);
                    }
                });
                serverPlayer.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(skill -> foragingSkill.deliverDataToClient(serverPlayer));
            }
        }
    }

    public static void farming(Player player, Block target_block, BlockPos target_block_pos, FarmingSkill farmingSkill) {
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player);
        if (i == 0) {
            if (player instanceof ServerPlayer serverPlayer) {
                if (!serverPlayer.level.isClientSide) {
                    LevelChunk chunk = serverPlayer.level.getChunkAt(target_block_pos);
                    chunk.getCapability(SkillChunkCap.BLOCK_CAP).ifPresent(skillChunk -> {
                        if (!skillChunk.getStoredPos().containsKey(target_block_pos)) {
                            XPAssignHelper.assignFarmingXP(target_block, farmingSkill, player, player.level);
                        }
                    });
                    serverPlayer.getCapability(FarmingSkillCapability.FARMING_CAP).ifPresent(skill -> farmingSkill.deliverDataToClient(serverPlayer));
                }
            }
        }
    }

    private static final Logger LOGGER = LogManager.getLogger();
}
