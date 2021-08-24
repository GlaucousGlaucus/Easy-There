package com.nexorel.et.Network;

import com.nexorel.et.capabilities.CombatSkillCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SkillPacket {

    private double tag;

    public SkillPacket(double tag) {
        this.tag = tag;
    }

    public static void encodeMsg(SkillPacket msg, PacketBuffer buffer) {
        buffer.writeDouble(msg.tag);
    }

    public static SkillPacket decodeMsg(PacketBuffer buffer) {
        return new SkillPacket(buffer.readDouble());
    }

    public static class Handler {
        public static void handle(final SkillPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                Minecraft.getInstance().player.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(cap -> {
                    msg.tag = cap.getXp();
                });
            }
            ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> combatSkill.setXp(msg.tag))));
            ctx.get().setPacketHandled(true);
        }
    }

    public static class ClientPacketHandler {
        public static void handle(final SkillPacket msg, Supplier<NetworkEvent.Context> ctx) {
        }
    }
}
