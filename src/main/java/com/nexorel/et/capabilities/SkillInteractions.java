package com.nexorel.et.capabilities;

import com.nexorel.et.content.Entity.damage_ind.DamageIndicatorEntity;
import com.nexorel.et.content.skills.SkillScreen;
import com.nexorel.et.setup.ClientSetup;
import com.nexorel.et.setup.ETConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import java.util.Map;
import java.util.Random;

public class SkillInteractions {

    @SubscribeEvent
    public static void PlayerLogInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) event.getPlayer();
        if (!serverPlayerEntity.level.isClientSide) {
            serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> combatSkill.shareData(serverPlayerEntity));
        }
    }

    @SubscribeEvent
    public static void PlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) event.getPlayer();
        if (!serverPlayerEntity.level.isClientSide) {
            serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> combatSkill.shareData(serverPlayerEntity));
        }
    }

    @SubscribeEvent
    public static void PlayerDimensionChangeEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) event.getPlayer();
        if (!serverPlayerEntity.level.isClientSide) {
            serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> combatSkill.shareData(serverPlayerEntity));
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
        if (event.getSource().getEntity() instanceof PlayerEntity) {
            if (event.getSource().getEntity() != null) {
                LivingEntity target = event.getEntityLiving();
                PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
                World world = player.level;
                CombatSkill combatSkill = player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
                Map<EntityType<?>, Float> xps = CombatSkill.getCombatXp();
                float xp;
                if (xps.get(target.getType()) != null) {
                    xp = xps.get(target.getType());
                    int OL = combatSkill.getLevel();
                    combatSkill.addXp(xp);
                    int NL = combatSkill.getLevel();
                    if (NL - OL > 0) {
                        player.sendMessage(new StringTextComponent(TextFormatting.AQUA + "\u263A" + "Skill Level Up: Combat Level: " + OL + " \u2192 " + NL), player.getUUID());
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundCategory.PLAYERS, 3F, 0.24F);
                    }
                } else {
                    int OL = combatSkill.getLevel();
                    int mob_level = CombatSkill.getMobLevel(target);
                    float unknown_mob_xp = CombatSkill.getCombatXPForMob(mob_level);
                    combatSkill.addXp(unknown_mob_xp);
                    int NL = combatSkill.getLevel();
                    if (NL - OL > 0) {
                        player.sendMessage(new StringTextComponent(TextFormatting.AQUA + "§kHELLO" + "Skill Level Up: Combat Level: " + OL + " \u2192 " + NL), player.getUUID());
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundCategory.PLAYERS, 3F, 0.24F);
                    }
                }
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 3F, 2F);
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                if (!serverPlayerEntity.level.isClientSide) {
                    serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(C -> C.shareData(serverPlayerEntity));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAttackMob(LivingHurtEvent event) {
        if (event.getSource().isProjectile()) {
            DamageSource damageSource = event.getSource();
            LivingEntity target = event.getEntityLiving();
            if (damageSource.getEntity() instanceof PlayerEntity && damageSource.getDirectEntity() != null && target != null) {
                PlayerEntity attacker = (PlayerEntity) damageSource.getEntity();
                CombatSkill combatSkill = attacker.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
                World world = attacker.level;
                if (world.isClientSide()) return;
                if (!(world instanceof ServerWorld)) throw new AssertionError("ServerWorld Expected");
                int combat_skill_level = combatSkill.getLevel();
                int defence = target.getArmorValue() + (target.getArmorValue() * (combat_skill_level / 20));
                float player_damage = event.getAmount();
                float wip_dmg_2 = (float) ((0.015 * (combat_skill_level * combat_skill_level) * (player_damage * player_damage)) / (player_damage + defence + 0.001) + 1);
                Random random = new Random();
                int rand_gen = random.nextInt(10 - 1) + 1;
                boolean flag2 = (rand_gen < 2.5) && !attacker.hasEffect(Effects.CONFUSION) && !attacker.hasEffect(Effects.BLINDNESS);
                if (flag2) {
                    wip_dmg_2 *= 1.5F;
                }
                target.hurt(DamageSource.indirectMobAttack(damageSource.getDirectEntity(), (LivingEntity) damageSource.getEntity()), wip_dmg_2);
                DamageIndicatorEntity indicator = new DamageIndicatorEntity(target.level, wip_dmg_2, flag2);
                float dist = 0.2F;
                double x = -dist * Math.sin(Math.toRadians(target.yRot)) * Math.cos(Math.toRadians(target.xRot));
                double z = dist * Math.cos(Math.toRadians(target.yRot)) * Math.cos(Math.toRadians(target.xRot));
                indicator.setPosRaw(target.getX() + x, target.getY() + 1F, target.getZ() + z);
                target.level.addFreshEntity(indicator);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onAttackMob(AttackEntityEvent entityEvent) {
        PlayerEntity attacker = entityEvent.getPlayer();
        Entity Target = entityEvent.getTarget();
        // TODO: Add a config option to disable Attack Cooldown
        CombatSkill combatSkill = attacker.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
        World world = attacker.level;
        if (world.isClientSide()) return;
        if (!(world instanceof ServerWorld)) throw new AssertionError("ServerWorld Expected");
        int combat_skill_level = combatSkill.getLevel();
        if (Target instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) Target;
            int defence = target.getArmorValue();
            float ench_bnus = EnchantmentHelper.getDamageBonus(attacker.getMainHandItem(), target.getMobType());
            float player_damage = (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) + ench_bnus;
            float wip_dmg_2 = (float) ((0.015 * (combat_skill_level * combat_skill_level) * (player_damage * player_damage)) / (player_damage + defence + 0.001) + 1);
            float attack_strength = attacker.getAttackStrengthScale(0.5F);
            float final_damage;
            if (ETConfig.COMBAT_COOLDOWN.get()) {
                final_damage = wip_dmg_2 * (attack_strength * 1.25F);
            } else {
                final_damage = wip_dmg_2;
            }
            boolean flag = attack_strength > 0.9F;
            boolean flag2 = flag && attacker.fallDistance > 0.0F && !attacker.isOnGround() && !attacker.onClimbable() && !attacker.isInWater() && !attacker.hasEffect(Effects.BLINDNESS) && !attacker.isPassenger();
            flag2 = flag2 && !attacker.isSprinting();
            if (flag2) {
                final_damage *= 1.5F;
            }
            // TODO: Add crit chance
            target.hurt(DamageSource.playerAttack(attacker), final_damage);
            DamageIndicatorEntity indicator = new DamageIndicatorEntity(target.level, final_damage, flag2);
            float dist = 0.2F;
            double x = -dist * Math.sin(Math.toRadians(target.yRot)) * Math.cos(Math.toRadians(target.xRot));
            double z = dist * Math.cos(Math.toRadians(target.yRot)) * Math.cos(Math.toRadians(target.xRot));
            indicator.setPosRaw(target.getX() + x, target.getY() + 1F, target.getZ() + z);
            target.level.addFreshEntity(indicator);
        }
        entityEvent.setCanceled(true);
    }
}
