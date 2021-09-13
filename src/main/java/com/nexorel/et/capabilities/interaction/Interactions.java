package com.nexorel.et.capabilities.interaction;

import com.nexorel.et.LootTable.modifier.SkillBonusModifier;
import com.nexorel.et.capabilities.chunk.SkillChunkCap;
import com.nexorel.et.capabilities.events.LevelUPEvent;
import com.nexorel.et.capabilities.events.XPGainEvent;
import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkillCapability;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkill;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkillCapability;
import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkill;
import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkillCapability;
import com.nexorel.et.capabilities.skills.ISkills;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkill;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkillCapability;
import com.nexorel.et.content.skills.SkillScreen;
import com.nexorel.et.setup.ClientSetup;
import com.nexorel.et.util.CapabilityHelper;
import com.nexorel.et.util.CombatHelper;
import com.nexorel.et.util.XPAssignHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.PistonEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

//@SuppressWarnings("unused")
public class Interactions {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void XPGainEvent(XPGainEvent event) {
        Player player = event.getPlayer();
        ISkills skills = event.getSkill();
        int OL = skills.getLevel();
        double xp = event.getXp_gain();
        skills.addXp(xp);
        player.displayClientMessage(new TextComponent(ChatFormatting.AQUA + event.getSkillName() + " +" + xp), true);
        int NL = skills.getLevel();
        if (NL - OL > 0) {
            MinecraftForge.EVENT_BUS.post(new LevelUPEvent(player, NL, OL, skills));
        }
    }

    @SubscribeEvent
    public static void LevelUpEvent(LevelUPEvent event) {
        Player player = event.getPlayer();
        Level world = event.getPlayer().level;
        int NL = event.getNew_lvl();
        int OL = event.getPrev_lvl();
        player.sendMessage(new TextComponent(ChatFormatting.AQUA + "\u263A" + "Skill Level Up: " + event.getSkillName() + " Level: " + OL + " \u2192 " + NL), player.getUUID());
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 3F, 0.24F);
    }

    @SubscribeEvent
    public static void PistonPostMoveEvent(PistonEvent.Pre event) {
        LevelAccessor levelAccessor = event.getWorld();
        BlockPos piston_pos = event.getFaceOffsetPos();
        BlockPos target_ex_pos = event.getFaceOffsetPos().relative(event.getDirection());
        ChunkSource source = levelAccessor.getChunkSource();
        ChunkAccess piston = levelAccessor.getChunk(piston_pos);
        ChunkAccess tar = levelAccessor.getChunk(target_ex_pos);
        int chunk_x = piston.getPos().x;
        int chunk_z = piston.getPos().z;
        int target_chunk_x = tar.getPos().x;
        int target_chunk_z = tar.getPos().z;
        LevelChunk piston_chunk = source.getChunk(chunk_x, chunk_z, true);
        LevelChunk targ_chunk = source.getChunk(target_chunk_x, target_chunk_z, true);
        boolean x = target_chunk_x != chunk_x;
        boolean z = target_chunk_z != chunk_z;
        boolean f = x || z;
        if (piston_chunk != null && !levelAccessor.isClientSide()) {
            XPAssignHelper.saveChunkPistonData(event.getPistonMoveType(), piston_chunk, piston_pos, levelAccessor.getBlockState(piston_pos), target_ex_pos, levelAccessor.getBlockState(target_ex_pos));
            if (f && targ_chunk != null) {
                XPAssignHelper.saveChunkPistonDataAcrossChunks(event.getPistonMoveType(), piston_chunk, targ_chunk, piston_pos, levelAccessor.getBlockState(piston_pos), target_ex_pos, levelAccessor.getBlockState(target_ex_pos));
            }
        }
    }

    @SubscribeEvent
    public static void PlayerTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer serverPlayerEntity) {
            if (!serverPlayerEntity.level.isClientSide) {
                CombatHelper.calculateAndGiveCritChance(event, serverPlayerEntity);
                serverPlayerEntity.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(foragingSkill -> {
                    int dig_speed_str = (int) Math.min(Math.floor(foragingSkill.getLevel() * 0.15), 2);
                    if (foragingSkill.getLevel() > 10) {
                        serverPlayerEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 3, dig_speed_str));
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void PlayerLogInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer serverPlayerEntity = (ServerPlayer) event.getPlayer();
        if (!serverPlayerEntity.level.isClientSide) {
            serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> combatSkill.shareData(serverPlayerEntity));
            serverPlayerEntity.getCapability(MiningSkillCapability.MINING_CAP).ifPresent(miningSkill -> miningSkill.shareData(serverPlayerEntity));
            serverPlayerEntity.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(foragingSkill -> foragingSkill.shareData(serverPlayerEntity));
            serverPlayerEntity.getCapability(FarmingSkillCapability.FARMING_CAP).ifPresent(farmingSkill -> farmingSkill.shareData(serverPlayerEntity));
        }
    }

    @SubscribeEvent
    public static void PlayerBlockPlaceEvent(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player instanceof ServerPlayer serverPlayer) {//Chunk Stuff
                BlockPos pos = event.getPos();
                LevelChunk chunk = serverPlayer.level.getChunkAt(pos);
                boolean cond_crops = event.getPlacedBlock().getBlock().getTags().contains(SkillBonusModifier.CROPS.getName());
                boolean cond_foraging = event.getPlacedBlock().getBlock().getTags().contains(SkillBonusModifier.XP_LOGS.getName());
                if (cond_crops || cond_foraging) {
                    chunk.getCapability(SkillChunkCap.BLOCK_CAP).ifPresent(skillChunk -> {
                        skillChunk.addData(event.getPos(), event.getPlacedBlock());
                    });
                }
                chunk.markUnsaved();
            }
        }
    }

    @SubscribeEvent
    public static void PlayerRespawnEvent(PlayerEvent.Clone event) {
        ServerPlayer serverPlayerEntity_original = (ServerPlayer) event.getOriginal();
        ServerPlayer serverPlayerEntity_new = (ServerPlayer) event.getPlayer();
        if (!serverPlayerEntity_original.level.isClientSide) {
            serverPlayerEntity_original.reviveCaps();
            if (event.isWasDeath()) {
                CapabilityHelper.cap_update(serverPlayerEntity_original, serverPlayerEntity_new);
            }
            serverPlayerEntity_original.invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void PlayerDimensionChangeEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        ServerPlayer serverPlayerEntity = (ServerPlayer) event.getPlayer();
        if (!serverPlayerEntity.level.isClientSide) {
            serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> combatSkill.shareData(serverPlayerEntity));
            serverPlayerEntity.getCapability(MiningSkillCapability.MINING_CAP).ifPresent(miningSkill -> miningSkill.shareData(serverPlayerEntity));
            serverPlayerEntity.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(foragingSkill -> foragingSkill.shareData(serverPlayerEntity));
            serverPlayerEntity.getCapability(FarmingSkillCapability.FARMING_CAP).ifPresent(farmingSkill -> farmingSkill.shareData(serverPlayerEntity));
        }
    }

    @SubscribeEvent
    public static void GUIOpen(InputEvent.KeyInputEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof SkillScreen) {
            if (event.getAction() == GLFW.GLFW_PRESS && event.getKey() == ClientSetup.TALISMAN_BAG_KEY.getKey().getValue()) {
                if (minecraft.player != null) minecraft.player.closeContainer();
            }
        } else if (minecraft.player != null && minecraft.screen == null) {
            if (ClientSetup.TALISMAN_BAG_KEY.isDown()) {
                SkillScreen.open();
            }
        }
    }

    @SubscribeEvent
    public static void onEntityKill(LivingDeathEvent event) {
        CombatHelper.GiveCombatXP(event);
    }

    @SubscribeEvent
    public static void onAttackMob(LivingHurtEvent event) {
        CombatHelper.RangedDamageForEvent(event);
    }

    @SubscribeEvent
    public static void onAttackMob(AttackEntityEvent entityEvent) {
        CombatHelper.MeleeDamageForEvent(entityEvent);
    }

    @SubscribeEvent
    public static void onBlockBreakByPlayer(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Block target_block = event.getState().getBlock();
        BlockPos target_block_pos = event.getPos();
        Level level = player.level;
        MiningSkill miningSkill = player.getCapability(MiningSkillCapability.MINING_CAP).orElse(null);
        ForagingSkill foragingSkill = player.getCapability(ForagingSkillCapability.FORAGING_CAP).orElse(null);
        FarmingSkill farmingSkill = player.getCapability(FarmingSkillCapability.FARMING_CAP).orElse(null);
        if (!level.isClientSide && level instanceof ServerLevel) {
            SkillBreakInteraction.onBreakInteration(event, target_block, target_block_pos, player, miningSkill, foragingSkill, farmingSkill);
            if (player instanceof ServerPlayer serverPlayer) {
                LevelChunk chunk = serverPlayer.level.getChunkAt(target_block_pos);
                chunk.getCapability(SkillChunkCap.BLOCK_CAP).ifPresent(skillChunk -> {
                    skillChunk.removeData(target_block_pos);
                });
                chunk.markUnsaved();
            }
        }
    }
}
