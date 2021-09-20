package com.nexorel.et.Network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class EasyTherePacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("et", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(
                id++,
                CombatSkillPacket.class,
                CombatSkillPacket::encodeMsg,
                CombatSkillPacket::decodeMsg,
                CombatSkillPacket.Handler::handle);
        INSTANCE.registerMessage(
                id++,
                MiningSkillPacket.class,
                MiningSkillPacket::encodeMsg,
                MiningSkillPacket::decodeMsg,
                MiningSkillPacket.Handler::handle);
        INSTANCE.registerMessage(
                id++,
                ForagingSkillPacket.class,
                ForagingSkillPacket::encodeMsg,
                ForagingSkillPacket::decodeMsg,
                ForagingSkillPacket.Handler::handle);
        INSTANCE.registerMessage(
                id++,
                FarmingSkillPacket.class,
                FarmingSkillPacket::encodeMsg,
                FarmingSkillPacket::decodeMsg,
                FarmingSkillPacket.Handler::handle);
        INSTANCE.registerMessage(
                id++,
                QuestionPacket.class,
                QuestionPacket::encodeMsg,
                QuestionPacket::decodeMsg,
                QuestionPacket.Handler::handle);
    }

    public static void sendDataToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }

    public static void sendDataToClient(Object msg, ServerPlayer player) {
        if (!(player instanceof FakePlayer)) {
            INSTANCE.sendTo(msg, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
        }
    }
}
