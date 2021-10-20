package com.nexorel.et.Network;

import com.nexorel.et.capabilities.skills.CombatSkill.CombatSkillCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class CombatSkillPacket {

    private final CompoundTag nbt;

    public CombatSkillPacket(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public static void encodeMsg(CombatSkillPacket msg, FriendlyByteBuf buffer) {
        buffer.writeNbt(msg.nbt);
    }

    public static CombatSkillPacket decodeMsg(FriendlyByteBuf buffer) {
        return new CombatSkillPacket(buffer.readNbt());
    }

    public static class Handler {
        public static void handle(final CombatSkillPacket msg, Supplier<NetworkEvent.Context> ctx) {
            if (Minecraft.getInstance().player != null) {
                ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player.getCapability(CombatSkillCapability.COMBAT_CAP).ifPresent(combatSkill -> {
                    combatSkill.setXp(msg.nbt.getDouble("xp"));
                })));
            }
            ctx.get().setPacketHandled(true);
        }
    }
}
