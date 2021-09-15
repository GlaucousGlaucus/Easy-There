package com.nexorel.et.content.blocks.dungeon.traps.ArrowTrap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class CustomTrapArrow extends Arrow {

    private float fac;

    public CustomTrapArrow(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
    }

    public CustomTrapArrow(Level level, double x, double y, double z, float fac) {
        super(level, x, y, z);
        this.fac = fac;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof Player player) {
            float amt = this.fac <= 0F ? 0.25F : this.fac;
            player.hurt(DamageSource.arrow(this, this).bypassArmor().bypassMagic(), (player.getMaxHealth() + player.getAbsorptionAmount()) * amt);
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        int seconds = 5;
        int duration = 20 * seconds;
        MobEffectInstance blindnes = new MobEffectInstance(MobEffects.BLINDNESS, duration, 1);
        MobEffectInstance weakness = new MobEffectInstance(MobEffects.WEAKNESS, duration, 0);
        MobEffectInstance fatigue = new MobEffectInstance(MobEffects.DIG_SLOWDOWN, duration, 1);
        MobEffectInstance slowness = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 1);
        entity.addEffect(blindnes, this.getEffectSource());
        entity.addEffect(weakness, this.getEffectSource());
        entity.addEffect(fatigue, this.getEffectSource());
        entity.addEffect(slowness, this.getEffectSource());
        entity.removeEffect(MobEffects.REGENERATION);
        entity.removeEffect(MobEffects.DAMAGE_RESISTANCE);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.discard();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("fac", this.fac);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("fac")) {
            this.fac = tag.getFloat("fac");
        }
    }
}
