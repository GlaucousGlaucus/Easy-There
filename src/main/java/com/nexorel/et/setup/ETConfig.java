
package com.nexorel.et.setup;


import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber
public class ETConfig {

    public static class Client {
        public final ForgeConfigSpec.DoubleValue DMG_IND_SIZE;

        Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Client").push("client");
            DMG_IND_SIZE = builder.comment("Damage Indicator Size")
                    .translation("et.configclient.dmg_ind_size")
                    .defineInRange("dmg_ind_size", 1D, 0.25, 2);
            builder.pop();
        }
    }

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
    public static final ForgeConfigSpec clientSpec;
    public static final ETConfig.Client CLIENT;

    static {
        final Pair<Common, ForgeConfigSpec> commonForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(ETConfig.Common::new);
        commonSpec = commonForgeConfigSpecPair.getRight();
        COMMON = commonForgeConfigSpecPair.getLeft();

        final Pair<Client, ForgeConfigSpec> clientForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(ETConfig.Client::new);
        clientSpec = clientForgeConfigSpecPair.getRight();
        CLIENT = clientForgeConfigSpecPair.getLeft();
    }

}

