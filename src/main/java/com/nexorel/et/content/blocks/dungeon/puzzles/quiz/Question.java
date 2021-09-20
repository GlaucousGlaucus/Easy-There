package com.nexorel.et.content.blocks.dungeon.puzzles.quiz;

public class Question {

    private final String Question;
    private final String Answer;

    public Question(String question, String answer) {
        Question = question;
        Answer = answer;
    }

    public Question(Question question) {
        Question = question.getQuestion();
        Answer = question.getAnswer();
    }

    public String getQuestion() {
        return Question;
    }

    public String getAnswer() {
        return Answer;
    }
}
