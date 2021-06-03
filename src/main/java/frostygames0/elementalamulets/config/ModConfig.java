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
        private final ForgeConfigSpec.IntValue FIRE_AMULET_USAGE_DMG;
        private final ForgeConfigSpec.IntValue SPEED_AMULET_USAGE_DMG;
        private final ForgeConfigSpec.IntValue JUMP_AMULET_USAGE_DMG;
        private final ForgeConfigSpec.IntValue INVISIBILITY_AMULET_USAGE_DMG;

        private final ForgeConfigSpec.BooleanValue GIVE_GUIDE_ON_FIRST_JOIN;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("General");
            GIVE_GUIDE_ON_FIRST_JOIN = builder.comment("Will player receive guide on first join").define("give_guide_on_join", false);
            builder.push("Amulets [NOT WORKING]");
            FIRE_AMULET_USAGE_DMG = builder.comment("Defines damage that fire amulet will receive on it's usage [WARNING! Setting this too high can cause unexpected behaviour!]")
                    .defineInRange("fire_amulet_usage_dmg", 1, 0, 1000);
            SPEED_AMULET_USAGE_DMG = builder.comment("Defines damage that speed amulet will receive on it's usage [WARNING! Setting this too high can cause unexpected behaviour!]")
                    .defineInRange("speed_amulet_usage_dmg", 1, 0, 1000);
            JUMP_AMULET_USAGE_DMG = builder.comment("Defines damage that jump amulet will receive on it's usage [WARNING! Setting this too high can cause unexpected behaviour!]")
                    .defineInRange("jump_amulet_usage_dmg", 5, 0, 1000);
            INVISIBILITY_AMULET_USAGE_DMG = builder.comment("Defines damage that invisibility amulet will receive on it's usage [WARNING! Setting this too high can cause unexpected behaviour!]")
                    .defineInRange("invisibility_amulet_usage_dmg", 1, 0, 1000);

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
        private final ForgeConfigSpec.BooleanValue DISPLAY_TOTEM_LIKE_ANIM_ONBREAK;
        private final ForgeConfigSpec.BooleanValue USE_LATIN_ELEMENT_NAMES;
        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("General");
            DISPLAY_TOTEM_LIKE_ANIM_ONBREAK = builder.comment("Display totem like animation when amulet gets destroyed?").define("amulet_totem_like_anim", true);
            USE_LATIN_ELEMENT_NAMES = builder.comment("If true, elements names will be in latin (Ignis, Aer, Aqua and Terra)").define("elements_latin_variant", false);
            builder.pop();
        }
    }
    public static class cached {
        public static int FIRE_AMULET_USAGE_DMG;
        public static int SPEED_AMULET_USAGE_DMG;
        public static int JUMP_AMULET_USAGE_DMG;
        public static int INVISIBILITY_AMULET_USAGE_DMG;
        public static boolean GIVE_GUIDE_ON_FIRST_JOIN;
        private static void bakeServerConfig() {
            FIRE_AMULET_USAGE_DMG = SERVER.FIRE_AMULET_USAGE_DMG.get();
            SPEED_AMULET_USAGE_DMG = SERVER.SPEED_AMULET_USAGE_DMG.get();
            JUMP_AMULET_USAGE_DMG = SERVER.JUMP_AMULET_USAGE_DMG.get();
            INVISIBILITY_AMULET_USAGE_DMG = SERVER.INVISIBILITY_AMULET_USAGE_DMG.get();
            GIVE_GUIDE_ON_FIRST_JOIN = SERVER.GIVE_GUIDE_ON_FIRST_JOIN.get();

        }
        public static boolean DISPLAY_TOTEM_LIKE_ANIM_ONBREAK;
        public static boolean USE_LATIN_ELEMENT_NAMES;
        private static void bakeClientConfig() {
            DISPLAY_TOTEM_LIKE_ANIM_ONBREAK = CLIENT.DISPLAY_TOTEM_LIKE_ANIM_ONBREAK.get();
            USE_LATIN_ELEMENT_NAMES = CLIENT.USE_LATIN_ELEMENT_NAMES.get();
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
