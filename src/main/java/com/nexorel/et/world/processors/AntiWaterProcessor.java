package com.nexorel.et.world.processors;

import com.mojang.serialization.Codec;
import com.nexorel.et.Registries.ProcessorInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;

public class AntiWaterProcessor extends StructureProcessor {

    public static final AntiWaterProcessor INSTANCE = new AntiWaterProcessor();
    public static final Codec<AntiWaterProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader reader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlaceSettings structurePlacementData, @Nullable StructureTemplate template) {
        ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
        if (blockInfoGlobal.state.hasProperty(BlockStateProperties.WATERLOGGED) && !blockInfoGlobal.state.getValue(BlockStateProperties.WATERLOGGED)) {
            ChunkAccess currentChunk = reader.getChunk(currentChunkPos.x, currentChunkPos.z);
            if (reader.getFluidState(blockInfoGlobal.pos).is(FluidTags.WATER)) {
                currentChunk.setBlockState(blockInfoGlobal.pos, Blocks.STONE.defaultBlockState(), false);
            }

            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {
                mutable.set(blockInfoGlobal.pos).move(direction);
                if (currentChunkPos.x != mutable.getX() >> 4 || currentChunkPos.z != mutable.getZ() >> 4) {
                    currentChunk = reader.getChunk(mutable);
                    currentChunkPos = new ChunkPos(mutable);
                }

                if (currentChunk.getFluidState(mutable).is(FluidTags.WATER)) {
                    currentChunk.setBlockState(mutable, Blocks.STONE.defaultBlockState(), false);
                }
            }
        }

        return blockInfoGlobal;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ProcessorInit.ANTI_WATER_PROCESSOR;
    }
}
