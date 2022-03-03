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

package frostygames0.elementalamulets.init;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.common.Tags;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> NECKLACES = tag("necklaces");
        public static final TagKey<Item> ELEMENTS = tag("elements");
        public static final TagKey<Item> SHARD_BLOCKS = tag("shard_blocks");

        public static final TagKey<Item> FIRE_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID + "/fire_element_convertible");
        public static final TagKey<Item> WATER_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID + "/water_element_convertible");
        public static final TagKey<Item> AIR_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID + "/air_element_convertible");
        public static final TagKey<Item> EARTH_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID + "/earth_element_convertible");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(modPrefix(name));
        }


        private static TagKey<Item> forge(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }
    }

    public static class Blocks {

        public static final TagKey<Block> EARTH_AMULET_BOOSTABLE = forge(ElementalAmulets.MOD_ID + "/earth_amulet_boostable");
        public static final TagKey<Block> SHARD_BLOCKS = tag("shard_blocks");
        public static final TagKey<Block> ELEMENTAL_ORE = tag("elemental_ore");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(modPrefix(name));
        }


        private static TagKey<Block> forge(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }
    }

    public static class Structures {
        public static final TagKey<ConfiguredStructureFeature<?, ?>> ON_JEWELLER_MAP = create("on_jeweller_map");

        private static TagKey<ConfiguredStructureFeature<?, ?>> create(String name) {
            return TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, modPrefix(name));
        }
    }

}
