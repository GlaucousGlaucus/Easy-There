package com.nexorel.et.content.Entity.projectile.aura_blast;

import com.nexorel.et.Registries.BlockInit;
import com.nexorel.et.Registries.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import java.util.Set;

import static com.nexorel.et.Reference.MODID;

public class AuraBlast extends AbstractHurtingProjectile {

    public static final Tag.Named<Block> BLACKLISTED_BLOCKS = BlockTags.createOptional(new ResourceLocation(MODID, "blacklisted_blocks"), Set.of(
            () -> Blocks.BEDROCK,
            () -> Blocks.NETHERITE_BLOCK,
            BlockInit.DUNGEON_PIT_TRAP,
            BlockInit.DUNGEON_CHISELED_POLISHED_BLACKSTONE,
            BlockInit.DUNGEON_CRACKED_POLISHED_BLACKSTONE_BRICKS,
            BlockInit.DUNGEON_GILDED_BLACKSTONE,
            BlockInit.DUNGEON_POLISHED_BLACKSTONE,
            BlockInit.DUNGEON_POLISHED_BLACKSTONE_BRICKS,
            BlockInit.DUNGEON_STONE
    ));

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
            BlockPos pos = ((BlockHitResult) result).getBlockPos().above();
            AreaEffectCloud areaEffectCloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
            Entity owner = this.getOwner();
            if (owner instanceof LivingEntity living) {
                areaEffectCloud.setOwner(living);
            }
            areaEffectCloud.setRadius(3.0F);
            areaEffectCloud.setRadiusOnUse(-0.5F);
            areaEffectCloud.setWaitTime(10);
            areaEffectCloud.setRadiusPerTick(-areaEffectCloud.getRadius() / (float) areaEffectCloud.getDuration());
            areaEffectCloud.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 3));
            areaEffectCloud.setFixedColor(0xc04aff);
            this.level.addFreshEntity(areaEffectCloud);
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
