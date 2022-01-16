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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ModTags {
    public static class Items {
        public static final Tag.Named<Item> NECKLACES = tag("necklaces");
        public static final Tag.Named<Item> ELEMENTS = tag("elements");
        public static final Tag.Named<Item> SHARD_BLOCKS = tag("shard_blocks");

        public static final Tag.Named<Item> FIRE_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID + "/fire_element_convertible");
        public static final Tag.Named<Item> WATER_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID + "/water_element_convertible");
        public static final Tag.Named<Item> AIR_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID + "/air_element_convertible");
        public static final Tag.Named<Item> EARTH_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID + "/earth_element_convertible");

        private static Tag.Named<Item> tag(String name) {
            return ItemTags.bind(modPrefix(name).toString());
        }

        private static Tag.Named<Item> forge(String name) {
            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }
    }

    public static class Blocks {

        public static final Tag.Named<Block> EARTH_AMULET_BOOSTABLE = forge(ElementalAmulets.MOD_ID + "/earth_amulet_boostable");
        public static final Tag.Named<Block> SHARD_BLOCKS = tag("shard_blocks");

        private static Tag.Named<Block> tag(String name) {
            return BlockTags.bind(modPrefix(name).toString());
        }

        private static Tags.IOptionalNamedTag<Block> forge(String name) {
            return BlockTags.createOptional(new ResourceLocation("forge", name));
        }
    }

}
