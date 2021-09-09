package com.nexorel.et.capabilities;

import com.nexorel.et.capabilities.CombatSkill.CombatSkill;
import com.nexorel.et.capabilities.CombatSkill.CombatSkillCapability;
import com.nexorel.et.capabilities.FarmingSkill.FarmingSkill;
import com.nexorel.et.capabilities.FarmingSkill.FarmingSkillCapability;
import com.nexorel.et.capabilities.ForagingSkill.ForagingSkill;
import com.nexorel.et.capabilities.ForagingSkill.ForagingSkillCapability;
import com.nexorel.et.capabilities.MiningSkill.MiningSkill;
import com.nexorel.et.capabilities.MiningSkill.MiningSkillCapability;
import com.nexorel.et.content.Entity.damage_ind.DamageIndicatorEntity;
import com.nexorel.et.content.items.Talismans.TalismanItem;
import com.nexorel.et.content.items.talisBag.TalismanBagItem;
import com.nexorel.et.content.skills.SkillScreen;
import com.nexorel.et.setup.ClientSetup;
import com.nexorel.et.setup.ETConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ModInteractions {

    @SubscribeEvent
    public static void PlayerTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer serverPlayerEntity) {
            if (!serverPlayerEntity.level.isClientSide) {
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
                serverPlayerEntity.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(foragingSkill -> {
                    int dig_speed_str = (int) Math.min(Math.floor(foragingSkill.getLevel() * 0.15), 3);
                    if (foragingSkill.getLevel() > 5) {
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
            serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> {
                combatSkill.shareData(serverPlayerEntity);
            });
            serverPlayerEntity.getCapability(MiningSkillCapability.MINING_CAP).ifPresent(miningSkill -> {
                miningSkill.shareData(serverPlayerEntity);
            });
            serverPlayerEntity.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(foragingSkill -> {
                foragingSkill.shareData(serverPlayerEntity);
            });
            serverPlayerEntity.getCapability(FarmingSkillCapability.FARMING_CAP).ifPresent(farmingSkill -> {
                farmingSkill.shareData(serverPlayerEntity);
            });
        }
    }

    @SubscribeEvent
    public static void PlayerRespawnEvent(PlayerEvent.Clone event) {
        ServerPlayer serverPlayerEntity_original = (ServerPlayer) event.getOriginal();
        ServerPlayer serverPlayerEntity_new = (ServerPlayer) event.getPlayer();
        if (!serverPlayerEntity_original.level.isClientSide) {
            serverPlayerEntity_original.reviveCaps();
            if (event.isWasDeath()) {
                serverPlayerEntity_original.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> {
                    serverPlayerEntity_new.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill_new -> {
                        combatSkill_new.setXp(combatSkill.getXp());
                        combatSkill_new.setCrit_chance(combatSkill.getCrit_chance());
                        combatSkill_new.shareData(serverPlayerEntity_new);
                    });
                });
                serverPlayerEntity_original.getCapability(MiningSkillCapability.MINING_CAP).ifPresent(miningSkill -> {
                    serverPlayerEntity_new.getCapability(MiningSkillCapability.MINING_CAP).ifPresent(miningSkill_new -> {
                        miningSkill_new.setXp(miningSkill.getXp());
                        miningSkill.shareData(serverPlayerEntity_new);
                    });
                });
                serverPlayerEntity_original.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(foragingSkill -> {
                    serverPlayerEntity_new.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(foragingSkill1 -> {
                        foragingSkill1.setXp(foragingSkill.getXp());
                        foragingSkill.shareData(serverPlayerEntity_new);
                    });
                });
                serverPlayerEntity_original.getCapability(FarmingSkillCapability.FARMING_CAP).ifPresent(farmingSkill -> {
                    serverPlayerEntity_new.getCapability(FarmingSkillCapability.FARMING_CAP).ifPresent(farmingSkill1 -> {
                        farmingSkill1.setXp(farmingSkill.getXp());
                        farmingSkill.shareData(serverPlayerEntity_new);
                    });
                });
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
        if (event.getSource().getEntity() instanceof Player) {
            if (event.getSource().getEntity() != null) {
                LivingEntity target = event.getEntityLiving();
                Player player = (Player) event.getSource().getEntity();
                Level world = player.level;
                CombatSkill combatSkill = player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
                assignCombatXP(target, combatSkill, player, world);
                ServerPlayer serverPlayerEntity = (ServerPlayer) player;
                if (!serverPlayerEntity.level.isClientSide) {
                    serverPlayerEntity.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(C -> C.shareData(serverPlayerEntity));
                }
            }
        }
    }

    private static void assignCombatXP(LivingEntity target, CombatSkill combatSkill, Player player, Level world) {
        Map<EntityType<?>, Float> xps = CombatSkill.getCombatXp();
        float xp;
        if (xps.get(target.getType()) != null) {
            xp = xps.get(target.getType());
            int OL = combatSkill.getLevel();
            combatSkill.addXp(xp);
            player.displayClientMessage(new TextComponent(ChatFormatting.AQUA + "Combat +" + xp), true);
            int NL = combatSkill.getLevel();
            if (NL - OL > 0) {
                player.sendMessage(new TextComponent(ChatFormatting.AQUA + "\u263A" + "Skill Level Up: Combat Level: " + OL + " \u2192 " + NL), player.getUUID());
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 3F, 0.24F);
            }
        } else {
            int OL = combatSkill.getLevel();
            int mob_level = CombatSkill.getMobLevel(target);
            float unknown_mob_xp = CombatSkill.getCombatXPForMob(mob_level);
            combatSkill.addXp(unknown_mob_xp);
            player.displayClientMessage(new TextComponent(ChatFormatting.AQUA + "Combat +" + unknown_mob_xp), true);
            int NL = combatSkill.getLevel();
            if (NL - OL > 0) {
                player.sendMessage(new TextComponent(ChatFormatting.AQUA + "§kHELLO" + "Skill Level Up: Combat Level: " + OL + " \u2192 " + NL), player.getUUID());
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 3F, 0.24F);
            }
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 3F, 2F);
    }

    @SubscribeEvent
    public static void onAttackMob(LivingHurtEvent event) {
        if (event.getSource().isProjectile()) {
            DamageSource damageSource = event.getSource();
            LivingEntity target = event.getEntityLiving();
            if (damageSource.getEntity() instanceof Player attacker && damageSource.getDirectEntity() != null && target != null) {
                CombatSkill combatSkill = attacker.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
                Level world = attacker.level;
                if (world.isClientSide()) return;
                if (!(world instanceof ServerLevel)) throw new AssertionError("ServerWorld Expected");
                float wip_dmg_2 = calculate_damage_PT(target, combatSkill, event.getAmount());
                Random random = new Random();
                int rand_gen = random.nextInt(10 - 1) + 1;
                boolean flag2 = (rand_gen < 2.5) && !attacker.hasEffect(MobEffects.CONFUSION) && !attacker.hasEffect(MobEffects.BLINDNESS);
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
        Player attacker = entityEvent.getPlayer();
        Entity Target = entityEvent.getTarget();
        CombatSkill combatSkill = attacker.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
        Level world = attacker.level;
        if (world.isClientSide()) return;
        if (!(world instanceof ServerLevel)) throw new AssertionError("ServerWorld Expected");
        int combat_skill_level = combatSkill.getLevel();
        if (Target instanceof LivingEntity target) {
            float attack_strength = attacker.getAttackStrengthScale(0.5F);
            float final_damage = calculate_dmg_M(target, attacker, combat_skill_level, attack_strength);
            boolean flag = ETConfig.COMMON.COMBAT_COOLDOWN.get() && (attack_strength > 0.9F);
            boolean flag2 = flag && combatSkill.canCrit() && !attacker.onClimbable() && !attacker.isInWater() && !attacker.hasEffect(MobEffects.BLINDNESS) && !attacker.isPassenger();
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

    // Foraging Skill
    private static void assignForagingXP(Block target, ForagingSkill foragingSkill, Player player, Level level) {
        if (player.isCreative()) return;
        Map<Block, Float> xps = ForagingSkill.getForagingXp();
        float xp;
        if (xps.get(target) != null) {
            xp = xps.get(target);
            xp = (float) (xp + (xp * (foragingSkill.getLevel() * 0.05)));
            int OL = foragingSkill.getLevel();
            foragingSkill.addXp(xp);
            player.displayClientMessage(new TextComponent(ChatFormatting.AQUA + "Foraging +" + xp), true);
            int NL = foragingSkill.getLevel();
            if (NL - OL > 0) {
                player.sendMessage(new TextComponent(ChatFormatting.AQUA + "\u263A" + "Skill Level Up: Foraging Level: " + OL + " \u2192 " + NL), player.getUUID());
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 3F, 0.24F);
            }
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 3F, 2F);
    }

    // Farming SKill int

    private static void assignFarmingXP(Block target, FarmingSkill farmingSkill, Player player, Level world) {
        if (player.isCreative()) return;
        Map<Block, Float> xps = FarmingSkill.getFarmingXp();
        float xp;
        if (xps.get(target) != null) {
            xp = xps.get(target);
            xp = (float) (xp + (xp * (farmingSkill.getLevel() * 0.05)));
            int OL = farmingSkill.getLevel();
            farmingSkill.addXp(xp);
            player.displayClientMessage(new TextComponent(ChatFormatting.AQUA + "Farming +" + xp), true);
            int NL = farmingSkill.getLevel();
            if (NL - OL > 0) {
                player.sendMessage(new TextComponent(ChatFormatting.AQUA + "\u263A" + "Skill Level Up: Farming Level: " + OL + " \u2192 " + NL), player.getUUID());
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 3F, 0.24F);
            }
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 3F, 2F);
    }

    // Mining Skill Interactions

    @SubscribeEvent
    public static void onBlockBreakByPlayer(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Block target_block = event.getState().getBlock();
        Level level = player.level;
        MiningSkill miningSkill = player.getCapability(MiningSkillCapability.MINING_CAP).orElse(null);
        ForagingSkill foragingSkill = player.getCapability(ForagingSkillCapability.FORAGING_CAP).orElse(null);
        FarmingSkill farmingSkill = player.getCapability(FarmingSkillCapability.FARMING_CAP).orElse(null);
        if (!player.level.isClientSide && player.level instanceof ServerLevel) {
            if (MiningSkill.getMiningXp().containsKey(target_block)) {
                event.setExpToDrop(event.getExpToDrop() + event.getExpToDrop() * miningSkill.getLevel());
                int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player);
                if (i == 0) {
                    assignMiningXP(event.getState().getBlock(), miningSkill, player, player.level);
                    if (player instanceof ServerPlayer serverPlayer) {
                        if (!serverPlayer.level.isClientSide) {
                            serverPlayer.getCapability(MiningSkillCapability.MINING_CAP).ifPresent(miningSkill1 -> miningSkill.shareData(serverPlayer));
                        }
                    }
                }
            } else if (ForagingSkill.getForagingXp().containsKey(target_block)) { /* Foraging Part */
                BlockPos target_block_pos = event.getPos();
                if (target_block_pos.getY() >= 62 && player.level.dimension().equals(Level.OVERWORLD)) {
                    assignForagingXP(target_block, foragingSkill, player, player.level);
                    if (player instanceof ServerPlayer serverPlayer) {
                        if (!serverPlayer.level.isClientSide) {
                            serverPlayer.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(skill -> foragingSkill.shareData(serverPlayer));
                        }
                    }
                }
            } else if (FarmingSkill.getFarmingXp().containsKey(target_block)) {
                assignFarmingXP(target_block, farmingSkill, player, player.level);
                if (player instanceof ServerPlayer serverPlayer) {
                    if (!serverPlayer.level.isClientSide) {
                        serverPlayer.getCapability(FarmingSkillCapability.FARMING_CAP).ifPresent(skill -> farmingSkill.shareData(serverPlayer));
                    }
                }
            }
        }
    }

    private static void assignMiningXP(Block target, MiningSkill miningSkill, Player player, Level world) {
        if (player.isCreative()) return;
        Map<Block, Float> xps = MiningSkill.getMiningXp();
        float xp;
        if (xps.get(target) != null) {
            xp = xps.get(target);
            xp = (float) (xp + (xp * (miningSkill.getLevel() * 0.05)));
            int OL = miningSkill.getLevel();
            miningSkill.addXp(xp);
            player.displayClientMessage(new TextComponent(ChatFormatting.AQUA + "Mining +" + xp), true);
            int NL = miningSkill.getLevel();
            if (NL - OL > 0) {
                player.sendMessage(new TextComponent(ChatFormatting.AQUA + "\u263A" + "Skill Level Up: Mining Level: " + OL + " \u2192 " + NL), player.getUUID());
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 3F, 0.24F);
            }
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 3F, 2F);
    }
    //--------------------------
}
