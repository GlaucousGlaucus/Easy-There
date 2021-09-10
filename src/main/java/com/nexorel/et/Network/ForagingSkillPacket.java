package com.nexorel.et.Network;

import com.nexorel.et.capabilities.skills.ForagingSkill.ForagingSkillCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class ForagingSkillPacket {

    private final CompoundTag nbt;

    public ForagingSkillPacket(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public static void encodeMsg(ForagingSkillPacket msg, FriendlyByteBuf buffer) {
        buffer.writeNbt(msg.nbt);
    }

    public static ForagingSkillPacket decodeMsg(FriendlyByteBuf buffer) {
        return new ForagingSkillPacket(buffer.readNbt());
    }

    public static class Handler {
        public static void handle(final ForagingSkillPacket msg, Supplier<NetworkEvent.Context> ctx) {
            if (Minecraft.getInstance().player != null) {
                ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player.getCapability(ForagingSkillCapability.FORAGING_CAP).ifPresent(foragingSkill -> {
                    foragingSkill.setXp(msg.nbt.getDouble("xp"));
                })));
            }
            ctx.get().setPacketHandled(true);
        }
    }

}
