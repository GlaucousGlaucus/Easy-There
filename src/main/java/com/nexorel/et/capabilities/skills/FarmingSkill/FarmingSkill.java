package com.nexorel.et.capabilities.skills.FarmingSkill;

import com.google.common.collect.Maps;
import com.nexorel.et.Network.EasyTherePacketHandler;
import com.nexorel.et.Network.FarmingSkillPacket;
import com.nexorel.et.capabilities.skills.ISkills;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public class FarmingSkill implements ISkills {

    private double xp;

    public FarmingSkill() {
        this(0);
    }

    public FarmingSkill(double points) {
        this.xp = points;
    }

    public int getLevel() {
        return ISkills.calculateLvlFromXp(this.xp);
    }

    public void addXp(double points) {
//        cap = 482028;
        xp += points;
    }

    public double getXp() {
        return this.xp;
    }

    public void setXp(double points) {
        xp = points;
    }

    public void shareData(ServerPlayer playerEntity) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("xp", this.xp);
        EasyTherePacketHandler.sendDataToClient(new FarmingSkillPacket(nbt), playerEntity);
    }

    public static Map<Block, Float> getFarmingXp() {
        Map<Block, Float> map = Maps.newLinkedHashMap();
        map.put(Blocks.WHEAT, 10.0F);
        map.put(Blocks.BEETROOTS, 15.0F);
        map.put(Blocks.POTATOES, 15.0F);
        map.put(Blocks.CARROTS, 15.0F);
        map.put(Blocks.MELON, 25.0F);
        map.put(Blocks.PUMPKIN, 30.0F);
        map.put(Blocks.BAMBOO, 1.0F);
        map.put(Blocks.SUGAR_CANE, 10.0F);
        map.put(Blocks.SWEET_BERRY_BUSH, 25.0F);
        map.put(Blocks.CACTUS, 50.0F);
        map.put(Blocks.RED_MUSHROOM_BLOCK, 50.0F);
        map.put(Blocks.BROWN_MUSHROOM_BLOCK, 50.0F);
        map.put(Blocks.KELP, 1.0F);
        map.put(Blocks.SEA_PICKLE, 20.0F);
        map.put(Blocks.NETHER_WART, 10.0F);
        map.put(Blocks.CHORUS_FLOWER, 30.0F);
        map.put(Blocks.GLOW_LICHEN, 25.0F);
        return map;
    }

}
