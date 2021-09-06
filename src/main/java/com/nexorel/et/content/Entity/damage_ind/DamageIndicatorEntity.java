package com.nexorel.et.content.Entity.damage_ind;

import com.nexorel.et.Registries.EntityInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class DamageIndicatorEntity extends Entity implements IEntityAdditionalSpawnData {

    public int age;
    private float damage = 100;
    private boolean wasCrit;
    private boolean targetAlive;
    public float t = -1;
    public float t1 = -1;
    public float t2 = -1;
    public float t3 = -1;

    public DamageIndicatorEntity(EntityType<? extends DamageIndicatorEntity> w, Level world) {
        super(w, world);
        this.noPhysics = true;
    }

    public DamageIndicatorEntity(Level world, float damage, boolean wasCrit, boolean targetAlive) {
        super(EntityInit.DMG_IND.get(), world);
        this.damage = damage;
        this.wasCrit = wasCrit;
        this.targetAlive = targetAlive;
    }

    @Override
    public void tick() {
        int finalAge = 40;
        if (this.age > finalAge) {
            this.discard();
        } else {
            this.age++;
        }
        if (this.level.isClientSide) {
            this.t1 = this.t;
            this.t = this.age > (finalAge - 6) ? ((float) finalAge - this.age) / 6 : (this.age < 6 ? ((float) this.age) / 6 : 1);
            this.t3 = this.t2;
            this.t2 = this.age > 32 && this.age <= 35 ? 0.25F : this.age > 35 ? -0.05F * (finalAge - this.age) : 0;
        }
    }

    public float getDamage() {
        return this.damage;
    }

    public boolean wasCrit() {
        return this.wasCrit;
    }

    public boolean getTargetAlive() {
        return this.targetAlive;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        this.age = nbt.getInt("age");
        this.damage = nbt.getFloat("damage");
        this.wasCrit = nbt.getBoolean("was_crit");
        this.targetAlive = nbt.getBoolean("target_alive");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("age", this.age);
        nbt.putFloat("damage", this.damage);
        nbt.putBoolean("was_crit", this.wasCrit);
        nbt.putBoolean("target_alive", this.targetAlive);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.age);
        buffer.writeFloat(this.damage);
        buffer.writeBoolean(this.wasCrit);
        buffer.writeBoolean(this.targetAlive);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.age = additionalData.readInt();
        this.damage = additionalData.readFloat();
        this.wasCrit = additionalData.readBoolean();
        this.targetAlive = additionalData.readBoolean();
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }
}
