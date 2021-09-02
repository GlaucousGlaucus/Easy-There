package com.nexorel.et.Network;

import com.nexorel.et.capabilities.CombatSkillCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class SkillPacket {

    private final CompoundTag nbt;

    public SkillPacket(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public static void encodeMsg(SkillPacket msg, FriendlyByteBuf buffer) {
        buffer.writeNbt(msg.nbt);
    }

    public static SkillPacket decodeMsg(FriendlyByteBuf buffer) {
        return new SkillPacket(buffer.readNbt());
    }

    public static class Handler {
        public static void handle(final SkillPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> {
                combatSkill.setXp(msg.nbt.getDouble("xp"));
                combatSkill.setCrit_chance(msg.nbt.getInt("crit_chance"));
            })));
            ctx.get().setPacketHandled(true);
        }
    }
}
