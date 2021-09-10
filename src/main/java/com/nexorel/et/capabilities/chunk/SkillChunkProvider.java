package com.nexorel.et.capabilities.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class SkillChunkProvider implements ICapabilitySerializable<CompoundTag> {

    private final SkillChunk skillChunk = new SkillChunk();
    private final static String SKILL_CHUNK_NBT = "skill_chunk";

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == SkillChunkCap.BLOCK_CAP) {
            return (LazyOptional<T>) LazyOptional.of(() -> skillChunk);
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        if (SkillChunkCap.BLOCK_CAP == null) {
            return new CompoundTag();
        } else {
            CompoundTag tag = new CompoundTag();
            ListTag listTag_pos = new ListTag();
            ListTag listTag_state = new ListTag();
            skillChunk.getStoredPos().forEach((blockPos, blockState) -> {
                listTag_pos.add(NbtUtils.writeBlockPos(blockPos));
                listTag_state.add(NbtUtils.writeBlockState(blockState));
            });
            tag.put("pp_pos", listTag_pos);
            tag.put("pp_state", listTag_state);
            return tag;
        }
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (SkillChunkCap.BLOCK_CAP != null) {
            HashMap<BlockPos, BlockState> posBlockStateHashMap = new HashMap<>();
            ListTag pos = nbt.getList("pp_pos", Constants.NBT.TAG_COMPOUND);
            ListTag state = nbt.getList("pp_state", Constants.NBT.TAG_COMPOUND);
            int size = pos.size();
            for (int i = 0; i < size; i++) {
                BlockPos temp_pos = NbtUtils.readBlockPos(pos.getCompound(i));
                BlockState temp_state = NbtUtils.readBlockState(state.getCompound(i));
                posBlockStateHashMap.put(temp_pos, temp_state);
            }
            skillChunk.setBlockPosData(posBlockStateHashMap);
        }
    }

}
