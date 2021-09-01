
package com.nexorel.et.setup;


import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber
public class ETConfig {

    public static class Common {
        public final ForgeConfigSpec.BooleanValue COMBAT_COOLDOWN;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Common").push("common");
            COMBAT_COOLDOWN = builder.comment("Use Attack Cooldown")
                    .translation("et.configskills.combat_cooldown")
                    .define("combat_cooldown", true);
            builder.pop();
        }
    }

    public static final ForgeConfigSpec commonSpec;
    public static final ETConfig.Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> commonForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(ETConfig.Common::new);
        commonSpec = commonForgeConfigSpecPair.getRight();
        COMMON = commonForgeConfigSpecPair.getLeft();
    }

}

