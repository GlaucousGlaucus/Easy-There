package com.nexorel.et.capabilities.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public class SkillChunk {

    private HashMap<BlockPos, BlockState> blocks;

    public SkillChunk() {
        this(new HashMap<>());
    }

    public SkillChunk(HashMap<BlockPos, BlockState> blocks) {
        this.blocks = blocks;
    }

    public HashMap<BlockPos, BlockState> getStoredPos() {
        return blocks;
    }

    public void addData(BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof AirBlock) return;
        this.blocks.put(pos, state);
    }

    public void removeData(BlockPos pos) {
        this.blocks.remove(pos);
    }

    public void setBlockPosData(HashMap<BlockPos, BlockState> blocks) {
        this.blocks = blocks;
    }
}
