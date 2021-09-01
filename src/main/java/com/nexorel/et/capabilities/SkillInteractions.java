package com.nexorel.et.capabilities;

import com.nexorel.et.EasyThere;
import com.nexorel.et.content.Entity.damage_ind.DamageIndicatorEntity;
import com.nexorel.et.content.items.Talismans.TalismanItem;
import com.nexorel.et.content.items.talisBag.TalismanBagItem;
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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SkillInteractions {

    /*public class MyClass {

    private static int cc = 0;

    public static void main(String args[]) {
        int[] m = new int[5];
        for (int t = 0; t < 5; t++) {
            m[t] = t * 10;
        }
      for (int i = 0; i < 10; i++) {
          AtomicInteger a = new AtomicInteger(1);
          for (int t = 0; t < 5; t++) {
            a.addAndGet(m[t]);
          }
          cc = a.get();
      }
    //   System.out.println("A = " + a);
      System.out.println("CC = " + cc);
    }
}*/

    @SubscribeEvent
    public static void PlayerTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) event.player;
            if (!serverPlayerEntity.level.isClientSide) {
                serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> {
                    PlayerInventory inv = event.player.inventory;
                    int items_cc = inv.items.stream().map(ItemStack::getItem).filter(item -> item instanceof TalismanItem).map(item -> (TalismanItem) item).mapToInt(TalismanItem::getCc).sum();
                    AtomicInteger items_talis_bag_cc = new AtomicInteger(0);
                    inv.items.forEach(stack -> {
                        if (stack.getItem() instanceof TalismanBagItem) {
                            if (stack.getTag() != null) {
                                items_talis_bag_cc.getAndSet(stack.getTag().getInt("cc"));
                            }
                        }
                    });
//                    int items_talis_bag_cc = inv.items.stream().map(ItemStack::getItem).filter(item -> item instanceof TalismanBagItem).map(item -> (TalismanBagItem) item).mapToInt(TalismanBagItem::getOverallCC).sum();
                    int final_cc = items_cc + items_talis_bag_cc.get();
                    combatSkill.setCrit_chance(MathHelper.clamp(final_cc, 0, 100));
                    combatSkill.shareData(serverPlayerEntity);
                });
            }
        }
    }

    @SubscribeEvent
    public static void PlayerLogInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) event.getPlayer();
        if (!serverPlayerEntity.level.isClientSide) {
            serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> {
                combatSkill.shareData(serverPlayerEntity);
            });
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
                assignCombatXP(target, combatSkill, player, world);
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                if (!serverPlayerEntity.level.isClientSide) {
                    serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(C -> C.shareData(serverPlayerEntity));
                }
            }
        }
    }

    private static void assignCombatXP(LivingEntity target, CombatSkill combatSkill, PlayerEntity player, World world) {
        Map<EntityType<?>, Float> xps = CombatSkill.getCombatXp();
        float xp;
        if (xps.get(target.getType()) != null) {
            xp = xps.get(target.getType());
            int OL = combatSkill.getLevel();
            combatSkill.addXp(xp);
            player.displayClientMessage(new StringTextComponent(TextFormatting.AQUA + "Combat +" + xp), true);
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
            player.displayClientMessage(new StringTextComponent(TextFormatting.AQUA + "Combat +" + unknown_mob_xp), true);
            int NL = combatSkill.getLevel();
            if (NL - OL > 0) {
                player.sendMessage(new StringTextComponent(TextFormatting.AQUA + "§kHELLO" + "Skill Level Up: Combat Level: " + OL + " \u2192 " + NL), player.getUUID());
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundCategory.PLAYERS, 3F, 0.24F);
            }
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 3F, 2F);
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
                float wip_dmg_2 = calculate_damage_PT(target, combatSkill, event.getAmount());
                Random random = new Random();
                int rand_gen = random.nextInt(10 - 1) + 1;
                boolean flag2 = (rand_gen < 2.5) && !attacker.hasEffect(Effects.CONFUSION) && !attacker.hasEffect(Effects.BLINDNESS);
                if (flag2) {
                    wip_dmg_2 *= 1.5F;
                }
                target.hurt(DamageSource.indirectMobAttack(damageSource.getDirectEntity(), (LivingEntity) damageSource.getEntity()), wip_dmg_2);
                summon_damage_indicator(target, wip_dmg_2, flag2);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onAttackMob(AttackEntityEvent entityEvent) {
        PlayerEntity attacker = entityEvent.getPlayer();
        Entity Target = entityEvent.getTarget();
        CombatSkill combatSkill = attacker.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
        World world = attacker.level;
        if (world.isClientSide()) return;
        if (!(world instanceof ServerWorld)) throw new AssertionError("ServerWorld Expected");
        int combat_skill_level = combatSkill.getLevel();
        if (Target instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) Target;
            float attack_strength = attacker.getAttackStrengthScale(0.5F);
            float final_damage = calculate_dmg_M(target, attacker, combat_skill_level, attack_strength);
            boolean flag = ETConfig.COMMON.COMBAT_COOLDOWN.get() && (attack_strength > 0.9F);
            boolean flag2 = flag && combatSkill.canCrit() && !attacker.onClimbable() && !attacker.isInWater() && !attacker.hasEffect(Effects.BLINDNESS) && !attacker.isPassenger();
            flag2 = flag2 && !attacker.isSprinting();
            if (flag2) {
                final_damage *= 1.5F;
            }
            target.hurt(DamageSource.playerAttack(attacker), final_damage);
            summon_damage_indicator(target, final_damage, flag2);
        }
        entityEvent.setCanceled(true);
    }

    public static float calculate_damage_PT(LivingEntity target, CombatSkill combatSkill, float player_dmg) {
        int combat_skill_level = combatSkill.getLevel();
        int defence = target.getArmorValue() + (target.getArmorValue() * (combat_skill_level / 20));
        return (float) ((0.015 * (combat_skill_level * combat_skill_level) * (player_dmg * player_dmg)) / (player_dmg + defence + 0.001) + 1);
    }

    public static float calculate_dmg_M(LivingEntity target, PlayerEntity attacker, int combat_skill_level, float attack_strength) {
        int defence = target.getArmorValue();
        float ench_bnus = EnchantmentHelper.getDamageBonus(attacker.getMainHandItem(), target.getMobType());
        float player_damage = (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) + ench_bnus;
        float wip_dmg_2 = (float) ((0.015 * (combat_skill_level * combat_skill_level) * (player_damage * player_damage)) / (player_damage + defence + 0.001) + 1);
        float final_damage;
        if (ETConfig.COMMON.COMBAT_COOLDOWN.get()) {
            final_damage = ((wip_dmg_2 * (attack_strength * 1.25F)) / 100) * 100;
        } else {
            final_damage = (wip_dmg_2 / 100) * 100;
        }
        return final_damage;
    }

    public static void summon_damage_indicator(LivingEntity target, float final_damage, boolean flag2) {
        DamageIndicatorEntity indicator = new DamageIndicatorEntity(target.level, final_damage, flag2, target.isAlive());
        float dist = 0.2F;
        double x = -dist * Math.sin(Math.toRadians(target.yRot)) * Math.cos(Math.toRadians(target.xRot));
        double z = dist * Math.cos(Math.toRadians(target.yRot)) * Math.cos(Math.toRadians(target.xRot));
        Random random = new Random();
        double rangeMin = -0.75;
        double rangeMax = 0.75;
        double rand_x = rangeMin + (rangeMax - rangeMin) * random.nextDouble(); // rangeMin + (rangeMax - rangeMin) * r.nextDouble()
        double rand_y = -0.5 + (0.35 + 0.5) * random.nextDouble();
        double rand_z = rangeMin + (rangeMax - rangeMin) * random.nextDouble(); // rangeMin + (rangeMax - rangeMin) * r.nextDouble()
        rand_x = rand_x < 0 ? MathHelper.clamp(rand_x, -0.75, -0.5) : MathHelper.clamp(rand_x, 0.5, 0.75);
        rand_z = rand_z < 0 ? MathHelper.clamp(rand_z, -0.75, -0.5) : MathHelper.clamp(rand_z, 0.5, 0.75);
        indicator.setPosRaw(target.getX() + x + rand_x, target.getY() + 1F + rand_y, target.getZ() + z + rand_z);
        EasyThere.LOGGER.info(final_damage);
        target.level.addFreshEntity(indicator);
    }
}
