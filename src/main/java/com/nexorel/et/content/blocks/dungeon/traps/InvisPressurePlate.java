package com.nexorel.et.content.blocks.dungeon.traps;

import com.nexorel.et.content.blocks.dungeon.DungeonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;

import java.util.Random;

public class InvisPressurePlate extends DungeonBlock {

    public InvisPressurePlate() {
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.POWERED, false));
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos pos, Random random) {
        int i = this.getSignalForState(blockState);
        if (i > 0) {
            this.checkPressed(serverLevel, pos, blockState, i);
        }
    }

    protected void checkPressed(Level level, BlockPos pos, BlockState state, int str) {
        int i = this.getSignalStrength(level, pos);
        boolean flag1 = i > 0;
        if (str != i) {
            BlockState blockstate = this.setSignalForState(state, i);
            level.setBlock(pos, blockstate, 2);
            this.updateNeighbours(level, pos);
            level.setBlocksDirty(pos, state, blockstate);
        }

        if (flag1) {
            level.getBlockTicks().scheduleTick(new BlockPos(pos), this, this.getPressedTime());
        }
    }

    protected int getPressedTime() {
        return 20;
    }

    private int getSignalStrength(Level level, BlockPos pos) {
        AABB aabb = new AABB(pos).expandTowards(0, 0.25, 0);
        return level.getEntitiesOfClass(Player.class, aabb).isEmpty() ? 0 : 15;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof Player) {
            int i = this.getSignalForState(state);
            if (i == 0) {
                this.checkPressed(level, pos, state, i);
            }
        }
    }

    protected void updateNeighbours(Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.below(), this);
    }

    protected int getSignalForState(BlockState state) {
        return state.getValue(BlockStateProperties.POWERED) ? 15 : 0;
    }

    protected BlockState setSignalForState(BlockState blockState, int strength) {
        return blockState.setValue(BlockStateProperties.POWERED, strength > 0);
    }

    @Override
    public void onRemove(BlockState old, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && old.getBlock() != newState.getBlock()) {
            if (this.getSignalForState(old) > 0) {
                this.updateNeighbours(level, pos);
            }
            super.onRemove(old, level, pos, newState, isMoving);
        }
    }

    @Override
    public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction) {
        return this.getSignalForState(state);
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction) {
        return direction == Direction.UP ? this.getSignalForState(state) : 0;
    }

    @Override
    public boolean isSignalSource(BlockState p_60571_) {
        return true;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_49353_) {
        return PushReaction.BLOCK;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.POWERED);
    }
}
