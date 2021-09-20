package com.nexorel.et.content.blocks.dungeon.puzzles.quiz;

import com.mojang.math.Vector3f;
import com.nexorel.et.content.blocks.dungeon.DungeonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Random;

public class QuestionBlock extends DungeonBlock implements EntityBlock {

    public static final Vector3f COLOR = new Vector3f(Vec3.fromRGB24(0xffb534));


    public QuestionBlock() {
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.POWERED, false));
    }

    private int getSignalForState(BlockState state) {
        return state.getValue(BlockStateProperties.POWERED) ? 15 : 0;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        Vector3f color = new Vector3f(Vec3.fromRGB24(0xffb534));
        if (state.getValue(BlockStateProperties.POWERED)) {
            for (double t = 0; t < 2 * Mth.PI; t += 0.5) {
                float j = random.nextFloat();
                float k = random.nextFloat();
                double x = pos.getX() + 0.5 + Math.cos(t);
                double z = pos.getZ() + 0.5 + Math.sin(t);
                double y = (double) ((float) pos.getY() + random.nextFloat());
                double delta = 0;
                double speed = 0;
                double count = 0;
                level.addParticle(new DustParticleOptions(color, 1.0F), x, y, z, delta, speed, count);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof QuestionBE be) {
            if (state.getValue(BlockStateProperties.POWERED)) {
                return InteractionResult.PASS;
            }
            if (player.getCommandSenderWorld().isClientSide) {
                QuestionScreen.open(be);
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (level1, blockPos, state1, t) -> {
            if (t instanceof QuestionBE te) te.tickServer();
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new QuestionBE(pos, state);
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

    protected void updateNeighbours(Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.POWERED);
    }
}
