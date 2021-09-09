package com.nexorel.et.Network;

import com.nexorel.et.capabilities.FarmingSkill.FarmingSkillCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class FarmingSkillPacket {

    private final CompoundTag nbt;

    public FarmingSkillPacket(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public static void encodeMsg(FarmingSkillPacket msg, FriendlyByteBuf buffer) {
        buffer.writeNbt(msg.nbt);
    }

    public static FarmingSkillPacket decodeMsg(FriendlyByteBuf buffer) {
        return new FarmingSkillPacket(buffer.readNbt());
    }

    public static class Handler {
        public static void handle(final FarmingSkillPacket msg, Supplier<NetworkEvent.Context> ctx) {
            if (Minecraft.getInstance().player != null) {
                ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player.getCapability(FarmingSkillCapability.FARMING_CAP).ifPresent(farmingSkill -> {
                    farmingSkill.setXp(msg.nbt.getDouble("xp"));
                })));
            }
            ctx.get().setPacketHandled(true);
        }
    }

}
