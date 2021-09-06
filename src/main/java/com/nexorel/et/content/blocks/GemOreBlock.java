package com.nexorel.et.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class GemOreBlock extends Block {

    private final int xp;

    public GemOreBlock(int xp) {
        super(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(35, 70)
        );
        this.xp = xp;
    }

    @Override
    public int getExpDrop(BlockState state, LevelReader world, BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? (int) Math.floor(xp + (5 * Math.random())) : 0;
    }
}
