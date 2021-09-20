package com.nexorel.et.content.blocks.dungeon.puzzles.quiz;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.nexorel.et.EasyThere;
import com.nexorel.et.util.PuzzleUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionManager extends SimpleJsonResourceReloadListener {

    public static final Logger LOGGER = EasyThere.LOGGER;
    private static final Gson GSON_INSTANCE = createSerializer().create();

    public static final ResourceLocation EMPTY = PuzzleUtil.RESOURCE_LOCATION_EMPTY;

    private Map<ResourceLocation, List<Question>> registeredQNA = ImmutableMap.of();

    private static final String folder = "quiz";

    public QuestionManager() {
        super(GSON_INSTANCE, folder);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> rl, ResourceManager manager, ProfilerFiller filler) {
        ImmutableMap.Builder<ResourceLocation, List<Question>> builder = ImmutableMap.builder();
        if (rl.remove(EMPTY) != null) {
            LOGGER.warn("Datapack tried to redefine {} elemental entity data, ignoring", EMPTY);
        }
        rl.forEach((resourceLocation, element) -> {
            try (Resource resource = manager.getResource(getPreparedPath(resourceLocation))) {
                InputStream in = resource.getInputStream();
                Reader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                JsonObject jsonObject = GsonHelper.fromJson(GSON_INSTANCE, reader, JsonObject.class);
                if (jsonObject == null) {
                    LOGGER.error("JsonObject is Null -> {}", jsonObject);
                } else {
                    JsonArray jsonarray = GsonHelper.getAsJsonArray(jsonObject, "values");
                    List<Question> questionList = new ArrayList<>();
                    PuzzleUtil.addQNAsToList(jsonarray, questionList);
                    builder.put(resourceLocation, questionList);
                }
            } catch (Exception e) {
                LOGGER.error("Couldn't parse qnas {}", rl, e);
            }
        });
        this.registeredQNA = builder.build();
    }

    public static GsonBuilder createSerializer() {
        return (new GsonBuilder());
    }

    public List<Question> getAllQuestions() {
        List<Question> l = new ArrayList<>();
        this.registeredQNA.forEach((resourceLocation1, questions) -> l.addAll(questions));
        l.remove(PuzzleUtil.EMPTY);
        return l;
    }

}
