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
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class SkillInteractions {

    /*@SubscribeEvent
    public static void testMethodSmth(PlayerEvent.PlayerLoggedInEvent event) {
        event.getPlayer();
    }*/

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
    public static void onAttackMob(AttackEntityEvent entityEvent) {
        PlayerEntity player = entityEvent.getPlayer();
        CombatSkill combatSkill = player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
        if (combatSkill == null) {
            EasyThere.LOGGER.error("COMBAT SKILL IS NULL");
            return;
        }
        if (combatSkill.getLevel() == 0) {
            combatSkill.addXp(1100);
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
            EasyThere.LOGGER.info(player_strength);
            float player_damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
//            float final_damage = combat_skill_level * player_damage * player_damage / player_damage + defence;
            float wip_dmg = ((float) ((0.015 * (combat_skill_level ^ 2)) * (player_damage * player_damage)) / (player_damage + defence) + 1);
            livingTarget.hurt(DamageSource.playerAttack(player), wip_dmg);
            entityEvent.setCanceled(true);
        }
    }

}
