package com.nexorel.et.content.blocks.dungeon.traps;

import com.nexorel.et.content.blocks.dungeon.DungeonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.*;

public class PitTrap extends DungeonBlock {

    protected static final VoxelShape NULL = Block.box(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    protected static final VoxelShape NORMAL = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public PitTrap() {
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.OPEN, false).setValue(BlockStateProperties.POWERED, false));
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, Random random) {
        if (serverLevel.hasNeighborSignal(pos)) {
            serverLevel.setBlock(pos, state.setValue(BlockStateProperties.OPEN, true), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
        } else {
            if (state.getValue(BlockStateProperties.OPEN)) {
                serverLevel.setBlock(pos, state.setValue(BlockStateProperties.OPEN, false), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos1, boolean b) {
        if (!level.isClientSide) {
            Block targetBlock = state.getBlock();
            Set<BlockPos> neighbours = getNeighbours(targetBlock, level, pos);
            for (BlockPos neighbour_pos : neighbours) {
                BlockState neighbour_state = level.getBlockState(neighbour_pos);
                boolean flag = level.hasNeighborSignal(pos);
                if (flag != neighbour_state.getValue(BlockStateProperties.POWERED)) {
                    if (neighbour_state.getValue(BlockStateProperties.OPEN) != flag) {
                        neighbour_state = neighbour_state.setValue(BlockStateProperties.OPEN, flag);
                    }
                    level.setBlock(neighbour_pos, neighbour_state.setValue(BlockStateProperties.POWERED, flag), 2);
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        if (state.getValue(BlockStateProperties.OPEN)) {
            return NULL;
        }
        return NORMAL;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        Block targetBlock = state.getBlock();
        Set<BlockPos> neighbours = getNeighbours(targetBlock, level, pos);
        if (!(entity instanceof Player)) {
            return;
        }
        for (BlockPos neighbour_pos : neighbours) {
            BlockState neighbour_state = level.getBlockState(neighbour_pos);
            if (neighbour_state.getValue(BlockStateProperties.POWERED)) continue;
            neighbour_state = neighbour_state.setValue(BlockStateProperties.OPEN, true);
            level.setBlock(neighbour_pos, neighbour_state, 2);
            level.getBlockTicks().scheduleTick(neighbour_pos, this, 100);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = defaultBlockState();
        boolean hasSignal = context.getLevel().hasNeighborSignal(context.getClickedPos());
        if (hasSignal) {
            blockstate.setValue(BlockStateProperties.POWERED, hasSignal);
        }
        return blockstate;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        if (state.getValue(BlockStateProperties.OPEN)) {
            return RenderShape.INVISIBLE;
        }
        return super.getRenderShape(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.POWERED, BlockStateProperties.OPEN);
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
}
