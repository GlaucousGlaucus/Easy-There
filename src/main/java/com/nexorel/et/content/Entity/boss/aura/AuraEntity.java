package com.nexorel.et.content.Entity.boss.aura;

import com.nexorel.et.content.Entity.projectile.aura_blast.AuraBlast;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.nexorel.et.Reference.MOD_ID;

public class AuraEntity extends Monster implements RangedAttackMob {

    private final ServerBossEvent bossEvent = new ServerBossEvent(new TextComponent(ChatFormatting.DARK_AQUA + "AURA"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);

    private int shield_time = 200;
    boolean flag = true;
    private Entity e;

    public AuraEntity(EntityType<? extends Monster> type, Level world) {
        super(type, world);
        this.setPersistenceRequired();
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.0D, 40, 20.0F) {
            @Override
            public void stop() {
                super.stop();
                AuraEntity.this.setAggressive(false);
            }

            @Override
            public void start() {
                super.start();
                AuraEntity.this.setAggressive(true);
            }
        });
        this.goalSelector.addGoal(2, new MinionsGoal(this, this.shield_time));
        this.goalSelector.addGoal(1, new AuraImpactGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true/**check sight*/));
        super.registerGoals();
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Monster.createMonsterAttributes()
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
    public void startSeenByPlayer(ServerPlayer entity) {
        super.startSeenByPlayer(entity);
        this.bossEvent.addPlayer(entity);
    }


    @Override
    public void tick() {
        if (this.isAlive()) {

            // Damages player if it is within a certain range of blocks
            Level EW = this.level;
            if (EW instanceof ServerLevel) {
                ServerLevel serverWorld = (ServerLevel) EW;
                Vec3 epos = AuraEntity.this.position();
                AABB b = new AABB(epos.x - 5, epos.y - 5, epos.z - 5, epos.x + 5, epos.y + 5, epos.z + 5);
                List<Entity> entities = serverWorld.getEntities(e, b);
                for (Entity entity : entities) {
                    if (entity instanceof Player) {
                        entity.hurt(DamageSource.MAGIC, 1.0F);
                    }
                }
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
            BlockEntity chest = this.level.getBlockEntity(pos);
            if (chest instanceof ChestBlockEntity) {
                ((ChestBlockEntity) chest).setLootTable(new ResourceLocation(MOD_ID, "chests/aura_chest"), random.nextLong());
            }
            flag = false;
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer entity) {
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
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        int chance = random.nextInt(10 - 1 + 1) + 1;
        if (chance < 11) {
            if (target instanceof Player) {
                double d0 = this.getHeadX((int) distanceFactor);
                double d1 = this.getHeadY((int) distanceFactor);
                double d2 = this.getHeadZ((int) distanceFactor);
                double d3 = target.getX() - d0;
                double d4 = target.getY() - d1;
                double d5 = target.getZ() - d2;
                AuraBlast auraBlast = new AuraBlast(this.level, this, d3, d4, d5);
                auraBlast.setOwner(this);
                auraBlast.setPosRaw(d0, d1 + 1, d2);
                this.level.addFreshEntity(auraBlast);
            }
        } else {
            if (target instanceof Player) {
                double x = target.getX();
                double y = target.getY();
                double z = target.getZ();
                LightningBolt le = new LightningBolt(EntityType.LIGHTNING_BOLT, this.level);
                le.setPos(x, y, z);
                this.level.addFreshEntity(le);
                target.hurt(DamageSource.GENERIC, 2);
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10, 2));
            }
        }
    }

    private double getHeadX(int p_82214_1_) {
        if (p_82214_1_ <= 0) {
            return this.getX();
        } else {
            float f = (this.yBodyRot + (float) (180 * (p_82214_1_ - 1))) * ((float) Math.PI / 180F);
            float f1 = Mth.cos(f);
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
            float f1 = Mth.sin(f);
            return this.getZ() + (double) f1 * 1.3D;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("shield_time", this.shield_time);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.shield_time = nbt.getInt("shield_time");
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Component component) {
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
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            super.tick();
            if (!this.ae.level.isClientSide) {
                int i = 0;
                do {
                    ServerLevel serverworld = (ServerLevel) AuraEntity.this.level;
                    DifficultyInstance difficultyinstance = serverworld.getCurrentDifficultyAt(this.ae.blockPosition());
                    Skeleton minion = createMinion(difficultyinstance, this.ae);
                    serverworld.addFreshEntity(minion);
                    if (i == 10) {
                        this.flag = false;
                    } else {
                        i++;
                    }
                } while (i < 11 && this.flag);
            }
        }

        private Skeleton createMinion(DifficultyInstance instance, AuraEntity entity) {
            Skeleton minion = EntityType.SKELETON.create(entity.level);
            if (minion != null) {
                minion.finalizeSpawn((ServerLevel) entity.level, instance, MobSpawnType.REINFORCEMENT, null, null);
                minion.setPos(entity.getX(), entity.getY(), entity.getZ());
                minion.isInvulnerableTo(DamageSource.WITHER);
                minion.isInvulnerableTo(DamageSource.CRAMMING);
                minion.isInvulnerableTo(DamageSource.LIGHTNING_BOLT);
                minion.isInvulnerableTo(DamageSource.LAVA);
                minion.isInvulnerableTo(DamageSource.IN_FIRE);
                minion.isInvulnerableTo(DamageSource.ON_FIRE);
                minion.setPersistenceRequired();
            }
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
            super.tick();
            if (!this.ae.level.isClientSide) {
                if (uses > 0) {
                    uses--;
                } else if (uses == 0) {
                    LivingEntity target = this.ae.getTarget();
                    if (target != null) {
                        this.ae.moveTo(target.getX(), target.getY(), target.getZ());
                        Random rand = new Random();
                        int a = rand.nextInt(10 - 1 + 1) + 1;
                        if (a < 8) {
                            target.hurt(DamageSource.MAGIC, 10.0F);
                            this.ae.heal(6.0F);
                        } else {
                            target.hurt(DamageSource.MAGIC, 5.0F);
                            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 1));
                            target.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 1));
                            target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
                        }
                        Level world = AuraEntity.this.level;
                        if (world instanceof ServerLevel) {
                            ServerLevel serverWorld = (ServerLevel) world;
                            Vec3 epos = AuraEntity.this.position();
                            serverWorld.sendParticles(ParticleTypes.EXPLOSION, epos.x, epos.y, epos.z, 20, 0.5, 0.5, 0.5, 0);
                        }
                    }
                    uses = 100;
                }
            }
        }

        @Override
        public boolean canUse() {
            return this.ae.getHealth() <= (this.ae.getMaxHealth() * 0.40);
        }
    }
}
