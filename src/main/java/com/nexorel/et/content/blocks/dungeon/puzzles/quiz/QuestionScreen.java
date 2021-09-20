package com.nexorel.et.content.blocks.dungeon.puzzles.quiz;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nexorel.et.Network.EasyTherePacketHandler;
import com.nexorel.et.Network.QuestionPacket;
import com.nexorel.et.util.PuzzleUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class QuestionScreen extends Screen {

    private static final TranslatableComponent name = new TranslatableComponent("screen.et.quiz");
    private EditBox answerBox;
    private Button conform;
    private final Question question;
    private final BlockPos pos;
    private final QuestionBE be;

    @Override
    public void tick() {
        this.answerBox.tick();
    }

    public QuestionScreen(QuestionBE be) {
        super(name);
        this.pos = be.getBlockPos();
        this.be = be;
        this.question = PuzzleUtil.getRandomQuestion();
    }

    @Override
    protected void init() {
        this.answerBox = new EditBox(this.font, this.width / 2 - 152, 160, 300, 20, new TranslatableComponent("quiz.answer"));
        this.answerBox.setMaxLength(64);
        this.answerBox.setValue(be.getAnswer());
        this.addWidget(this.answerBox);
        int conform_btn_width = 200;
        int conform_btn_height = 20;
        this.conform = this.addRenderableWidget(new Button(this.width / 2 - (conform_btn_width / 2), 185, conform_btn_width, conform_btn_height, new TranslatableComponent("quiz.done_button"), button -> {
            sendToServer();
            Minecraft.getInstance().setScreen(null);
        }));
        this.conform.visible = true;
        this.setInitialFocus(this.answerBox);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String answerBoxValue = this.answerBox.getValue();
        this.init(minecraft, width, height);
        this.answerBox.setValue(answerBoxValue);
    }

    public static void open(QuestionBE be) {
        Minecraft.getInstance().setScreen(new QuestionScreen(be));
    }

    @Override
    public void onClose() {
        super.onClose();
        sendToServer();
    }

    private void sendToServer() {
        CompoundTag tag = new CompoundTag();
        tag.put("pos", NbtUtils.writeBlockPos(this.pos));
        tag.putString("answer", this.answerBox.getValue());
        tag.putString("q", this.question.getQuestion());
        tag.putString("a", this.question.getAnswer());
        EasyTherePacketHandler.sendDataToServer(new QuestionPacket(tag));
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        String description = "There is some text embedded on the block. It says: \n \"To Get Pass This Place You Must Solve This Riddle.\"";
        String[] description_array = description.split("\n");
        int lengthOfArrayDESC = description_array.length;
        for (int h = 0; h < lengthOfArrayDESC; h++) {
            String s = description_array[h];
            s = s.strip();
            MutableComponent label = new TextComponent(s);
            label.withStyle(ChatFormatting.AQUA);
            label.withStyle(ChatFormatting.ITALIC);
            int intHeight = 20 + h * 15;
            drawString(matrixStack, this.font, label, this.width / 2 - (this.font.width(s) / 2), intHeight, 0xffffff);
        }
        String question_label = this.question.getQuestion();
        String[] qstr = question_label.split("\n");
        int lengthOfArrayQSTR = qstr.length;
        for (int h = 0; h < lengthOfArrayQSTR; h++) {
            String s = qstr[h];
            s = s.strip();
            int intHeight = 60 + h * 15;
            drawString(matrixStack, this.font, s, this.width / 2 - (this.font.width(s) / 2), intHeight, 0xffffff);
        }
        String answer_label = "What's the Answer ?";
        MutableComponent label = new TextComponent(answer_label);
        label.withStyle(ChatFormatting.BOLD);
        drawString(matrixStack, this.font, label, this.width / 2 - 55, 145, 0xffffff);
        this.answerBox.render(matrixStack, mouseX, mouseY, partialTicks);
        this.conform.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public Question getQuestion() {
        return question;
    }
}
