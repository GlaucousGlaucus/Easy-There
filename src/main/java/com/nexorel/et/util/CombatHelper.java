package com.nexorel.et.util;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nexorel.et.EasyThere;
import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkill;
import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkillCapability;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkill;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkillCapability;
import com.nexorel.et.capabilities.skills.Stats.Stats;
import com.nexorel.et.capabilities.skills.Stats.StatsCapability;
import com.nexorel.et.content.Entity.damage_ind.DamageIndicatorEntity;
import com.nexorel.et.content.items.Talismans.TalismanItem;
import com.nexorel.et.content.items.talisBag.TalismanBagItem;
import com.nexorel.et.setup.ETConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
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
                    serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(C -> C.deliverDataToClient(serverPlayerEntity));
                }
            }
        }
    }

    public static void RangedDamageForEvent(LivingHurtEvent event, LivingEntity target) {
        if (event.getSource().isProjectile()) {
            DamageSource damageSource = event.getSource();
            if (damageSource.getEntity() instanceof Player attacker && damageSource.getDirectEntity() != null && target != null) {
                CombatSkill combatSkill = attacker.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
                Stats stats = attacker.getCapability(StatsCapability.STATS_CAP).orElse(null);
                Level level = attacker.level;
                double accuracy = stats.getAccuracy();
                if (level.isClientSide()) return;
                if (!(level instanceof ServerLevel)) throw new AssertionError("ServerWorld Expected");
                float damage_pt = CombatHelper.calculate_damage_PT(target, combatSkill, event.getAmount());
                Random random = new Random();
                int rand_gen = random.nextInt(10 - 1) + 1;
                boolean CritCheck = stats.canCrit() && !attacker.hasEffect(MobEffects.CONFUSION) && !attacker.hasEffect(MobEffects.BLINDNESS);
                if (CritCheck) {
                    damage_pt *= 1.5F;
                }
                damage_pt += (damage_pt * accuracy / 100) / 2;
                target.hurt(DamageSource.indirectMobAttack(damageSource.getDirectEntity(), (LivingEntity) damageSource.getEntity()), damage_pt);
                if (ETConfig.COMMON.DMG_IND_ENABLED.get()) {
                    CombatHelper.summon_damage_indicator(target, damage_pt, CritCheck, false);
                }
                event.setCanceled(true);
            }
        }
    }

    public static void MeleeDamageForEvent(AttackEntityEvent event) {
        Player attacker = event.getPlayer();
        Entity Target = event.getTarget();
        CombatSkill combatSkill = attacker.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
        Stats stats = attacker.getCapability(StatsCapability.STATS_CAP).orElse(null);
        Level world = attacker.level;
        if (world.isClientSide()) return;
        if (!(world instanceof ServerLevel)) throw new AssertionError("ServerWorld Expected");
        int combat_skill_level = combatSkill.getLevel();
        if (Target instanceof LivingEntity target) {
            float attack_strength = attacker.getAttackStrengthScale(0.5F);
            double strength = stats.getStrength();
            float final_damage = CombatHelper.calculate_dmg_M(target, attacker, combat_skill_level, attack_strength, strength);
            boolean attackCooldownCheck = ETConfig.COMMON.COMBAT_COOLDOWN.get() && (attack_strength > 0.9F);
            boolean CritCheck = attackCooldownCheck && stats.canCrit() && !attacker.onClimbable() && !attacker.isInWater() && !attacker.hasEffect(MobEffects.BLINDNESS) && !attacker.isPassenger();
            CritCheck = CritCheck && !attacker.isSprinting();
            if (CritCheck) {
                final_damage *= 1.5F;
            }
            target.hurt(DamageSource.playerAttack(attacker), final_damage);
            if (ETConfig.COMMON.DMG_IND_ENABLED.get()) {
                CombatHelper.summon_damage_indicator(target, final_damage, CritCheck, false);
            }
            event.setCanceled(true);
        }
    }

    public static void calculateAndAddStats(TickEvent.PlayerTickEvent event, ServerPlayer serverPlayerEntity) {
        Stats stats = serverPlayerEntity.getCapability(StatsCapability.STATS_CAP).orElse(null);
        MiningSkill miningSkill = serverPlayerEntity.getCapability(MiningSkillCapability.MINING_CAP).orElse(null);
        int mining_level = miningSkill.getLevel();
        double mining_skill_fortune = mining_level * 5D;
        Inventory inv = event.player.getInventory();
        double items_accuracy = 0;
        double items_agility = 0;
        double items_strength = 0;
        double items_fortune = 0;
        int items_cc = 0;

        AtomicDouble items_talis_bag_accuracy = new AtomicDouble(0);
        AtomicDouble items_talis_bag_agility = new AtomicDouble(0);
        AtomicDouble items_talis_bag_strength = new AtomicDouble(0);
        AtomicDouble items_talis_bag_fortune = new AtomicDouble(0);
        AtomicInteger items_talis_bag_cc = new AtomicInteger(0);

        for (ItemStack stack : inv.items) {
            if (stack.getItem() instanceof TalismanItem talismanItem) {
                items_accuracy += talismanItem.getAccuracy();
                items_agility += talismanItem.getAgility();
                items_strength += talismanItem.getStrength();
                items_fortune += talismanItem.getFortune();
                items_cc += talismanItem.getCc();
            } else if (stack.getItem() instanceof TalismanBagItem) {
                if (stack.getTag() != null) {
                    CompoundTag tag = stack.getTag();
                    items_talis_bag_accuracy.getAndSet(tag.getDouble("accuracy"));
                    items_talis_bag_agility.getAndSet(tag.getDouble("agility"));
                    items_talis_bag_strength.getAndSet(tag.getDouble("strength"));
                    items_talis_bag_fortune.getAndSet(tag.getDouble("fortune"));
                    items_talis_bag_cc.getAndSet(tag.getInt("cc"));
                }
            }
        }
        double final_accuracy = items_accuracy + items_talis_bag_accuracy.get();
        double final_agility = items_agility + items_talis_bag_agility.get();
        double final_strength = items_strength + items_talis_bag_strength.get();
        double final_fortune = items_fortune + items_talis_bag_fortune.get() + mining_skill_fortune;
        int final_cc = items_cc + items_talis_bag_cc.get();
        stats.setAccuracy(Mth.clamp(final_accuracy, 0, 100));
        stats.setAgility(Mth.clamp(final_agility, 0, 100));
        stats.setStrength(final_strength);
        stats.setFortune(final_fortune);
        stats.setCrit_chance(Mth.clamp(final_cc, 0, 100));
        stats.shareData(serverPlayerEntity);
    }

    public static float calculate_damage_PT(LivingEntity target, CombatSkill combatSkill, float player_dmg) {
        int combat_skill_level = combatSkill.getLevel();
        int defence = target.getArmorValue() + (target.getArmorValue() * (combat_skill_level / 20));
        return (float) ((0.015 * (combat_skill_level * combat_skill_level) * (player_dmg * player_dmg)) / (player_dmg + defence + 0.001) + 1);
    }

    public static float calculate_dmg_M(LivingEntity target, Player attacker, int combat_skill_level, float attack_strength, double strength) {
        int defence = target.getArmorValue();
        float ench_bnus = EnchantmentHelper.getDamageBonus(attacker.getMainHandItem(), target.getMobType());
        float player_damage = (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) + ench_bnus;
        float wip_dmg_2 = (float) ((0.015 * (combat_skill_level * combat_skill_level) * (player_damage * player_damage)) / (player_damage + defence + 0.001) + 1);
        float strength_bonus = (float) ((wip_dmg_2) * strength);
        float final_damage;
        if (ETConfig.COMMON.COMBAT_COOLDOWN.get()) {
            final_damage = ((wip_dmg_2 * (attack_strength * 1.25F)) / 100) * 100;
        } else {
            final_damage = (wip_dmg_2 / 100) * 100;
        }
        final_damage += strength_bonus;
        return final_damage;
    }

    public static void summon_damage_indicator(LivingEntity target, float final_damage, boolean wasCritDamage, boolean wasBlocked) {
        DamageIndicatorEntity indicator = new DamageIndicatorEntity(target.level, final_damage, wasCritDamage, target.isAlive(), wasBlocked);
        float dist = 0.2F;
        double x = -dist * Math.sin(Math.toRadians(target.getYRot())) * Math.cos(Math.toRadians(target.getXRot()));
        double z = dist * Math.cos(Math.toRadians(target.getYRot())) * Math.cos(Math.toRadians(target.getXRot()));
        Random random = target.getRandom();
        double rangeMin = -0.75;
        double rangeMax = 0.75;
        double rand_x = SomeFunctions.generateRandomDoubleBetweenRange(rangeMin, rangeMax, random);
        double rand_y = SomeFunctions.generateRandomDoubleBetweenRange(-0.5, 0.35, random);
        double rand_z = SomeFunctions.generateRandomDoubleBetweenRange(rangeMin, rangeMax, random);
        rand_x = rand_x < 0 ? Mth.clamp(rand_x, -0.75, -0.5) : Mth.clamp(rand_x, 0.5, 0.75);
        rand_z = rand_z < 0 ? Mth.clamp(rand_z, -0.75, -0.5) : Mth.clamp(rand_z, 0.5, 0.75);
        indicator.setPosRaw(target.getX() + x + rand_x, target.getY() + 1F + rand_y, target.getZ() + z + rand_z);
        target.level.addFreshEntity(indicator);
    }

    /**
     * Adds the values of all combat xps in JSON file to questionList
     *
     * @param jsonarray The Array of mobs and xp
     * @param combat_xp The map of mobs and the combat xp
     */
    public static void addCombatXpsToMap(JsonArray jsonarray, Map<EntityType<?>, Float> combat_xp) {
        for (JsonElement jsonElement : jsonarray) {
            if (jsonElement.isJsonObject()) {
                JsonObject object = jsonElement.getAsJsonObject();
                if (object == null) {
                    EasyThere.LOGGER.info("Json Object is Null");
                } else {
                    ResourceLocation mob = new ResourceLocation(GsonHelper.getAsString(object, "Mob"));
                    int level = GsonHelper.getAsInt(object, "Level");
                    if (ForgeRegistries.ENTITIES.containsKey(mob)) {
                        EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(mob);
                        combat_xp.put(entityType, CombatSkill.getCombatXPForMob(level));
                    } else {
                        EasyThere.LOGGER.error("Invalid Entity: {}", mob.toString());
                    }
                }
            }
        }
    }

    public static float getCombatXpForMob(EntityType<?> type) {
        Map<EntityType<?>, Float> combat_xp_list = EasyThere.getCombatXpManager().getAllXps();
        return combat_xp_list.get(type);
    }

}
