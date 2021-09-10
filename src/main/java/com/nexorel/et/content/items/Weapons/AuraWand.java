package com.nexorel.et.content.items.Weapons;

import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkill;
import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkillCapability;
import com.nexorel.et.content.items.IWandTiers;
import com.nexorel.et.util.CombatHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

import static com.nexorel.et.EasyThere.EASY_THERE;

public class AuraWand extends Item {
    private Entity e;
    private final Tier tier;

    public AuraWand() {
        super(new Item.Properties().tab(EASY_THERE).defaultDurability(IWandTiers.aura_scale.getUses()));
        this.tier = IWandTiers.aura_scale;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        HitResult result = player.pick(15.0, 2, false);
        ItemStack itemInHand = player.getItemInHand(hand);
        CompoundTag tag = itemInHand.getTag();
        if (!world.isClientSide) {
            if (!player.isCrouching()) {
                if (tag != null && tag.contains("test_mode")) {
                    if (tag.getInt("test_mode") == 1) {
                        AuraBlast(result, player, world, itemInHand);
                    } else if (tag.getInt("test_mode") == 2) {
                        player.displayClientMessage(new TextComponent("Mode 2"), true);
                    } else {
                        player.displayClientMessage(new TextComponent(ChatFormatting.RED + "PLEASE SELECT MODE"), true);
                    }
                } else {
                    player.displayClientMessage(new TextComponent(ChatFormatting.RED + "PLEASE SELECT MODE"), true);
                }
            } else {
                if (tag == null) {
                    tag = new CompoundTag();
                    itemInHand.setTag(tag);
                }
                if (tag.contains("test_mode")) {
                    if (tag.getInt("test_mode") == 1) {
                        tag.putInt("test_mode", 2);
                    } else {
                        tag.putInt("test_mode", 1);
                    }
                } else {
                    tag.putInt("test_mode", 1);
                }
            }
        }
        return InteractionResultHolder.success(itemInHand);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    private void AuraBlast(HitResult result, Player player, Level world, ItemStack itemInHand) {
        if (result.getType() == HitResult.Type.BLOCK) {
            Vec3 location = result.getLocation();
            player.moveTo(location.x, location.y, location.z);
            AABB b = new AABB(player.getX() - 5, player.getY() - 5, player.getZ() - 5, player.getX() + 5, player.getY() + 5, player.getZ() + 5);
            List<Entity> entities = world.getEntities(e, b);
            for (Entity target : entities) {
                if (target instanceof LivingEntity && !(target instanceof Player)) {
                    CombatSkill combatSkill = player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
                    float final_dmg = CombatHelper.calculate_damage_PT((LivingEntity) target, combatSkill, this.tier.getAttackDamageBonus());
                    boolean flag = combatSkill.canCrit();
                    final_dmg = flag ? (float) (final_dmg * 1.5) : final_dmg;
                    target.hurt(DamageSource.MAGIC, final_dmg);
                    CombatHelper.summon_damage_indicator((LivingEntity) target, final_dmg, flag);
                }
            }
        } else {

            int dist = 5;
            double x = -dist * Math.sin(Math.toRadians(player.getYRot())) * Math.cos(Math.toRadians(player.getXRot()));
            double y = -dist * Math.sin(Math.toRadians(player.getXRot()));
            double z = dist * Math.cos(Math.toRadians(player.getYRot())) * Math.cos(Math.toRadians(player.getXRot()));
            player.moveTo(player.getX() + x, player.getY() + y + 0.5, player.getZ() + z);
            AABB b = new AABB(player.getX() - 5, player.getY() - 5, player.getZ() - 5, player.getX() + 5, player.getY() + 5, player.getZ() + 5);
            List<Entity> entities = world.getEntities(e, b);
            for (Entity target : entities) {
                if (target instanceof LivingEntity && !(target instanceof Player)) {
                    CombatSkill combatSkill = player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
                    float final_dmg = CombatHelper.calculate_damage_PT((LivingEntity) target, combatSkill, this.tier.getAttackDamageBonus());
                    boolean flag = combatSkill.canCrit();
                    final_dmg = flag ? (float) (final_dmg * 1.5) : final_dmg;
                    target.hurt(DamageSource.MAGIC, final_dmg);
                    CombatHelper.summon_damage_indicator((LivingEntity) target, final_dmg, flag);
                }
            }
        }
        ServerLevel serverWorld = (ServerLevel) world;
        Vec3 epos = player.position();
        serverWorld.sendParticles(ParticleTypes.EXPLOSION, epos.x, epos.y, epos.z, 20, 0.5, 0.5, 0.5, 0);
        itemInHand.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> textComponents, TooltipFlag flag) {
        super.appendHoverText(stack, world, textComponents, flag);
        CompoundTag tag = stack.getTag();
        String[] modes = new String[2];
        modes[0] = "Aura Blast";
        modes[1] = "Smeth Else";
        if (tag != null) {
            int mode_no = tag.getInt("test_mode");
            textComponents.add(new TextComponent(ChatFormatting.GOLD + String.valueOf(mode_no == 1 || mode_no == 2 ? modes[mode_no - 1] : "Select Mode")));
        } else {
            textComponents.add(new TextComponent(ChatFormatting.GOLD + "RIP"));
        }
        if (Screen.hasShiftDown()) {
            textComponents.add(new TextComponent(ChatFormatting.GOLD + "Wanted to make the hyperion ability from Hypixel Skyblock. This is my version"));
        }
    }
}
