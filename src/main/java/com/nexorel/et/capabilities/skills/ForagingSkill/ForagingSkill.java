package com.nexorel.et.capabilities.skills.ForagingSkill;

import com.google.common.collect.Maps;
import com.nexorel.et.Network.EasyTherePacketHandler;
import com.nexorel.et.Network.ForagingSkillPacket;
import com.nexorel.et.capabilities.skills.ISkills;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public class ForagingSkill implements ISkills {

    private double xp;

    public ForagingSkill() {
        this(0);
    }

    public ForagingSkill(double points) {
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
        EasyTherePacketHandler.sendDataToClient(new ForagingSkillPacket(nbt), playerEntity);
    }

    public static Map<Block, Float> getForagingXp() {
        Map<Block, Float> map = Maps.newLinkedHashMap();
        map.put(Blocks.ACACIA_LOG, 15.0F);
        map.put(Blocks.BIRCH_LOG, 7.0F);
        map.put(Blocks.DARK_OAK_LOG, 8.0F);
        map.put(Blocks.OAK_LOG, 5.0F);
        map.put(Blocks.SPRUCE_LOG, 10.0F);
        map.put(Blocks.JUNGLE_LOG, 20.0F);
        map.put(Blocks.ACACIA_SAPLING, 5.0F);
        map.put(Blocks.BIRCH_SAPLING, 5.0F);
        map.put(Blocks.DARK_OAK_SAPLING, 5.0F);
        map.put(Blocks.OAK_SAPLING, 5.0F);
        map.put(Blocks.SPRUCE_SAPLING, 5.0F);
        map.put(Blocks.JUNGLE_SAPLING, 5.0F);
        return map;
    }

}
