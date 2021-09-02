package com.nexorel.et.content.blocks.GemRefinery;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;

public class GemRefineryBlock extends Block implements EntityBlock {

    public GemRefineryBlock() {
        super(
                Properties.of(Material.METAL)
                        .instabreak()
                        .strength(2)
                        .requiresCorrectToolForDrops()
        );
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return (level1, blockPos, state, t) -> {
            if (t instanceof GemRefineryTile tile) {
                tile.tickServer();
            }
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!world.isClientSide) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof GemRefineryTile) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return new TranslatableComponent("screen.et.gem_refinery");
                    }

                    @Nullable
                    @Override
                    public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player playerEntity) {
                        return new GRC(windowID, playerInventory, playerEntity, (GemRefineryTile) world.getBlockEntity(pos));
                    }
                };
                NetworkHooks.openGui((ServerPlayer) player, containerProvider, tileEntity.getBlockPos());
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.CONSUME;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState new_state, boolean is_Moving) {
        if (!state.is(new_state.getBlock())) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te instanceof Container) {
                Containers.dropContents(world, pos, (Container) te);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, new_state, is_Moving);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GemRefineryTile(pos, state);
    }
}
