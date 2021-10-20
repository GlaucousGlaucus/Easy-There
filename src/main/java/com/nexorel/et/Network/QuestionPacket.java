package com.nexorel.et.Network;

import com.nexorel.et.content.blocks.dungeon.puzzles.quiz.Question;
import com.nexorel.et.content.blocks.dungeon.puzzles.quiz.QuestionBE;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class QuestionPacket {

    private final CompoundTag nbt;

    public QuestionPacket(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public static void encodeMsg(QuestionPacket msg, FriendlyByteBuf buffer) {
        buffer.writeNbt(msg.nbt);
    }

    public static QuestionPacket decodeMsg(FriendlyByteBuf buffer) {
        return new QuestionPacket(buffer.readNbt());
    }

    public static class Handler {
        public static void handle(final QuestionPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                BlockPos pos = NbtUtils.readBlockPos(msg.nbt.getCompound("pos"));
                String answer = msg.nbt.getString("answer");
                Question question = new Question(msg.nbt.getString("q"), msg.nbt.getString("a"));
                if (player != null) {
                    Level level = player.level;
                    if (level.hasChunkAt(pos) && level instanceof ServerLevel && level.getBlockEntity(pos) instanceof QuestionBE be) {
                        be.setAnswer(answer);
                        be.setQuestion(question);
                        be.setChanged();
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
