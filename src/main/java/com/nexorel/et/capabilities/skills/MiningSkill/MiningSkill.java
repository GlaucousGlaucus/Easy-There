package com.nexorel.et.capabilities.skills.MiningSkill;

import com.google.common.collect.Maps;
import com.nexorel.et.Network.EasyTherePacketHandler;
import com.nexorel.et.Network.MiningSkillPacket;
import com.nexorel.et.Registries.BlockInit;
import com.nexorel.et.capabilities.skills.ISkills;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public class MiningSkill implements ISkills {

    private double xp;

    public MiningSkill() {
        this(0);
    }

    public MiningSkill(double points) {
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
        EasyTherePacketHandler.sendDataToClient(new MiningSkillPacket(nbt), playerEntity);
    }

    public static Map<Block, Float> getMiningXp() {
        Map<Block, Float> map = Maps.newLinkedHashMap();
        map.put(Blocks.DIAMOND_ORE, 50.0F);
        map.put(Blocks.COAL_ORE, 5.0F);
        map.put(Blocks.COPPER_ORE, 8.0F);
        map.put(Blocks.LAPIS_ORE, 25.0F);
        map.put(Blocks.REDSTONE_ORE, 15.0F);
        map.put(Blocks.GOLD_ORE, 20.0F);
        map.put(Blocks.IRON_ORE, 13.0F);
        map.put(Blocks.EMERALD_ORE, 25.0F);
        map.put(Blocks.DEEPSLATE_DIAMOND_ORE, 75.0F);
        map.put(Blocks.DEEPSLATE_COAL_ORE, 10.0F);
        map.put(Blocks.DEEPSLATE_COPPER_ORE, 16.0F);
        map.put(Blocks.DEEPSLATE_LAPIS_ORE, 30.0F);
        map.put(Blocks.DEEPSLATE_REDSTONE_ORE, 18.0F);
        map.put(Blocks.DEEPSLATE_GOLD_ORE, 25.0F);
        map.put(Blocks.DEEPSLATE_IRON_ORE, 15.0F);
        map.put(Blocks.DEEPSLATE_EMERALD_ORE, 45.0F);
        map.put(Blocks.NETHER_QUARTZ_ORE, 25.0F);
        map.put(Blocks.NETHER_GOLD_ORE, 25.0F);
        map.put(Blocks.ANCIENT_DEBRIS, 150.0F);
        // Gems
        map.put(BlockInit.AGRIYELITE_BLOCK.get(), 25.0F);
        map.put(BlockInit.AQUOMITE_BLOCK.get(), 25.0F);
        map.put(BlockInit.GOLD_ALMAO_BLOCK.get(), 25.0F);
        map.put(BlockInit.ORANGE_RED_TEMARELITE_BLOCK.get(), 25.0F);
        map.put(BlockInit.CRYOPHANITE_BLOCK.get(), 25.0F);
        map.put(BlockInit.PEACH_EKANESIA_BLOCK.get(), 25.0F);
        map.put(BlockInit.CRIMSON_PECTENE_BLOCK.get(), 25.0F);
        map.put(BlockInit.BLUE_VIOLET_AEGIDONYX_BLOCK.get(), 25.0F);
        map.put(BlockInit.ELECTRIC_BLUE_CYPBERITE_BLOCK.get(), 25.0F);
        map.put(BlockInit.TWINKLING_BREADITE_BLOCK.get(), 25.0F);
        map.put(BlockInit.SALMON_LINADINGERITE_BLOCK.get(), 25.0F);
        map.put(BlockInit.BLUE_RAPMONY_BLOCK.get(), 25.0F);
        map.put(BlockInit.VIOLET_TUNORADOITE_BLOCK.get(), 25.0F);
        map.put(BlockInit.MAGENTA_ROSE_LOLLNIC_BLOCK.get(), 25.0F);
        map.put(BlockInit.JADE_PETAOGOPITE_BLOCK.get(), 25.0F);
        return map;
    }

}
