/*
 *     Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.config;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfig {

    // Server Config
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final Server SERVER;
    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static class Server {
        private final ForgeConfigSpec.BooleanValue FANCY_COMBINATION;

        private final ForgeConfigSpec.DoubleValue JUMP_AMULET_BOOST;
        private final ForgeConfigSpec.DoubleValue FIRE_AMULET_FIRE_RESISTANCE;
        private final ForgeConfigSpec.DoubleValue FIRE_AMULET_LAVA_RESISTANCE;
        private final ForgeConfigSpec.DoubleValue SPEED_AMULET_BOOST;
        private final ForgeConfigSpec.DoubleValue PROTECTION_AMULET_REFLECT_DAMAGE_MULT;
        private final ForgeConfigSpec.IntValue PROTECTION_AMULET_CHARGE_TIME;
        private final ForgeConfigSpec.DoubleValue WATER_AMULET_SPEED_BOOST;
        private final ForgeConfigSpec.IntValue EARTH_AMULET_COOLDOWN;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("General");
            FANCY_COMBINATION = builder.comment("Add special effects when Elemental Combinator is working [DEFAULT: true]").define("elemental_combinator_special_effects", true);

            builder.comment("Anything that is related to the amulets [WARNING! Setting any of these values too high may cause bugs, lags and even crashes]")
                    .push("Amulets");


            builder.push("Amulet of Jump-Boost");
            JUMP_AMULET_BOOST = builder.comment("How high will players jump with Jump Amulet [DEFAULT: 0.3]").defineInRange("jump_amulet_boost", 0.3, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.push("Amulet of Fire-Resistance");
            FIRE_AMULET_FIRE_RESISTANCE = builder.comment("How good will Amulet of Fire-Resistance protect from fire [DEFAULT: 0.5]").defineInRange("fire_amulet_fire_resistance", 0.5, 0, Integer.MAX_VALUE);
            FIRE_AMULET_LAVA_RESISTANCE = builder.comment("How good will Amulet of Fire-Resistance protect from lava [DEFAULT: 0.25]").defineInRange("fire_amulet_lava_resistance", 0.25, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.push("Amulet of Speed-Boost");
            SPEED_AMULET_BOOST = builder.comment("How fast will players run with Amulet of Speed-Boost [DEFAULT: 1.08]").defineInRange("speed_amulet_boost", 1.08, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.push("Amulet of Terra-Protection");
            PROTECTION_AMULET_REFLECT_DAMAGE_MULT = builder.comment("How much damage will Leaf Shield absorb? [DEFAULT: 0.5]") .defineInRange("protection_amulet_absorption", 0.5f, 0, Integer.MAX_VALUE);
            PROTECTION_AMULET_CHARGE_TIME = builder.comment("How long will leaf shield recharge one bar (in ticks)? [DEFAULT: 80]").defineInRange("protection_amulet_recharge_time", 80, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.push("Amulet of Water");
            WATER_AMULET_SPEED_BOOST = builder.comment("How fast will players swim with Water Amulet [DEFAULT 0.5]").defineInRange("water_amulet_speed_boost", 0.5f, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.push("Amulet of Earth");
            EARTH_AMULET_COOLDOWN = builder.comment("How long will the Amulet of Earth be on cooldown? (in ticks, 1 sec - 20 ticks) [DEFAULT: 100]").defineInRange("earth_amulet_cooldown", 100, 0, Integer.MAX_VALUE);

            builder.pop(3);
        }
    }

    // Client Config
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;
    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class Client {
        private final ForgeConfigSpec.BooleanValue USE_LATIN_ELEMENT_NAMES;
        private final ForgeConfigSpec.BooleanValue AMULETS_TIER_DIFFERENCE;
        private final ForgeConfigSpec.BooleanValue RENDER_COMBINATOR_STACK;
        private final ForgeConfigSpec.BooleanValue RENDER_LEAF_SHIELD;
        private final ForgeConfigSpec.BooleanValue RENDER_LEAF_CHARGE_OVERLAY;
        private final ForgeConfigSpec.BooleanValue SHOW_SPLASHES;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("General");
            USE_LATIN_ELEMENT_NAMES = builder.comment("Use latin names for the elements(Ignis, Aer, Aqua, Terra and Aether) [DEFAULT: false]").translation("config.elementalamulets.elements_latin_variant").define("elements_latin_variant", false);
            SHOW_SPLASHES = builder.comment("Show custom splash(es)? [DEFAULT: true]").translation("config.elementalamulets.show_splashes").define("show_splashes", true);
            builder.push("Rendering");
            RENDER_COMBINATOR_STACK = builder.comment("Render output slot on the top Elemental Combinator? [DEFAULT: true]").translation("config.elementalamulets.elemental_combinator_stack_display").define("render_elemental_combinator_stack", true);

            builder.push("Amulets");

            builder.push("Amulet of Terra-Protection");
            RENDER_LEAF_CHARGE_OVERLAY = builder.comment("Render a leaf charge overlay? [DEFAULT: true]").translation("config.elementalamulets.render_leaf_charge_overlay").define("render_leaf_charge_overlay", true);
            RENDER_LEAF_SHIELD = builder.comment("Render a leaf shield around the player? [DEFAULT: true]").translation("config.elementalamulets.render_leaf_shield").define("render_leaf_shield", true);
            builder.pop();

            AMULETS_TIER_DIFFERENCE = builder.comment("Set to true, if you want amulets to be different based on their tier [DEFAULT: true]").translation("config.elementalamulets.amulets_tier_difference").define("amulets_tier_difference", true);
            builder.pop();

            builder.pop();

            builder.pop();
        }
    }

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
    public static class Common {
        private final ForgeConfigSpec.BooleanValue GENERATE_CULT_TEMPLE;
        private final ForgeConfigSpec.BooleanValue GENERATE_JEWELLER_HOUSE;
        private final ForgeConfigSpec.BooleanValue GENERATE_ORES;
        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("World generation");
            GENERATE_CULT_TEMPLE = builder.comment("Generate Cult's temple ruins? [DEFAULT: true]").translation("config.elementalamulets.generate_cult_temple").define("generate_cult_temple", true);
            GENERATE_JEWELLER_HOUSE = builder.comment("Generate Jeweller's house? [DEFAULT: true]").translation("config.elementalamulets.generate_jeweller_house").define("generate_jeweller_house", true);
            GENERATE_ORES = builder.comment("Generate Elemental Shards ore? [DEFAULT: true]").translation("config.elementalamulets.generate_ores").define("generate_ores", true);
            builder.pop();
        }
    }

    // ---------------------------------------- //
    //           Config Caching                 //
    // ---------------------------------------- //

    public static class CachedValues {
        public static boolean FANCY_COMBINATION;
        public static double JUMP_AMULET_BOOST;
        public static double FIRE_AMULET_FIRE_RESISTANCE;
        public static double FIRE_AMULET_LAVA_RESISTANCE;
        public static double SPEED_AMULET_BOOST;
        public static double PROTECTION_AMULET_REFLECT_DAMAGE_MULT;
        public static int PROTECTION_AMULET_CHARGE_TIME;
        public static double WATER_AMULET_SPEED_BOOST;
        public static int EARTH_AMULET_COOLDOWN;

        private static void cacheServerConfig() {
            FANCY_COMBINATION = SERVER.FANCY_COMBINATION.get();
            JUMP_AMULET_BOOST = SERVER.JUMP_AMULET_BOOST.get();
            FIRE_AMULET_FIRE_RESISTANCE = SERVER.FIRE_AMULET_FIRE_RESISTANCE.get();
            FIRE_AMULET_LAVA_RESISTANCE = SERVER.FIRE_AMULET_LAVA_RESISTANCE.get();
            SPEED_AMULET_BOOST = SERVER.SPEED_AMULET_BOOST.get();
            PROTECTION_AMULET_REFLECT_DAMAGE_MULT = SERVER.PROTECTION_AMULET_REFLECT_DAMAGE_MULT.get();
            PROTECTION_AMULET_CHARGE_TIME = SERVER.PROTECTION_AMULET_CHARGE_TIME.get();
            WATER_AMULET_SPEED_BOOST = SERVER.WATER_AMULET_SPEED_BOOST.get();
            EARTH_AMULET_COOLDOWN = SERVER.EARTH_AMULET_COOLDOWN.get();
        }

        public static boolean AMULETS_TIER_DIFFERENCE;
        public static boolean USE_LATIN_ELEMENT_NAMES;
        //public static int COMBINATOR_STACK_ROTATION_SPEED;
        public static boolean RENDER_COMBINATOR_STACK;
        public static boolean RENDER_LEAF_SHIELD;
        public static boolean RENDER_LEAF_CHARGE_OVERLAY;
        public static boolean SHOW_SPLASHES;

        private static void cacheClientConfig() {
            USE_LATIN_ELEMENT_NAMES = CLIENT.USE_LATIN_ELEMENT_NAMES.get();
            AMULETS_TIER_DIFFERENCE = CLIENT.AMULETS_TIER_DIFFERENCE.get();
            RENDER_COMBINATOR_STACK = CLIENT.RENDER_COMBINATOR_STACK.get();
            RENDER_LEAF_SHIELD = CLIENT.RENDER_LEAF_SHIELD.get();
            RENDER_LEAF_CHARGE_OVERLAY = CLIENT.RENDER_LEAF_CHARGE_OVERLAY.get();
            SHOW_SPLASHES = CLIENT.SHOW_SPLASHES.get();
        }

        public static boolean GENERATE_CULT_TEMPLE;
        public static boolean GENERATE_JEWELLER_HOUSE;
        public static boolean GENERATE_ORES;

        private static void cacheCommonConfig() {
            GENERATE_CULT_TEMPLE = COMMON.GENERATE_CULT_TEMPLE.get();
            GENERATE_JEWELLER_HOUSE = COMMON.GENERATE_JEWELLER_HOUSE.get();
            GENERATE_ORES = COMMON.GENERATE_ORES.get();
        }
    }

    @SubscribeEvent
    public static void configEvent(net.minecraftforge.fml.config.ModConfig.ModConfigEvent event) {
        if(event.getConfig().getSpec() == ModConfig.SERVER_SPEC) {
            CachedValues.cacheServerConfig();
        }
        if(event.getConfig().getSpec() == ModConfig.CLIENT_SPEC) {
            CachedValues.cacheClientConfig();
        }
        if(event.getConfig().getSpec() == ModConfig.COMMON_SPEC) {
            CachedValues.cacheCommonConfig();
        }
    }

}
