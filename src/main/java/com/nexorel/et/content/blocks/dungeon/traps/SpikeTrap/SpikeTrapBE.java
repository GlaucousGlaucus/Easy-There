package com.nexorel.et.content.blocks.dungeon.traps.SpikeTrap;

import com.nexorel.et.Registries.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.*;

public class SpikeTrapBE extends BlockEntity {

    private int cooldown;
    private boolean isVenomous;
    private int duration;
    private int strength;

    public SpikeTrapBE(BlockPos pos, BlockState state) {
        super(BlockEntityInit.SPIKE_TRAP_TILE.get(), pos, state);
    }

    public void tickServer() {
        if (cooldown > 0) {
            cooldown--;
            setChanged();

        }
        if (level != null) {
            boolean powered = this.getBlockState().getValue(BlockStateProperties.POWERED);
            List<Player> l = getPlayers(level);
            if (cooldown <= 0 && !l.isEmpty()) {
                cooldown = 200;
                activateTrap(level, l);
                setChanged();
            }
            if (powered != (cooldown == 0)) {
                changeCooldown(level, cooldown);
                BlockState blockState = level.getBlockState(this.getBlockPos());
                blockState = blockState.setValue(BlockStateProperties.POWERED, cooldown == 0);
                level.setBlock(this.getBlockPos(), blockState,
                        Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
            }
        }

    }

    private void changeCooldown(Level level, int cooldown) {
        Set<BlockPos> posSet = getNeighbours(level.getBlockState(this.getBlockPos()).getBlock(), level, this.getBlockPos());
        posSet.remove(this.getBlockPos());
        for (BlockPos pos : posSet) {
            if (level.getBlockEntity(pos) instanceof SpikeTrapBE be) {
                be.setCooldown(cooldown);
            }
        }
    }

    private List<Player> getPlayers(Level level) {
        BlockState state = this.getBlockState();
        Direction direction = state.getValue(BlockStateProperties.FACING);
        double x = direction.getStepX() * 0.25D;
        double y = direction.getStepY() * 0.25D;
        double z = direction.getStepZ() * 0.25D;
        BlockPos pos = this.getBlockPos();
        AABB aabb = new AABB(pos).expandTowards(x, y, z);
        return level.getEntitiesOfClass(Player.class, aabb);
    }

    private void activateTrap(Level level, List<Player> players) {
        if (!players.isEmpty() && level instanceof ServerLevel) {
            for (Player player : players) {
                float damage = getDamage(player);
                player.hurt(DamageSource.GENERIC.bypassMagic(), damage);
                if (isVenomous) {
                    player.addEffect(new MobEffectInstance(MobEffects.POISON, duration, strength));
                }
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
            }
        }
    }

    private float getDamage(Player player) {
        if (level != null) {
            Difficulty difficulty = level.getDifficulty();
            float hard_damage = (player.getAbsorptionAmount() + player.getMaxHealth()) * 0.25F;
            if (difficulty == Difficulty.PEACEFUL) return 1F;
            if (difficulty == Difficulty.EASY) return 1.5F;
            if (difficulty == Difficulty.NORMAL) return 2F;
            if (difficulty == Difficulty.HARD) return hard_damage;
        }
        return 1F;
    }

    /**
     * Credit: MrCrayfish
     *
     * @see <a href="https://github.com/MrCrayfish/Enchantable/blob/1.16.X/src/main/java/com/mrcrayfish/enchantable/enchantment/OreEaterEnchantment.java">MrCrayfish - Enchantable</a>
     */
    private Set<BlockPos> getNeighbours(@Nullable Block targetBlock, Level world, BlockPos pos) {
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> explored = new LinkedHashSet<>();
        queue.add(pos);
        explored.add(pos);
        while (!queue.isEmpty()) {
            BlockPos entry = queue.remove();
            addMatchingBlock(targetBlock, world, entry.north(), queue, explored);
            addMatchingBlock(targetBlock, world, entry.east(), queue, explored);
            addMatchingBlock(targetBlock, world, entry.south(), queue, explored);
            addMatchingBlock(targetBlock, world, entry.west(), queue, explored);
            addMatchingBlock(targetBlock, world, entry.above(), queue, explored);
            addMatchingBlock(targetBlock, world, entry.below(), queue, explored);
            explored.add(entry);
        }
        return explored;
    }

    private void addMatchingBlock(@Nullable Block targetBlock, Level world, BlockPos pos, Queue<BlockPos> queue, Set<BlockPos> explored) {
        BlockState state = world.getBlockState(pos);
        if (state.isAir()) {
            return;
        }
        if (targetBlock == null || state.getBlock() == targetBlock) {
            if (!explored.contains(pos)) {
                queue.offer(pos);
            }
        }
    }

    public void setCooldown(int value) {
        this.cooldown = value;
        setChanged();
    }

    @Override
    public void load(CompoundTag tag) {
        cooldown = tag.getInt("cooldown");
        isVenomous = tag.getBoolean("is_venomous");
        duration = tag.getInt("duration");
        strength = tag.getInt("strength");
        super.load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("cooldown", cooldown);
        tag.putBoolean("is_venomous", isVenomous);
        tag.putInt("duration", duration);
        tag.putInt("strength", strength);
        return super.save(tag);
    }

}
