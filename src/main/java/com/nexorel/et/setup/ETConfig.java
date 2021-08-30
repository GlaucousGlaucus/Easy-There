
package com.nexorel.et.setup;


import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class ETConfig {

    // Categories
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_SKILLS = "skills";

    // Sub-Categories
    public static final String SUB_CATEGORY_COMBAT_SKILL = "combat_skill";

    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.BooleanValue COMBAT_COOLDOWN;

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        SERVER_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
        SERVER_BUILDER.comment("Skill Settings").push(CATEGORY_SKILLS);

        setupCombatSkillConfig(SERVER_BUILDER, CLIENT_BUILDER);
        SERVER_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupCombatSkillConfig(ForgeConfigSpec.Builder SERVER_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER) {
        SERVER_BUILDER.comment("Combat Skill").push(SUB_CATEGORY_COMBAT_SKILL);

        COMBAT_COOLDOWN = SERVER_BUILDER.comment("Use Combat Cooldown").define("cooldown", true);

        SERVER_BUILDER.pop();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }


}

