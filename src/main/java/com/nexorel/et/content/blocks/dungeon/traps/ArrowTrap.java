package com.nexorel.et.content.blocks.dungeon.traps;

import com.nexorel.et.content.blocks.dungeon.DungeonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nullable;
import java.util.Random;

public class ArrowTrap extends DungeonBlock {

    public ArrowTrap() {
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.NORTH).setValue(BlockStateProperties.TRIGGERED, false));
    }

    @Override
    public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, Random random) {
        dispenseFrom(serverLevel, pos);
    }

    protected void dispenseFrom(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Direction direction = state.getValue(BlockStateProperties.FACING);
        double x = pos.getX() + (double) direction.getStepX() + 0.5;
        double y = pos.getY() + (double) direction.getStepY() + 0.25;
        double z = pos.getZ() + (double) direction.getStepZ() + 0.5;
        Arrow arrow = new Arrow(level, x, y, z);
        arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        arrow.setCritArrow(true);
        arrow.setBaseDamage(20.0F);
        ItemStack stack = new ItemStack(Items.TIPPED_ARROW);
        PotionUtils.setPotion(stack, Potions.STRONG_HARMING);
        PotionUtils.setPotion(stack, Potions.SLOWNESS);
        PotionUtils.setPotion(stack, Potions.WEAKNESS);
        arrow.setEffectsFromItem(stack);
        // Last two : power, uncertainity
        float power = 1F;
        float uncertainity = 6.0F;
        arrow.shoot(direction.getStepX(), direction.getStepY() + 0.1F, direction.getStepZ(), power, uncertainity);
        level.addFreshEntity(arrow);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos1, boolean b) {
        if (!level.isClientSide) {
            boolean flag = level.hasNeighborSignal(pos);
            boolean flag1 = state.getValue(BlockStateProperties.TRIGGERED);
            if (flag && !flag1) {
                level.getBlockTicks().scheduleTick(pos, this, 4);
                level.setBlock(pos, state.setValue(BlockStateProperties.TRIGGERED, Boolean.TRUE), 4);
            } else if (!flag && flag1) {
                level.setBlock(pos, state.setValue(BlockStateProperties.TRIGGERED, Boolean.FALSE), 4);
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = defaultBlockState();
        boolean hasSignal = context.getLevel().hasNeighborSignal(context.getClickedPos());
        if (hasSignal) {
            blockstate = blockstate.setValue(BlockStateProperties.TRIGGERED, hasSignal);
        }
        blockstate = blockstate.setValue(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite());
        return blockstate;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.TRIGGERED);
    }
}
