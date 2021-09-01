package com.nexorel.et.content.items.Weapons;

import com.nexorel.et.EasyThere;
import com.nexorel.et.capabilities.CombatSkill;
import com.nexorel.et.capabilities.CombatSkillCapability;
import com.nexorel.et.capabilities.SkillInteractions;
import com.nexorel.et.content.items.IWandTiers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

import static com.nexorel.et.EasyThere.EASY_THERE;

public class AuraWand extends Item {
    private Entity e;
    private final IItemTier tier;

    public AuraWand() {
        super(new Item.Properties().tab(EASY_THERE).defaultDurability(IWandTiers.aura_scale.getUses()));
        this.tier = IWandTiers.aura_scale;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        RayTraceResult result = player.pick(15.0, 2, false);
        ItemStack itemInHand = player.getItemInHand(hand);
        CompoundNBT tag = itemInHand.getTag();
        if (!world.isClientSide) {
            if (!player.isCrouching()) {
                if (tag != null && tag.contains("test_mode")) {
                    if (tag.getInt("test_mode") == 1) {
                        AuraBlast(result, player, world, itemInHand);
                    } else if (tag.getInt("test_mode") == 2) {
                        player.displayClientMessage(new StringTextComponent("Mode 2"), true);
                    } else {
                        player.displayClientMessage(new StringTextComponent(TextFormatting.RED + "PLEASE SELECT MODE"), true);
                    }
                } else {
                    player.displayClientMessage(new StringTextComponent(TextFormatting.RED + "PLEASE SELECT MODE"), true);
                }
            } else {
                if (tag == null) {
                    tag = new CompoundNBT();
                    itemInHand.setTag(tag);
                }
                if (tag.contains("test_mode")) {
                    EasyThere.LOGGER.info("TEST_MODE: " + tag.getInt("test_mode"));
                    if (tag.getInt("test_mode") == 1) {
                        tag.putInt("test_mode", 2);
                    } else {
                        tag.putInt("test_mode", 1);
                    }
                    EasyThere.LOGGER.info("TEST_MODE: " + tag.getInt("test_mode"));
                } else {
                    tag.putInt("test_mode", 1);
                }
            }
        }
        return ActionResult.success(itemInHand);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    private void AuraBlast(RayTraceResult result, PlayerEntity player, World world, ItemStack itemInHand) {
        if (result.getType() == RayTraceResult.Type.BLOCK) {
            Vector3d location = result.getLocation();
            player.moveTo(location.x, location.y, location.z);
            AxisAlignedBB b = new AxisAlignedBB(player.getX() - 5, player.getY() - 5, player.getZ() - 5, player.getX() + 5, player.getY() + 5, player.getZ() + 5);
            List<Entity> entities = world.getEntities(e, b);
            for (Entity target : entities) {
                if (target instanceof LivingEntity && !(target instanceof PlayerEntity)) {
                    CombatSkill combatSkill = player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
                    float final_dmg = SkillInteractions.calculate_damage_PT((LivingEntity) target, combatSkill, this.tier.getAttackDamageBonus());
                    boolean flag = combatSkill.canCrit();
                    final_dmg = flag ? (float) (final_dmg * 1.5) : final_dmg;
                    target.hurt(DamageSource.MAGIC, final_dmg);
                    SkillInteractions.summon_damage_indicator((LivingEntity) target, final_dmg, flag);
                    EasyThere.LOGGER.info(target);
                }
            }
        } else {

            int dist = 5;
            double x = -dist * Math.sin(Math.toRadians(player.yRot)) * Math.cos(Math.toRadians(player.xRot));
            double y = -dist * Math.sin(Math.toRadians(player.xRot));
            double z = dist * Math.cos(Math.toRadians(player.yRot)) * Math.cos(Math.toRadians(player.xRot));
            player.moveTo(player.getX() + x, player.getY() + y + 0.5, player.getZ() + z);
            AxisAlignedBB b = new AxisAlignedBB(player.getX() - 5, player.getY() - 5, player.getZ() - 5, player.getX() + 5, player.getY() + 5, player.getZ() + 5);
            List<Entity> entities = world.getEntities(e, b);
            for (Entity target : entities) {
                if (target instanceof LivingEntity && !(target instanceof PlayerEntity)) {
                    CombatSkill combatSkill = player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
                    float final_dmg = SkillInteractions.calculate_damage_PT((LivingEntity) target, combatSkill, this.tier.getAttackDamageBonus());
                    boolean flag = combatSkill.canCrit();
                    final_dmg = flag ? (float) (final_dmg * 1.5) : final_dmg;
                    target.hurt(DamageSource.MAGIC, final_dmg);
                    SkillInteractions.summon_damage_indicator((LivingEntity) target, final_dmg, flag);
                    EasyThere.LOGGER.info(target);
                }
            }
        }
        ServerWorld serverWorld = (ServerWorld) world;
        Vector3d epos = player.position();
        serverWorld.sendParticles(ParticleTypes.EXPLOSION, epos.x, epos.y, epos.z, 20, 0.5, 0.5, 0.5, 0);
        itemInHand.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> textComponents, ITooltipFlag flag) {
        super.appendHoverText(stack, world, textComponents, flag);
        CompoundNBT tag = stack.getTag();
        String[] modes = new String[2];
        modes[0] = "Aura Blast";
        modes[1] = "Smeth Else";
        if (tag != null) {
            int mode_no = tag.getInt("test_mode");
            textComponents.add(new StringTextComponent(TextFormatting.GOLD + String.valueOf(mode_no == 1 || mode_no == 2 ? modes[mode_no - 1] : "Select Mode")));
        } else {
            textComponents.add(new StringTextComponent(TextFormatting.GOLD + "RIP"));
        }
        if (Screen.hasShiftDown()) {
            textComponents.add(new StringTextComponent(TextFormatting.GOLD + "Wanted to make the hyperion ability from Hypixel Skyblock. This is my version"));
        }
    }
}
