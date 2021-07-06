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
        private final ForgeConfigSpec.DoubleValue PROTECTION_AMULET_ABSORPTION;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("General");
            FANCY_COMBINATION = builder.comment("Add special effects when Elemental Combinator is working [DEFAULT: true]").define("elemental_combinator_special_effects", true);

            builder.comment("Anything that is related to the amulets [WARNING! Setting any of these values too high may cause bugs, lags and even crashes]")
                    .push("Amulets");
            JUMP_AMULET_BOOST = builder.comment("How high will players jump with Jump Amulet [DEFAULT: 0.3]").defineInRange("jump_amulet_boost", 0.3, 0, Integer.MAX_VALUE);
            FIRE_AMULET_FIRE_RESISTANCE = builder.comment("How good will Fire Amulet protect from fire [DEFAULT: 0.5]").defineInRange("fire_amulet_fire_resistance", 0.5, 0, Integer.MAX_VALUE);
            FIRE_AMULET_LAVA_RESISTANCE = builder.comment("How good will Fire Amulet protect from lava [DEFAULT: 0.25]").defineInRange("fire_amulet_lava_resistance", 0.25, 0, Integer.MAX_VALUE);
            SPEED_AMULET_BOOST = builder.comment("How fast will players run with Speed Amulet [DEFAULT: 1.08]").defineInRange("speed_amulet_boost", 1.08, 0, Integer.MAX_VALUE);
            PROTECTION_AMULET_ABSORPTION = builder.comment("How much damage will Leaf Shild absorb? [DEFAULT: 3.0]") .defineInRange("protection_amulet_absorption", 3f, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.pop();
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
        private final ForgeConfigSpec.IntValue COMBINATOR_STACK_ROTATION_SPEED;
        private final ForgeConfigSpec.BooleanValue RENDER_COMBINATOR_STACK;
        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("General");
            USE_LATIN_ELEMENT_NAMES = builder.comment("If true, elements names will be in latin (Ignis, Aer, Aqua and Terra) [DEFAULT: false]").translation("config.elementalamulets.elements_latin_variant").define("elements_latin_variant", false);

            builder.push("Rendering");
            RENDER_COMBINATOR_STACK = builder.comment("Render output slot on top Elemental Combinator? [DEFAULT: true]").translation("config.elementalamulets.elemental_combinator_stack_display").define("elemental_combinator_stack_display", true);
            COMBINATOR_STACK_ROTATION_SPEED = builder.comment("How fast does rendered item spins? [DEFAULT: true]").translation("config.elementalamulets.elemental_combinator_stack_speed").defineInRange("elemental_combinator_stack_speed", 20, 1, Integer.MAX_VALUE);

            builder.push("Amulets");
            AMULETS_TIER_DIFFERENCE = builder.comment("Set to true, if you want amulets to be different based on their tier [DEFAULT: false]").translation("config.elementalamulets.amulets_tier_difference").define("amulets_tier_difference", true);
            builder.pop();

            builder.pop();

            builder.pop();
        }
    }
    public static class cached {
        public static boolean FANCY_COMBINATION;
        public static double JUMP_AMULET_BOOST;
        public static double FIRE_AMULET_FIRE_RESISTANCE;
        public static double FIRE_AMULET_LAVA_RESISTANCE;
        public static double SPEED_AMULET_BOOST;
        public static double PROTECTION_AMULET_ABSORPTION;

        private static void bakeServerConfig() {
            FANCY_COMBINATION = SERVER.FANCY_COMBINATION.get();
            JUMP_AMULET_BOOST = SERVER.JUMP_AMULET_BOOST.get();
            FIRE_AMULET_FIRE_RESISTANCE = SERVER.FIRE_AMULET_FIRE_RESISTANCE.get();
            FIRE_AMULET_LAVA_RESISTANCE = SERVER.FIRE_AMULET_LAVA_RESISTANCE.get();
            SPEED_AMULET_BOOST = SERVER.SPEED_AMULET_BOOST.get();
            PROTECTION_AMULET_ABSORPTION = SERVER.PROTECTION_AMULET_ABSORPTION.get();
        }

        public static boolean AMULETS_TIER_DIFFERENCE;
        public static boolean USE_LATIN_ELEMENT_NAMES;
        public static int COMBINATOR_STACK_ROTATION_SPEED;
        public static boolean RENDER_COMBINATOR_STACK;
        private static void bakeClientConfig() {
            USE_LATIN_ELEMENT_NAMES = CLIENT.USE_LATIN_ELEMENT_NAMES.get();
            AMULETS_TIER_DIFFERENCE = CLIENT.AMULETS_TIER_DIFFERENCE.get();
            COMBINATOR_STACK_ROTATION_SPEED = CLIENT.COMBINATOR_STACK_ROTATION_SPEED.get();
            RENDER_COMBINATOR_STACK = CLIENT.RENDER_COMBINATOR_STACK.get();
        }
    }

    @SubscribeEvent
    public static void configEvent(net.minecraftforge.fml.config.ModConfig.ModConfigEvent event) {
        if(event.getConfig().getSpec() == ModConfig.SERVER_SPEC) {
            cached.bakeServerConfig();
        }
        if(event.getConfig().getSpec() == ModConfig.CLIENT_SPEC) {
            cached.bakeClientConfig();
        }
    }

    @SubscribeEvent
    public static void onLoad(net.minecraftforge.fml.config.ModConfig.Loading event) {
        if(event.getConfig().getSpec() == ModConfig.SERVER_SPEC) {
            ElementalAmulets.LOGGER.debug("Server config file " + event.getConfig().getFileName() + " was loaded at " + event.getConfig().getFullPath());
        }
        if(event.getConfig().getSpec() == ModConfig.CLIENT_SPEC) {
            ElementalAmulets.LOGGER.debug("Client config file " + event.getConfig().getFileName() + " was loaded at " + event.getConfig().getFullPath());
        }
    }
}
