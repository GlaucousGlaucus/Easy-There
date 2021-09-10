package com.nexorel.et.Network;

import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkillCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class MiningSkillPacket {

    private final CompoundTag nbt;

    public MiningSkillPacket(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public static void encodeMsg(MiningSkillPacket msg, FriendlyByteBuf buffer) {
        buffer.writeNbt(msg.nbt);
    }

    public static MiningSkillPacket decodeMsg(FriendlyByteBuf buffer) {
        return new MiningSkillPacket(buffer.readNbt());
    }

    public static class Handler {
        public static void handle(final MiningSkillPacket msg, Supplier<NetworkEvent.Context> ctx) {
            if (Minecraft.getInstance().player != null) {
                ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player.getCapability(MiningSkillCapability.MINING_CAP).ifPresent(miningSkill -> {
                    miningSkill.setXp(msg.nbt.getDouble("xp"));
                })));
            }
            ctx.get().setPacketHandled(true);
        }
    }
}
