package com.nexorel.et.content.Entity.projectile.aura_blast;

import com.nexorel.et.Registries.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class AuraBlast extends AbstractHurtingProjectile {

    public AuraBlast(EntityType<? extends AuraBlast> entityType, Level world) {
        super(entityType, world);
    }

    @OnlyIn(Dist.CLIENT)
    public AuraBlast(Level world, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(EntityInit.AURA_BLAST.get(), x, y, z, accelX, accelY, accelZ, world);
    }

    public AuraBlast(Level world, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(EntityInit.AURA_BLAST.get(), shooter, accelX, accelY, accelZ, world);
    }

    @Override
    protected float getInertia() {
        return 0.98F;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (result.getType() == HitResult.Type.ENTITY) {
            Entity e = ((EntityHitResult) result).getEntity();
            if (e instanceof LivingEntity) {
                ((LivingEntity) e).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
                e.hurt(DamageSource.MAGIC, 2.0F);
            }
        } else if (result.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult) result).getBlockPos();
            this.level.removeBlock(pos, false);
        }
        this.discard();
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.DRAGON_BREATH;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
