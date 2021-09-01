package com.nexorel.et.content.Entity.damage_ind;

import com.nexorel.et.Registries.EntityInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class DamageIndicatorEntity extends Entity implements IEntityAdditionalSpawnData {

    private int age;
    private float damage = 100;
    private boolean wasCrit;
    private boolean targetAlive;

    public DamageIndicatorEntity(EntityType<? extends DamageIndicatorEntity> w, World world) {
        super(w, world);
        this.noPhysics = true;
    }

    public DamageIndicatorEntity(World world, float damage, boolean wasCrit, boolean targetAlive) {
        super(EntityInit.DMG_IND.get(), world);
        this.damage = damage;
        this.wasCrit = wasCrit;
        this.targetAlive = targetAlive;
    }

    @Override
    public void tick() {
        int finalAge = 40;
        if (this.age > finalAge) {
            this.remove();
        } else {
            this.age++;
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
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        this.age = nbt.getInt("age");
        this.damage = nbt.getFloat("damage");
        this.wasCrit = nbt.getBoolean("was_crit");
        this.targetAlive = nbt.getBoolean("target_alive");
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        nbt.putInt("age", this.age);
        nbt.putFloat("damage", this.damage);
        nbt.putBoolean("was_crit", this.wasCrit);
        nbt.putBoolean("target_alive", this.targetAlive);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(this.age);
        buffer.writeFloat(this.damage);
        buffer.writeBoolean(this.wasCrit);
        buffer.writeBoolean(this.targetAlive);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
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
