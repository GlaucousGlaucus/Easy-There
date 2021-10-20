package com.nexorel.et.Network;

import com.nexorel.et.capabilities.skills.Stats.Stats;
import com.nexorel.et.capabilities.skills.Stats.StatsCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class StatsPacket {

    private final CompoundTag nbt;

    public StatsPacket(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public static void encodeMsg(StatsPacket msg, FriendlyByteBuf buffer) {
        buffer.writeNbt(msg.nbt);
    }

    public static StatsPacket decodeMsg(FriendlyByteBuf buffer) {
        return new StatsPacket(buffer.readNbt());
    }

    public static class Handler {
        public static void handle(final StatsPacket msg, Supplier<NetworkEvent.Context> ctx) {
            if (Minecraft.getInstance().player != null) {
                Stats stats = Minecraft.getInstance().player.getCapability(StatsCapability.STATS_CAP).orElse(null);
                ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    stats.setAccuracy(msg.nbt.getDouble("accuracy"));
                    stats.setAgility(msg.nbt.getDouble("agility"));
                    stats.setFortune(msg.nbt.getDouble("fortune"));
                    stats.setStrength(msg.nbt.getDouble("strength"));
                    stats.setCrit_chance(msg.nbt.getInt("crit_chance"));
                }));
            }
            ctx.get().setPacketHandled(true);
        }
    }

}
