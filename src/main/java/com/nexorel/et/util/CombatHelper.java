package com.nexorel.et.util;

import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkill;
import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkillCapability;
import com.nexorel.et.content.Entity.damage_ind.DamageIndicatorEntity;
import com.nexorel.et.content.items.Talismans.TalismanItem;
import com.nexorel.et.content.items.talisBag.TalismanBagItem;
import com.nexorel.et.setup.ETConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class CombatHelper {

    public static void GiveCombatXP(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player) {
            if (event.getSource().getEntity() != null) {
                LivingEntity target = event.getEntityLiving();
                Player player = (Player) event.getSource().getEntity();
                Level world = player.level;
                CombatSkill combatSkill = player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
                XPAssignHelper.assignCombatXP(target, combatSkill, player, world);
                ServerPlayer serverPlayerEntity = (ServerPlayer) player;
                if (!serverPlayerEntity.level.isClientSide) {
                    serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(C -> C.shareData(serverPlayerEntity));
                }
            }
        }
    }

    public static void RangedDamageForEvent(LivingHurtEvent event) {
        if (event.getSource().isProjectile()) {
            DamageSource damageSource = event.getSource();
            LivingEntity target = event.getEntityLiving();
            if (damageSource.getEntity() instanceof Player attacker && damageSource.getDirectEntity() != null && target != null) {
                CombatSkill combatSkill = attacker.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
                Level world = attacker.level;
                if (world.isClientSide()) return;
                if (!(world instanceof ServerLevel)) throw new AssertionError("ServerWorld Expected");
                float wip_dmg_2 = CombatHelper.calculate_damage_PT(target, combatSkill, event.getAmount());
                Random random = new Random();
                int rand_gen = random.nextInt(10 - 1) + 1;
                boolean flag2 = (rand_gen < 2.5) && !attacker.hasEffect(MobEffects.CONFUSION) && !attacker.hasEffect(MobEffects.BLINDNESS);
                if (flag2) {
                    wip_dmg_2 *= 1.5F;
                }
                target.hurt(DamageSource.indirectMobAttack(damageSource.getDirectEntity(), (LivingEntity) damageSource.getEntity()), wip_dmg_2);
                CombatHelper.summon_damage_indicator(target, wip_dmg_2, flag2);
                event.setCanceled(true);
            }
        }
    }

    public static void MeleeDamageForEvent(AttackEntityEvent entityEvent) {
        Player attacker = entityEvent.getPlayer();
        Entity Target = entityEvent.getTarget();
        CombatSkill combatSkill = attacker.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
        Level world = attacker.level;
        if (world.isClientSide()) return;
        if (!(world instanceof ServerLevel)) throw new AssertionError("ServerWorld Expected");
        int combat_skill_level = combatSkill.getLevel();
        if (Target instanceof LivingEntity target) {
            float attack_strength = attacker.getAttackStrengthScale(0.5F);
            float final_damage = CombatHelper.calculate_dmg_M(target, attacker, combat_skill_level, attack_strength);
            boolean flag = ETConfig.COMMON.COMBAT_COOLDOWN.get() && (attack_strength > 0.9F);
            boolean flag2 = flag && combatSkill.canCrit() && !attacker.onClimbable() && !attacker.isInWater() && !attacker.hasEffect(MobEffects.BLINDNESS) && !attacker.isPassenger();
            flag2 = flag2 && !attacker.isSprinting();
            if (flag2) {
                final_damage *= 1.5F;
            }
            target.hurt(DamageSource.playerAttack(attacker), final_damage);
            CombatHelper.summon_damage_indicator(target, final_damage, flag2);
        }
        entityEvent.setCanceled(true);
    }

    public static void calculateAndGiveCritChance(TickEvent.PlayerTickEvent event, ServerPlayer serverPlayerEntity) {
        serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> {
            Inventory inv = event.player.getInventory();
            int items_cc = inv.items.stream().map(ItemStack::getItem).filter(item -> item instanceof TalismanItem).map(item -> (TalismanItem) item).mapToInt(TalismanItem::getCc).sum();
            AtomicInteger items_talis_bag_cc = new AtomicInteger(0);
            inv.items.forEach(stack -> {
                if (stack.getItem() instanceof TalismanBagItem) {
                    if (stack.getTag() != null) {
                        items_talis_bag_cc.getAndSet(stack.getTag().getInt("cc"));
                    }
                }
            });
            int final_cc = items_cc + items_talis_bag_cc.get();
            combatSkill.setCrit_chance(Mth.clamp(final_cc, 0, 100));
            combatSkill.shareData(serverPlayerEntity);
        });
    }

    public static float calculate_damage_PT(LivingEntity target, CombatSkill combatSkill, float player_dmg) {
        int combat_skill_level = combatSkill.getLevel();
        int defence = target.getArmorValue() + (target.getArmorValue() * (combat_skill_level / 20));
        return (float) ((0.015 * (combat_skill_level * combat_skill_level) * (player_dmg * player_dmg)) / (player_dmg + defence + 0.001) + 1);
    }

    public static float calculate_dmg_M(LivingEntity target, Player attacker, int combat_skill_level, float attack_strength) {
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
        double x = -dist * Math.sin(Math.toRadians(target.getYRot())) * Math.cos(Math.toRadians(target.getXRot()));
        double z = dist * Math.cos(Math.toRadians(target.getYRot())) * Math.cos(Math.toRadians(target.getXRot()));
        Random random = new Random();
        double rangeMin = -0.75;
        double rangeMax = 0.75;
        double rand_x = rangeMin + (rangeMax - rangeMin) * random.nextDouble(); // rangeMin + (rangeMax - rangeMin) * r.nextDouble()
        double rand_y = -0.5 + (0.35 + 0.5) * random.nextDouble();
        double rand_z = rangeMin + (rangeMax - rangeMin) * random.nextDouble(); // rangeMin + (rangeMax - rangeMin) * r.nextDouble()
        rand_x = rand_x < 0 ? Mth.clamp(rand_x, -0.75, -0.5) : Mth.clamp(rand_x, 0.5, 0.75);
        rand_z = rand_z < 0 ? Mth.clamp(rand_z, -0.75, -0.5) : Mth.clamp(rand_z, 0.5, 0.75);
        indicator.setPosRaw(target.getX() + x + rand_x, target.getY() + 1F + rand_y, target.getZ() + z + rand_z);
        target.level.addFreshEntity(indicator);
    }

}
