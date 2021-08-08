package com.nexorel.et.content.Entity.projectile.aura_blast;

import com.nexorel.et.Registries.EntityInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class AuraBlast extends DamagingProjectileEntity {

    public AuraBlast(EntityType<? extends AuraBlast> entityType, World world) {
        super(entityType, world);
    }

    @OnlyIn(Dist.CLIENT)
    public AuraBlast(World world, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(EntityInit.AURA_BLAST.get(), x, y, z, accelX, accelY, accelZ, world);
    }

    public AuraBlast(World world, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(EntityInit.AURA_BLAST.get(), shooter, accelX, accelY, accelZ, world);
    }

    @Override
    protected float getInertia() {
        return 0.98F;
    }

    @Override
    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity e = ((EntityRayTraceResult) result).getEntity();
            if (e instanceof LivingEntity) {
                ((LivingEntity) e).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 2));
                e.hurt(DamageSource.MAGIC, 2.0F);
            }
        } else if (result.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos pos = ((BlockRayTraceResult) result).getBlockPos();
            this.level.removeBlock(pos, false);
        }
        this.remove();
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
    protected IParticleData getTrailParticle() {
        return ParticleTypes.DRAGON_BREATH;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
