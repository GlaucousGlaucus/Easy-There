package com.nexorel.et.content.blocks.dungeon;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

/*
 *
 * Traps:-
 *           1. Flame Trap,
 *                Pit trap,
 *                Spike Trap,
 *                Literally a Bear trap (A bear falls on player),
 *           2. Puzzles - A Quiz which bamboozles player,
 *               Player has to find a lever to gain access to other rooms,
 *               A Maze (Memory Maze - player will be given the directions to the end and will have a limited amount of time)
 *               Timed Trivia (I would not read this.txt)
 *           3. Add Timer to dungeon run
 *           4. Unfair Room #UNFAIRMAPS
 *
 * */
public class DungeonBlock extends Block {

    public DungeonBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
                .strength(-1.0F, 3600000.0F)
                .noDrops()
                .sound(SoundType.STONE)
                .isValidSpawn(DungeonBlock::never)
        );
    }

    private static Boolean never(BlockState p_50779_, BlockGetter p_50780_, BlockPos p_50781_, EntityType<?> p_50782_) {
        return false;
    }
}
