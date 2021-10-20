
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

        // Ores
        public final ForgeConfigSpec.IntValue AGRIYELITE_ORE_SIZE;
        public final ForgeConfigSpec.IntValue AQUOMITE_ORE_SIZE;
        public final ForgeConfigSpec.IntValue GOLD_ALMAO_ORE_SIZE;
        public final ForgeConfigSpec.IntValue ORANGE_RED_TEMARELITE_ORE_SIZE;
        public final ForgeConfigSpec.IntValue CRYOPHANITE_ORE_SIZE;
        public final ForgeConfigSpec.IntValue PEACH_EKANESIA_ORE_SIZE;
        public final ForgeConfigSpec.IntValue CRIMSON_PECTENE_ORE_SIZE;
        public final ForgeConfigSpec.IntValue BLUE_VIOLET_AEGIDONYX_ORE_SIZE;
        public final ForgeConfigSpec.IntValue ELECTRIC_BLUE_CYPBERITE_ORE_SIZE;
        public final ForgeConfigSpec.IntValue TWINKLING_BREADITE_ORE_SIZE;
        public final ForgeConfigSpec.IntValue SALMON_LINADINGERITE_ORE_SIZE;
        public final ForgeConfigSpec.IntValue BLUE_RAPMONY_ORE_SIZE;
        public final ForgeConfigSpec.IntValue VIOLET_TUNORADOITE_ORE_SIZE;
        public final ForgeConfigSpec.IntValue MAGENTA_ROSE_LOLLNIC_ORE_SIZE;
        public final ForgeConfigSpec.IntValue JADE_PETAOGOPITE_ORE_SIZE;


        // Doubles
        public final ForgeConfigSpec.IntValue DUNGEON_MAP_TRADE_COST;

        // Booleans
        public final ForgeConfigSpec.BooleanValue COMBAT_COOLDOWN;
        public final ForgeConfigSpec.BooleanValue DMG_IND_ENABLED;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Common").push("common");

            COMBAT_COOLDOWN = builder.comment("Use Attack Cooldown")
                    .translation("et.configskills.combat_cooldown")
                    .define("combat_cooldown", true);

            DMG_IND_ENABLED = builder.comment("Enable/Disable Damage Indicators").translation("et:config.dmg_ind_bool")
                    .define("damage_indicator_toggle", true);

            DUNGEON_MAP_TRADE_COST = builder.comment("Cost of Dungeon Map from Wandering Trader")
                    .translation("et.config.dungeon.map_cost")
                    .defineInRange("dungeon_map_cost", 20, 1, 64);

            // Ores
            int oreMax = 200;
            int oreMin = 1;
            AGRIYELITE_ORE_SIZE = builder.comment("Size of agriyelite_ore Veins")
                    .translation("et.config.ore.agriyelite_ore_size")
                    .defineInRange("agriyelite_ore_size", 8, oreMin, oreMax);
            AQUOMITE_ORE_SIZE = builder.comment("Size of aquomite_ore Veins")
                    .translation("et.config.ore.aquomite_ore_size")
                    .defineInRange("aquomite_ore_size", 8, oreMin, oreMax);
            GOLD_ALMAO_ORE_SIZE = builder.comment("Size of gold_almao_ore Veins")
                    .translation("et.config.ore.gold_almao_ore_size")
                    .defineInRange("gold_almao_ore_size", 8, oreMin, oreMax);
            ORANGE_RED_TEMARELITE_ORE_SIZE = builder.comment("Size of orange_red_temarelite_ore Veins")
                    .translation("et.config.ore.orange_red_temarelite_ore_size")
                    .defineInRange("orange_red_temarelite_ore_size", 8, oreMin, oreMax);
            CRYOPHANITE_ORE_SIZE = builder.comment("Size of cryophanite_ore Veins")
                    .translation("et.config.ore.cryophanite_ore_size")
                    .defineInRange("cryophanite_ore", 8, oreMin, oreMax);
            PEACH_EKANESIA_ORE_SIZE = builder.comment("Size of peach_ekanesia_ore Veins")
                    .translation("et.config.ore.peach_ekanesia_ore_size")
                    .defineInRange("peach_ekanesia_ore", 8, oreMin, oreMax);
            CRIMSON_PECTENE_ORE_SIZE = builder.comment("Size of crimson_pectene_ore Veins")
                    .translation("et.config.ore.crimson_pectene_ore_size")
                    .defineInRange("crimson_pectene_ore", 8, oreMin, oreMax);
            BLUE_VIOLET_AEGIDONYX_ORE_SIZE = builder.comment("Size of blue_violet_aegidonyx_ore Veins")
                    .translation("et.config.ore.blue_violet_aegidonyx_ore_size")
                    .defineInRange("blue_violet_aegidonyx_ore", 8, oreMin, oreMax);
            ELECTRIC_BLUE_CYPBERITE_ORE_SIZE = builder.comment("Size of electric_blue_cypberite_ore Veins")
                    .translation("et.config.ore.electric_blue_cypberite_ore_size")
                    .defineInRange("electric_blue_cypberite_ore", 8, oreMin, oreMax);
            TWINKLING_BREADITE_ORE_SIZE = builder.comment("Size of twinkling_breadite_ore Veins")
                    .translation("et.config.ore.twinkling_breadite_ore_size")
                    .defineInRange("twinkling_breadite_ore", 8, oreMin, oreMax);
            SALMON_LINADINGERITE_ORE_SIZE = builder.comment("Size of salmon_linadingerite_ore Veins")
                    .translation("et.config.ore.salmon_linadingerite_ore_size")
                    .defineInRange("salmon_linadingerite_ore_size", 8, oreMin, oreMax);
            BLUE_RAPMONY_ORE_SIZE = builder.comment("Size of blue_rapmony_ore Veins")
                    .translation("et.config.ore.blue_rapmony_ore_size")
                    .defineInRange("blue_rapmony_ore", 8, oreMin, oreMax);
            VIOLET_TUNORADOITE_ORE_SIZE = builder.comment("Size of violet_tunoradoite_ore Veins")
                    .translation("et.config.ore.violet_tunoradoite_ore_size")
                    .defineInRange("violet_tunoradoite_ore", 8, oreMin, oreMax);
            MAGENTA_ROSE_LOLLNIC_ORE_SIZE = builder.comment("Size of magenta_rose_lollnic_ore Veins")
                    .translation("et.config.ore.magenta_rose_lollnic_ore_size")
                    .defineInRange("magenta_rose_lollnic_ore", 8, oreMin, oreMax);
            JADE_PETAOGOPITE_ORE_SIZE = builder.comment("Size of jade_petaogopite_ore Veins")
                    .translation("et.config.ore.jade_petaogopite_ore_size")
                    .defineInRange("jade_petaogopite_ore", 8, oreMin, oreMax);

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

