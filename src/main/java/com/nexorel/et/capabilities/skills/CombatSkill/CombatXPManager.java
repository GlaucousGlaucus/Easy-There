package com.nexorel.et.capabilities.skills.CombatSkill;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.nexorel.et.EasyThere;
import com.nexorel.et.util.CombatHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.nexorel.et.Reference.MODID;

public class CombatXPManager extends SimpleJsonResourceReloadListener {

    public static final Logger LOGGER = EasyThere.LOGGER;
    private static final Gson GSON_INSTANCE = createSerializer().create();

    public static final ResourceLocation EMPTY = new ResourceLocation(MODID, "empty");

    private Map<ResourceLocation, Map<EntityType<?>, Float>> registeredXps = ImmutableMap.of();

    private static final String folder = "skills/combat_xp";

    public CombatXPManager() {
        super(GSON_INSTANCE, folder);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> rl, ResourceManager manager, ProfilerFiller filler) {
        ImmutableMap.Builder<ResourceLocation, Map<EntityType<?>, Float>> builder = ImmutableMap.builder();
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
                    Map<EntityType<?>, Float> combat_xp = new HashMap<>();
                    CombatHelper.addCombatXpsToMap(jsonarray, combat_xp);
                    builder.put(resourceLocation, combat_xp);
                }
            } catch (Exception e) {
                LOGGER.error("Couldn't parse xps {}", rl, e);
            }
        });
        this.registeredXps = builder.build();
    }

    public static GsonBuilder createSerializer() {
        return (new GsonBuilder());
    }

    public Map<EntityType<?>, Float> getAllXps() {
        Map<EntityType<?>, Float> map1 = new HashMap<>();
        this.registeredXps.forEach((resourceLocation1, map) -> map1.putAll(map));
        return map1;
    }
}
