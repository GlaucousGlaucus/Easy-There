package com.nexorel.et.content.blocks.GemRefinery;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class GemRefineryBlock extends Block {

    public GemRefineryBlock() {
        super(Properties.of(Material.METAL));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new GemRefineryTile();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof GemRefineryTile) {
                INamedContainerProvider containerProvider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("screen.et.gem_refinery");
                    }

                    @Nullable
                    @Override
                    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                        return new GRC(windowID, playerInventory, playerEntity, (GemRefineryTile) world.getBlockEntity(pos));
                    }
                };
                NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getBlockPos());
            }
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.CONSUME;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState new_state, boolean is_Moving) {
        if (!state.is(new_state.getBlock())) {
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof IInventory) {
                InventoryHelper.dropContents(world, pos, (IInventory) te);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, new_state, is_Moving);
        }
    }
}
