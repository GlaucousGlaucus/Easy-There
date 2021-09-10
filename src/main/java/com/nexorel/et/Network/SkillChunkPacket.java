package com.nexorel.et.Network;

import com.nexorel.et.capabilities.chunk.SkillChunkCap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.HashMap;
import java.util.function.Supplier;

public class SkillChunkPacket {

    private final CompoundTag nbt;

    public SkillChunkPacket(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public static void encodeMsg(SkillChunkPacket msg, FriendlyByteBuf buffer) {
        buffer.writeNbt(msg.nbt);
    }

    public static SkillChunkPacket decodeMsg(FriendlyByteBuf buffer) {
        return new SkillChunkPacket(buffer.readNbt());
    }

    public static class Handler {
        public static void handle(final SkillChunkPacket msg, Supplier<NetworkEvent.Context> ctx) {
            if (Minecraft.getInstance().player != null) {
                ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player.getCapability(SkillChunkCap.BLOCK_CAP).ifPresent(skillChunk -> {
                    HashMap<BlockPos, BlockState> posBlockStateHashMap = new HashMap<>();
                    ListTag pos = msg.nbt.getList("pp_pos", Constants.NBT.TAG_COMPOUND);
                    ListTag state = msg.nbt.getList("pp_state", Constants.NBT.TAG_COMPOUND);
                    for (int i = 0; i < msg.nbt.size(); i++) {
                        BlockPos temp_pos = NbtUtils.readBlockPos(pos.getCompound(i));
                        BlockState temp_state = NbtUtils.readBlockState(state.getCompound(i));
                        posBlockStateHashMap.put(temp_pos, temp_state);
                    }
                    skillChunk.setBlockPosData(posBlockStateHashMap);
                })));
            }
            ctx.get().setPacketHandled(true);
        }
    }

}
