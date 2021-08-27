package com.nexorel.et.capabilities;

import com.nexorel.et.EasyThere;
import com.nexorel.et.content.skills.SkillScreen;
import com.nexorel.et.setup.ClientSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class SkillInteractions {

    @SubscribeEvent
    public static void PlayerLogInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) event.getPlayer();
        if (!serverPlayerEntity.level.isClientSide) {
            serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> combatSkill.syncData(serverPlayerEntity));
        }
    }

    @SubscribeEvent
    public static void PlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) event.getPlayer();
        if (!serverPlayerEntity.level.isClientSide) {
            serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> combatSkill.syncData(serverPlayerEntity));
        }
    }

    @SubscribeEvent
    public static void PlayerDimensionChangeEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) event.getPlayer();
        if (!serverPlayerEntity.level.isClientSide) {
            serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> combatSkill.syncData(serverPlayerEntity));
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
            ClientPlayerEntity clientPlayerEntity = minecraft.player;
            if (ClientSetup.TALISMAN_BAG_KEY.isDown()) {
                SkillScreen.open();
            }
        }
    }

    @SubscribeEvent
    public static void onEntityKill(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof PlayerEntity) {
            if (event.getSource().getEntity() != null) {
                LivingEntity target = event.getEntityLiving();
                PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
                World world = player.level;
                if (world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) world;
                    Vector3d epos = target.position();
                    serverWorld.sendParticles(ParticleTypes.EXPLOSION, epos.x, epos.y, epos.z, 20, 0.5, 0.5, 0.5, 0);
                }
                CombatSkill combatSkill = player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
                combatSkill.addXp(10000);
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                if (!serverPlayerEntity.level.isClientSide) {
                    serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(C -> C.syncData(serverPlayerEntity));
                }
                EasyThere.LOGGER.info("----------------");
                EasyThere.LOGGER.info(combatSkill.getLevel());
                EasyThere.LOGGER.info("----------------");
            }
        }
    }

    @SubscribeEvent
    public static void onAttackMob(AttackEntityEvent entityEvent) {
        PlayerEntity player = entityEvent.getPlayer();
        CombatSkill combatSkill = player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
        if (combatSkill == null) {
            EasyThere.LOGGER.error("COMBAT SKILL IS NULL");
            return;
        }
        if (combatSkill.getLevel() == 0) {
            EasyThere.LOGGER.info("ADDED XP");
        }
        EasyThere.LOGGER.info(combatSkill.getLevel());
        World world = player.level;
        if (world.isClientSide()) return;
        if (!(world instanceof ServerWorld)) throw new AssertionError("ServerWorld Expected");
        ServerWorld serverWorld = (ServerWorld) world;
        int combat_skill_level = combatSkill.getLevel();
        Entity target = entityEvent.getTarget();
        if (target instanceof LivingEntity) {
            LivingEntity livingTarget = (LivingEntity) target;
            int defence = ((LivingEntity) target).getArmorValue();
            float player_strength = player.getAttackStrengthScale(0.5F);
            float player_damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float wip_dmg_2 = (float) ((0.015 * (combat_skill_level * combat_skill_level) * (player_damage * player_damage)) / (player_damage + defence + 0.001) + 1);
            livingTarget.hurt(DamageSource.playerAttack(player), wip_dmg_2);
            /*EasyThere.LOGGER.info("DMG: ");
            EasyThere.LOGGER.info(wip_dmg_2);
            EasyThere.LOGGER.info("Skill: ");
            EasyThere.LOGGER.info(combat_skill_level);
            EasyThere.LOGGER.info(CombatSkill.calculateFullTargetXp(combat_skill_level));
            EasyThere.LOGGER.info("Defence: ");
            EasyThere.LOGGER.info(defence);
            EasyThere.LOGGER.info("Strength: ");
            EasyThere.LOGGER.info(player_strength);*/
            entityEvent.setCanceled(true);
        }
    }

}
