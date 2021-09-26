package com.nexorel.et.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nexorel.et.EasyThere;
import com.nexorel.et.content.blocks.dungeon.puzzles.quiz.Question;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.nexorel.et.Reference.MODID;

public class PuzzleUtil {

    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = LogManager.getLogger();
    public static final Question EMPTY = new Question("", "");
    public static final ResourceLocation RESOURCE_LOCATION_EMPTY = new ResourceLocation(MODID, "empty");

    /**
     * Just for reference
     */
    @Deprecated
    public static Map<List<String>, List<String>> getHardCodedQuestions() {
        Map<List<String>, List<String>> qna = new LinkedHashMap<>();
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        try {
            InputStream in = PuzzleUtil.class.getClassLoader().getResourceAsStream("/data/et/quiz/items.json");
            try {
                Reader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                JsonObject jsonObject = GsonHelper.fromJson(GSON, reader, JsonObject.class);
                if (jsonObject == null) {
                    LOGGER.error("PUZZLES ERROR");
                } else {
                    JsonArray jsonarray = GsonHelper.getAsJsonArray(jsonObject, "values");
                    for (JsonElement jsonElement : jsonarray) {
                        if (jsonElement.isJsonObject()) {
                            JsonObject object = jsonElement.getAsJsonObject();
                            if (object == null) {
                                LOGGER.info("NULL");
                            } else {
                                String q = GsonHelper.getAsString(object, "Question");
                                String a = GsonHelper.getAsString(object, "Answer");
                                questions.add(q);
                                answers.add(a);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.info(e);
            }
        } catch (Exception e) {
            LOGGER.info("B");
        }
        qna.put(questions, answers);
        return qna;
    }

    /**
     * Adds the values of all questions in JSON file to questionList
     *
     * @param jsonarray    - The Array of questions and answers
     * @param questionList - The list of quetsions
     */
    public static void addQNAsToList(JsonArray jsonarray, List<Question> questionList) {
        for (JsonElement jsonElement : jsonarray) {
            if (jsonElement.isJsonObject()) {
                JsonObject object = jsonElement.getAsJsonObject();
                if (object == null) {
                    LOGGER.info("NULL");
                } else {
                    String q = GsonHelper.getAsString(object, "Question");
                    String a = GsonHelper.getAsString(object, "Answer");
                    questionList.add(new Question(q, a));
                }
            }
        }
    }

    private static final Random random = new Random();

    public static Question getRandomQuestion() {
        List<Question> question = EasyThere.getQNAManager().getAllQuestions();
        int index = random.nextInt(question.size());
        return question.get(index);
    }
}
