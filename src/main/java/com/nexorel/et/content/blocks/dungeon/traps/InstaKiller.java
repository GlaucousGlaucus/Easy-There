package com.nexorel.et.content.blocks.dungeon.traps;

import com.nexorel.et.content.blocks.dungeon.DungeonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class InstaKiller extends DungeonBlock {

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!(entity instanceof Player player)) {
            entity.kill();
        } else {
            if (!player.isCreative()) {
                entity.kill();
            }
        }
    }
}
