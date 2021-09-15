package com.nexorel.et.content.blocks.dungeon.traps.ArrowTrap;

import com.nexorel.et.Registries.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class ArrowTrapBE extends BlockEntity {

    private int cooldown;
    private double range;
    private float arrow_fac;

    public ArrowTrapBE(BlockPos pos, BlockState state) {
        super(BlockEntityInit.ARROW_TRAP_TILE.get(), pos, state);
    }

    public void tickServer() {
        if (cooldown > 0) {
            cooldown--;
            setChanged();
        }
        if (cooldown <= 0 && activateTrap()) {
            cooldown = 200;
            if (level instanceof ServerLevel serverLevel) {
                Set<BlockPos> posSet = getNeighbours(level.getBlockState(this.getBlockPos()).getBlock(), level, this.getBlockPos());
                for (BlockPos pos : posSet) {
                    dispenseFrom(serverLevel, pos);
                }
                changeCooldown(level, cooldown);
            }
            setChanged();
        }
    }

    private boolean activateTrap() {
        BlockState state = this.getBlockState();
        Direction direction = state.getValue(BlockStateProperties.FACING);
        double fac = range <= 0 ? 2D : range;
        double x = direction.getStepX() * fac;
        double y = direction.getStepY() * fac;
        double z = direction.getStepZ() * fac;
        BlockPos pos = this.getBlockPos();
        AABB aabb = new AABB(pos.offset(0, -1, 0)).expandTowards(x, y, z);
        return level != null && !level.getEntitiesOfClass(Player.class, aabb).isEmpty();
    }

    protected void dispenseFrom(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Direction direction = state.getValue(BlockStateProperties.FACING);
        double x = pos.getX() + (double) direction.getStepX() + 0.5;
        double y = pos.getY() + (double) direction.getStepY() + 0.25;
        double z = pos.getZ() + (double) direction.getStepZ() + 0.5;
        CustomTrapArrow arrow = new CustomTrapArrow(level, x, y, z, this.arrow_fac);
        arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        float power = 1F;
        float uncertainity = 6.0F;
        arrow.shoot(direction.getStepX(), direction.getStepY() + 0.1F, direction.getStepZ(), power, uncertainity);
        level.addFreshEntity(arrow);
    }

    private void changeCooldown(Level level, int cooldown) {
        Set<BlockPos> posSet = getNeighbours(level.getBlockState(this.getBlockPos()).getBlock(), level, this.getBlockPos());
        posSet.remove(this.getBlockPos());
        for (BlockPos pos : posSet) {
            if (level.getBlockEntity(pos) instanceof ArrowTrapBE be) {
                be.setCooldown(cooldown);
            }
        }
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

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int val) {
        this.cooldown = val;
        setChanged();
    }

    @Override
    public void load(CompoundTag tag) {
        cooldown = tag.getInt("cooldown");
        range = tag.getDouble("range");
        arrow_fac = tag.getFloat("arrow_fac");
        super.load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("cooldown", cooldown);
        tag.putDouble("range", range);
        tag.putFloat("arrow_fac", arrow_fac);
        return super.save(tag);
    }
}
