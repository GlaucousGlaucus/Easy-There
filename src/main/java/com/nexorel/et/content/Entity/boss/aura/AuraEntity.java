package com.nexorel.et.content.Entity.boss.aura;

import com.nexorel.et.content.Entity.projectile.aura_blast.AuraBlast;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.nexorel.et.Reference.MOD_ID;

public class AuraEntity extends MonsterEntity implements IRangedAttackMob {

    private final ServerBossInfo bossEvent = new ServerBossInfo(new StringTextComponent(TextFormatting.DARK_AQUA + "AURA"), BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS);

    private int shield_time = 200;
    boolean flag = true;
    private Entity e;

    RangedAttackGoal rag = new RangedAttackGoal(this, 1.0D, 40, 20.0F);
    MinionsGoal shieldAndMinionsGoal = new MinionsGoal(this, this.shield_time);
    AuraImpactGoal auraImpactGoal = new AuraImpactGoal(this);

    public AuraEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
        this.setPersistenceRequired();
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomWalkingGoal(this, 0.6));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true/**check sight*/));
        super.registerGoals();
    }

    public static AttributeModifierMap.MutableAttribute prepareAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.MOVEMENT_SPEED, (double) 0.6F)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.ARMOR, 44.0D);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)
                || source == DamageSource.FALL
        ) {
            return false;
        } else if (source != DamageSource.DROWN && !(source.getEntity() instanceof AuraEntity)) {
            return super.hurt(source, amount);
        } else {
            return false;
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayerEntity entity) {
        super.startSeenByPlayer(entity);
        this.bossEvent.addPlayer(entity);
    }

    @Override
    public void tick() {
        if (this.isAlive()) {

           // Damages player if it is within a certain range of blocks
            World EW = this.level;
            if (EW instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) EW;
                Vector3d epos = AuraEntity.this.position();
                AxisAlignedBB b = new AxisAlignedBB(epos.x - 5, epos.y - 5, epos.z - 5, epos.x + 5, epos.y + 5, epos.z + 5);
                List<Entity> entities = serverWorld.getEntities(e, b);
                for (Entity entity : entities) {
                    if (entity instanceof PlayerEntity) {
                        entity.hurt(DamageSource.MAGIC, 1.0F);
                    }
                }
            }

            // Manages the phase of the boss fight
            if (this.getHealth() > 0.5 * this.getMaxHealth()) {
                this.goalSelector.addGoal(2, rag);
            } else if (this.getHealth() < 0.5 * this.getMaxHealth()) {
                this.goalSelector.removeGoal(rag);
                this.goalSelector.addGoal(2, shieldAndMinionsGoal);
            } else if (this.getHealth() <= 0.35 * this.getMaxHealth()) {
                this.goalSelector.removeGoal(shieldAndMinionsGoal);
                this.goalSelector.addGoal(2, auraImpactGoal);
            }
        }
        super.tick();
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (!this.level.isClientSide && flag) {
            BlockPos pos = this.blockPosition();
            BlockState state = Blocks.CHEST.defaultBlockState();
            this.level.setBlockAndUpdate(pos, state);
            TileEntity chest = this.level.getBlockEntity(pos);
            if (chest instanceof ChestTileEntity) {
                ((ChestTileEntity) chest).setLootTable(new ResourceLocation(MOD_ID, "chests/aura_chest"), random.nextLong());
            }
            flag = false;
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayerEntity entity) {
        super.stopSeenByPlayer(entity);
        this.bossEvent.removePlayer(entity);
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void performRangedAttack(LivingEntity target, float v) {
        int chance = random.nextInt(10 - 1 + 1) + 1;
        if (chance < 11) {
            if (target instanceof PlayerEntity) {
                double d0 = this.getHeadX((int) v);
                double d1 = this.getHeadY((int) v);
                double d2 = this.getHeadZ((int) v);
                double d3 = target.getX() - d0;
                double d4 = target.getY() - d1;
                double d5 = target.getZ() - d2;
                AuraBlast auraBlast = new AuraBlast(this.level, this, d3, d4, d5);
                auraBlast.setOwner(this);
                auraBlast.setPosRaw(d0, d1, d2);
                this.level.addFreshEntity(auraBlast);
            }
        } else {
            if (target instanceof PlayerEntity) {
                double x = target.getX();
                double y = target.getY();
                double z = target.getZ();
                LightningBoltEntity le = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, this.level);
                le.setPos(x, y, z);
                this.level.addFreshEntity(le);
                target.hurt(DamageSource.GENERIC, 2);
                target.addEffect(new EffectInstance(Effects.WEAKNESS, 10, 2));
            }
        }
    }

    private double getHeadX(int p_82214_1_) {
        if (p_82214_1_ <= 0) {
            return this.getX();
        } else {
            float f = (this.yBodyRot + (float) (180 * (p_82214_1_ - 1))) * ((float) Math.PI / 180F);
            float f1 = MathHelper.cos(f);
            return this.getX() + (double) f1 * 1.3D;
        }
    }

    private double getHeadY(int p_82208_1_) {
        return p_82208_1_ <= 0 ? this.getY() + 3.0D : this.getY() + 2.2D;
    }

    private double getHeadZ(int p_82213_1_) {
        if (p_82213_1_ <= 0) {
            return this.getZ();
        } else {
            float f = (this.yBodyRot + (float) (180 * (p_82213_1_ - 1))) * ((float) Math.PI / 180F);
            float f1 = MathHelper.sin(f);
            return this.getZ() + (double) f1 * 1.3D;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("shield_time", this.shield_time);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        this.shield_time = nbt.getInt("shield_time");
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable ITextComponent component) {
        super.setCustomName(component);
        this.bossEvent.setName(this.getDisplayName());
    }

    class MinionsGoal extends Goal {

        AuraEntity ae;
        boolean flag = true;
        int shieldTime;

        public MinionsGoal(AuraEntity ae, int shieldTime) {
            this.ae = ae;
            this.shieldTime = shieldTime;
        }

        @Override
        public boolean canUse() {
            return this.ae.getHealth() <= this.ae.getMaxHealth() * 0.5
                    && this.ae.getMaxHealth() * 0.25 < this.ae.getHealth()
                    && this.flag;
        }

        @Override
        public void tick() {
            super.tick();
            World world = this.ae.level;
            if (!this.ae.level.isClientSide) {
                int i = 0;
                do {
                    ServerWorld serverworld = (ServerWorld) AuraEntity.this.level;
                    DifficultyInstance difficultyinstance = serverworld.getCurrentDifficultyAt(this.ae.blockPosition());
                    SkeletonEntity minion = createMinion(difficultyinstance, this.ae);
                    serverworld.addFreshEntity(minion);
                    if (i == 10) {
                        this.flag = false;
                    } else {
                        i++;
                    }
                } while (i < 11 && this.flag);
            }
        }

        private SkeletonEntity createMinion(DifficultyInstance instance, AuraEntity entity) {
            SkeletonEntity minion = EntityType.SKELETON.create(entity.level);
            minion.finalizeSpawn((ServerWorld) entity.level, instance, SpawnReason.REINFORCEMENT, null, null);
            minion.setPos(entity.getX(), entity.getY(), entity.getZ());
            minion.invulnerableTime = 100;
            minion.isInvulnerableTo(DamageSource.WITHER);
            minion.isInvulnerableTo(DamageSource.CRAMMING);
            minion.isInvulnerableTo(DamageSource.LIGHTNING_BOLT);
            minion.isInvulnerableTo(DamageSource.LAVA);
            minion.setPersistenceRequired();
            return minion;
        }
    }

    class AuraImpactGoal extends Goal {

        AuraEntity ae;
        int uses = 100;

        public AuraImpactGoal(AuraEntity auraEntity) {
            this.ae = auraEntity;
        }


        @Override
        public void tick() {
            if (uses > 0) {
                uses--;
            } else if (uses == 0 && !AuraEntity.this.level.isClientSide) {
                LivingEntity target = this.ae.getTarget();
                if (target != null) {
                    this.ae.moveTo(target.getX(), target.getY(), target.getZ());
                    Random rand = new Random();
                    int a = rand.nextInt(10 - 1 + 1) + 1;
                    if (a < 8) {
                        target.hurt(DamageSource.MAGIC, 3.0F);
                        this.ae.heal(6.0F);
                    } else {
                        target.addEffect(new EffectInstance(Effects.BLINDNESS, 100, 100));
                        target.addEffect(new EffectInstance(Effects.HUNGER, 100, 100));
                        target.addEffect(new EffectInstance(Effects.POISON, 100, 100));
                    }
                    World world = AuraEntity.this.level;
                    if (world instanceof ServerWorld) {
                        ServerWorld serverWorld = (ServerWorld) world;
                        Vector3d epos = AuraEntity.this.position();
                        serverWorld.sendParticles(ParticleTypes.EXPLOSION, epos.x, epos.y, epos.z, 20, 0.5, 0.5, 0.5, 0);
                    }
                }
                uses = 100;
            }
        }

        @Override
        public boolean canUse() {
            return this.ae.getHealth() <= this.ae.getMaxHealth() * 0.25;
        }
    }
}
