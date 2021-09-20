package com.nexorel.et.content.blocks.dungeon.puzzles.quiz;

import com.nexorel.et.EasyThere;
import com.nexorel.et.Registries.BlockEntityInit;
import com.nexorel.et.util.SomeFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.Constants;

public class QuestionBE extends BlockEntity {

    private Question question;
    private String answer = "";
    private boolean locked = false;
    private boolean flag;

    public QuestionBE(BlockPos pos, BlockState state) {
        super(BlockEntityInit.QUESTION_TILE.get(), pos, state);
    }

    public void tickServer() {
        if (this.question != null && this.getQuestion().getAnswer().equalsIgnoreCase(this.getAnswer())) {
            this.locked = true;
            setChanged();
        } else {
            locked = false;
        }
        if (this.locked && !this.flag) {
            Level level = this.level;
            BlockPos pos = this.getBlockPos();
            if (level != null) {
                BlockState state = level.getBlockState(pos);
                state = state.setValue(BlockStateProperties.POWERED, true);
                level.setBlock(pos, state, Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
                level.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 5.0F, SomeFunctions.generateRandomIntBetweenRange(0, 2, level.random) + level.random.nextFloat());
            }
            this.flag = true;
        }
    }

    @Override
    public void load(CompoundTag tag) {
        String ques = tag.getString("ques");
        String ans = tag.getString("ans");
        this.question = new Question(ques, ans);
        EasyThere.LOGGER.info("TEST: " + tag.getString("answer"));
        this.answer = tag.getString("answer");
        this.locked = tag.getBoolean("locked");
        this.flag = tag.getBoolean("flag");
        super.load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("ques", this.question == null ? "" : this.question.getQuestion());
        tag.putString("ans", this.question == null ? "" : this.question.getAnswer());
        tag.putString("answer", answer);
        tag.putBoolean("locked", locked);
        tag.putBoolean("flag", flag);
        return super.save(tag);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}
