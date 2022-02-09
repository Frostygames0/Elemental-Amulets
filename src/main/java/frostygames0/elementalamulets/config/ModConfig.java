/*
 *  Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.config;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.client.patchouli.ElementalGuideConfigFlags;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
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
        private final ForgeConfigSpec.BooleanValue fancyCombination;
        private final ForgeConfigSpec.BooleanValue oldFashionedWay;

        private final ForgeConfigSpec.DoubleValue jumpAmuletBoost;
        private final ForgeConfigSpec.DoubleValue fireAmuletFireResistance;
        private final ForgeConfigSpec.DoubleValue fireAmuletLavaResistance;
        private final ForgeConfigSpec.DoubleValue speedAmuletBoost;
        private final ForgeConfigSpec.DoubleValue protectionAmuletReflectDamageMult;
        private final ForgeConfigSpec.IntValue protectionAmuletChargeTime;
        private final ForgeConfigSpec.BooleanValue protectionAmuletIgnoreNaturality;
        private final ForgeConfigSpec.DoubleValue waterAmuletSpeedBoost;
        private final ForgeConfigSpec.IntValue earthAmuletCooldown;
        private final ForgeConfigSpec.BooleanValue earthAmuletIgnoreNaturality;
        private final ForgeConfigSpec.IntValue pacifyingAmuletBreakCooldown;
        private final ForgeConfigSpec.BooleanValue pacifyingAmuletAngerOnbreak;
        private final ForgeConfigSpec.IntValue pacifyingAmuletDisorientationTime;
        private final ForgeConfigSpec.DoubleValue knockbackAmuletKnockbackMult;
        private final ForgeConfigSpec.BooleanValue modifyVanillaLoot;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("Loot");
            modifyVanillaLoot = builder.comment("Add mod items to the vanilla loot tables [DEFAULT: true]").define("modify_vanilla_loot", true);
            builder.pop();

            builder.push("Elemental Combinator");
            oldFashionedWay = builder.comment("Allow usage of old combination procedure(Shift-Click on the block) [DEFAULT: false]").define("elemental_combinator_old_way", false);
            fancyCombination = builder.comment("Add special effects when Elemental Combinator is working [DEFAULT: true]").define("elemental_combinator_special_effects", true);
            builder.pop();

            builder.comment("Anything that is related to the amulets [WARNING! Setting any of these values too high may cause bugs, lags and even crashes]")
                    .push("Amulets");

            builder.push("Amulet of Jump-Boost");
            jumpAmuletBoost = builder.comment("How high will players jump with Jump Amulet [DEFAULT: 0.3]").defineInRange("jump_amulet_boost", 0.3, 0, Float.MAX_VALUE);
            builder.pop();

            builder.push("Amulet of Fire-Resistance");
            fireAmuletFireResistance = builder.comment("How good will Amulet of Fire-Resistance protect from fire [DEFAULT: 0.5]").defineInRange("fire_amulet_fire_resistance", 0.5, 0, Float.MAX_VALUE);
            fireAmuletLavaResistance = builder.comment("How good will Amulet of Fire-Resistance protect from lava [DEFAULT: 0.25]").defineInRange("fire_amulet_lava_resistance", 0.25, 0, Float.MAX_VALUE);
            builder.pop();

            builder.push("Amulet of Speed-Boost");
            speedAmuletBoost = builder.comment("How fast will players run with Amulet of Speed-Boost [DEFAULT: 1.08]").defineInRange("speed_amulet_boost", 1.08, 0, Float.MAX_VALUE);
            builder.pop();

            builder.push("Amulet of Terra-Protection");
            protectionAmuletReflectDamageMult = builder.comment("How much damage will Leaf Shield absorb? [DEFAULT: 0.5]").defineInRange("protection_amulet_absorption", 0.5f, 0, Float.MAX_VALUE);
            protectionAmuletChargeTime = builder.comment("How long will leaf shield recharge one bar (in ticks, 1 sec - 20 ticks)? [DEFAULT: 80]").defineInRange("protection_amulet_recharge_time", 80, 0, Integer.MAX_VALUE);
            protectionAmuletIgnoreNaturality = ignoreNaturality(builder, "protection_amulet");
            builder.pop();

            builder.push("Amulet of Water");
            waterAmuletSpeedBoost = builder.comment("How fast will players swim with Water Amulet [DEFAULT 0.5]").defineInRange("water_amulet_speed_boost", 0.5f, 0, Float.MAX_VALUE);
            builder.pop();

            builder.push("Amulet of Earth");
            earthAmuletCooldown = builder.comment("How long will the Amulet of Earth be on cooldown? (in ticks, 1 sec - 20 ticks) [DEFAULT: 100]").defineInRange("earth_amulet_cooldown", 100, 0, Integer.MAX_VALUE);
            earthAmuletIgnoreNaturality = ignoreNaturality(builder, "earth_amulet");
            builder.pop();

            builder.push("Pacifying Amulet");
            pacifyingAmuletDisorientationTime = builder.comment("How long will the enemy  be disoriented (if enemy can't be pacified)? (in ticks, 1 sec - 20 ticks) [DEFAULT: 100]").defineInRange("pacifying_amulet_tiredness_time", 100, 0, Short.MAX_VALUE);
            pacifyingAmuletBreakCooldown = builder.comment("How long will you need to wait until you can use Pacifying Amulet when broken (in ticks, 1 sec - 20 ticks) [DEFAULT: 200]").defineInRange("pacifying_amulet_break_cooldown", 200, 0, Integer.MAX_VALUE);
            pacifyingAmuletAngerOnbreak = builder.comment("Anger all nearby 'angerable'(wolves, golems, etc) entities when pacifying amulet breaks? [DEFAULT: true]").define("pacifying_amulet_anger_onbreak", true);

            builder.pop();

            builder.push("Knockback Amulet");
            knockbackAmuletKnockbackMult = builder.comment("How strong will knockback be? [DEFAULT: 0.5]").defineInRange("knockback_amulet_knockback_mult", 0.5, 0, Double.MAX_VALUE);

            builder.pop(2);
        }

        private static ForgeConfigSpec.BooleanValue ignoreNaturality(ForgeConfigSpec.Builder builder, String name) {
            return builder.comment("Ignore 'naturality' of the dimension player is in? (compass behaviour, bed, etc.) [DEFAULT: false]").define(name + "_ignore_naturality", false);
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
        private final ForgeConfigSpec.BooleanValue useLatinElementNames;
        private final ForgeConfigSpec.BooleanValue amuletsTierDifference;
        private final ForgeConfigSpec.BooleanValue renderCombinatorStack;
        private final ForgeConfigSpec.BooleanValue renderLeafShield;
        private final ForgeConfigSpec.BooleanValue renderLeafChargeOverlay;
        private final ForgeConfigSpec.BooleanValue showSplashes;

        public Client(ForgeConfigSpec.Builder builder) {
            showSplashes = builder.comment("Show custom splash(es)? [DEFAULT: true]").translation("config.elementalamulets.show_splashes").define("show_splashes", true);
            useLatinElementNames = builder.comment("Use latin names for the elements(Ignis, Aer, Aqua, Terra and Aether) [DEFAULT: false]").translation("config.elementalamulets.elements_latin_variant").define("elements_latin_variant", false);
            builder.push("Rendering");
            renderCombinatorStack = builder.comment("Render output slot on the top Elemental Combinator? [DEFAULT: true]").translation("config.elementalamulets.elemental_combinator_stack_display").define("render_elemental_combinator_stack", true);

            builder.push("Amulets");

            builder.push("Amulet of Terra-Protection");
            renderLeafChargeOverlay = builder.comment("Render a leaf charge overlay? [DEFAULT: true]").translation("config.elementalamulets.render_leaf_charge_overlay").define("render_leaf_charge_overlay", true);
            renderLeafShield = builder.comment("Render a leaf shield around the player? [DEFAULT: true]").translation("config.elementalamulets.render_leaf_shield").define("render_leaf_shield", true);
            builder.pop();

            amuletsTierDifference = builder.comment("Set to true, if you want amulets to be different based on their tier [DEFAULT: true]").translation("config.elementalamulets.amulets_tier_difference").define("amulets_tier_difference", true);
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
        private final ForgeConfigSpec.BooleanValue generateCultTemple;
        private final ForgeConfigSpec.BooleanValue generateJewellerHouse;
        private final ForgeConfigSpec.BooleanValue generateOres;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("World generation");
            generateCultTemple = builder.comment("Generate Cult's temple ruins? [DEFAULT: true, requires world restart]").translation("config.elementalamulets.generate_cult_temple").worldRestart().define("generate_cult_temple", true);
            generateJewellerHouse = builder.comment("Generate Jeweller's house? [DEFAULT: true, requires world restart]").translation("config.elementalamulets.generate_jeweller_house").worldRestart().define("generate_jeweller_house", true);
            generateOres = builder.comment("Generate Elemental Shards ore? [DEFAULT: true, requires world restart]").translation("config.elementalamulets.generate_ores").worldRestart().define("generate_ores", true);
            builder.pop();
        }
    }

    // ---------------------------------------- //
    //           Config Caching                 //
    // ---------------------------------------- //

    public static class CachedValues {
        public static boolean FANCY_COMBINATION;
        public static boolean OLD_FASHIONED_WAY;
        public static double JUMP_AMULET_BOOST;
        public static double FIRE_AMULET_FIRE_RESISTANCE;
        public static double FIRE_AMULET_LAVA_RESISTANCE;
        public static double SPEED_AMULET_BOOST;
        public static double PROTECTION_AMULET_REFLECT_DAMAGE_MULT;
        public static int PROTECTION_AMULET_CHARGE_TIME;
        public static boolean PROTECTION_AMULET_IGNORE_NATURALITY;
        public static double WATER_AMULET_SPEED_BOOST;
        public static int EARTH_AMULET_COOLDOWN;
        public static boolean EARTH_AMULET_IGNORE_NATURALITY;
        public static int PACIFYING_AMULET_BREAK_COOLDOWN;
        public static boolean PACIFYING_AMULET_ANGER_ONBREAK;
        public static int PACIFYING_AMULET_DISORIENTATION_TIME;
        public static double KNOCKBACK_AMULET_KNOCKBACK_MULT;
        public static boolean MODIFY_VANILLA_LOOT;

        private static void cacheServerConfig() {
            FANCY_COMBINATION = SERVER.fancyCombination.get();
            OLD_FASHIONED_WAY = SERVER.oldFashionedWay.get();
            JUMP_AMULET_BOOST = SERVER.jumpAmuletBoost.get();
            FIRE_AMULET_FIRE_RESISTANCE = SERVER.fireAmuletFireResistance.get();
            FIRE_AMULET_LAVA_RESISTANCE = SERVER.fireAmuletLavaResistance.get();
            SPEED_AMULET_BOOST = SERVER.speedAmuletBoost.get();
            PROTECTION_AMULET_REFLECT_DAMAGE_MULT = SERVER.protectionAmuletReflectDamageMult.get();
            PROTECTION_AMULET_CHARGE_TIME = SERVER.protectionAmuletChargeTime.get();
            PROTECTION_AMULET_IGNORE_NATURALITY = SERVER.protectionAmuletIgnoreNaturality.get();
            WATER_AMULET_SPEED_BOOST = SERVER.waterAmuletSpeedBoost.get();
            EARTH_AMULET_COOLDOWN = SERVER.earthAmuletCooldown.get();
            EARTH_AMULET_IGNORE_NATURALITY = SERVER.earthAmuletIgnoreNaturality.get();
            MODIFY_VANILLA_LOOT = SERVER.modifyVanillaLoot.get();
            PACIFYING_AMULET_BREAK_COOLDOWN = SERVER.pacifyingAmuletBreakCooldown.get();
            PACIFYING_AMULET_ANGER_ONBREAK = SERVER.pacifyingAmuletAngerOnbreak.get();
            PACIFYING_AMULET_DISORIENTATION_TIME = SERVER.pacifyingAmuletDisorientationTime.get();
            KNOCKBACK_AMULET_KNOCKBACK_MULT = SERVER.knockbackAmuletKnockbackMult.get();
        }

        public static boolean AMULETS_TIER_DIFFERENCE;
        public static boolean USE_LATIN_ELEMENT_NAMES;
        public static boolean RENDER_COMBINATOR_STACK;
        public static boolean RENDER_LEAF_SHIELD;
        public static boolean RENDER_LEAF_CHARGE_OVERLAY;
        public static boolean SHOW_SPLASHES;

        private static void cacheClientConfig() {
            USE_LATIN_ELEMENT_NAMES = CLIENT.useLatinElementNames.get();
            AMULETS_TIER_DIFFERENCE = CLIENT.amuletsTierDifference.get();
            RENDER_COMBINATOR_STACK = CLIENT.renderCombinatorStack.get();
            RENDER_LEAF_SHIELD = CLIENT.renderLeafShield.get();
            RENDER_LEAF_CHARGE_OVERLAY = CLIENT.renderLeafChargeOverlay.get();
            SHOW_SPLASHES = CLIENT.showSplashes.get();
        }

        public static boolean GENERATE_CULT_TEMPLE;
        public static boolean GENERATE_JEWELLER_HOUSE;
        public static boolean GENERATE_ORES;

        private static void cacheCommonConfig() {
            GENERATE_CULT_TEMPLE = COMMON.generateCultTemple.get();
            GENERATE_JEWELLER_HOUSE = COMMON.generateJewellerHouse.get();
            GENERATE_ORES = COMMON.generateOres.get();
        }
    }

    @SubscribeEvent
    public static void configEvent(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == ModConfig.SERVER_SPEC) {
            CachedValues.cacheServerConfig();
        }
        if (event.getConfig().getSpec() == ModConfig.CLIENT_SPEC) {
            CachedValues.cacheClientConfig();
        }
        if (event.getConfig().getSpec() == ModConfig.COMMON_SPEC) {
            CachedValues.cacheCommonConfig();
        }
        // Updates config flags in patchouli
        ElementalGuideConfigFlags.updateConfigFlags();
    }

}
