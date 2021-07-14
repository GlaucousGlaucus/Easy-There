package com.nexorel.et.content.blocks.AuraInfestedBlock;

import com.nexorel.et.Registries.TileInit;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class AuraInfestedTile extends TileEntity {

    public static final ModelProperty<BlockState> stateForRender1 = new ModelProperty<>();
//    private BlockState stateForRender = Blocks.STONE.defaultBlockState();
    private BlockState stateForRender;

    public AuraInfestedTile() {
        super(TileInit.AURA_INFESTED_TILE.get());
    }

    public BlockState getBlockForRender() {
        return stateForRender;
    }

    public void setBlockForRender(BlockState state) {
        this.stateForRender = state;
        setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        if (nbt.contains("a_i")) {
            NBTUtil.readBlockState(nbt.getCompound("a_i"));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        if (stateForRender != null) {
            nbt.put("a_i", NBTUtil.writeBlockState(stateForRender));
        }
        return super.save(nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        if (stateForRender != null) {
            nbt.put("a_i", NBTUtil.writeBlockState(stateForRender));
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        if (tag.contains("a_i")) {
            NBTUtil.readBlockState(tag.getCompound("a_i"));
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getBlockPos(), 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState old = stateForRender;
        CompoundNBT tag = pkt.getTag();
        handleUpdateTag(this.getBlockState(), tag);
        if (!Objects.equals(old, stateForRender)) {
            ModelDataManager.requestModelDataRefresh(this);
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
        }
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(stateForRender1, stateForRender).build();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos(), getBlockPos());
    }
}
