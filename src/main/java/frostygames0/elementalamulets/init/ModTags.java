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
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ModTags {
    public static class Items {
        public static final ITag.INamedTag<Item> NECKLACES = tag("necklaces");
        public static final ITag.INamedTag<Item> ELEMENTS = tag("elements");
        public static final ITag.INamedTag<Item> SHARDS_BLOCKS = tag("shards_blocks");

        public static final ITag.INamedTag<Item> FIRE_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID + "/fire_element_convertible");
        public static final ITag.INamedTag<Item> WATER_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID + "/water_element_convertible");
        public static final ITag.INamedTag<Item> AIR_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID + "/air_element_convertible");
        public static final ITag.INamedTag<Item> EARTH_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID + "/earth_element_convertible");

        private static ITag.INamedTag<Item> tag(String name) {
            return ItemTags.bind(modPrefix(name).toString());
        }

        private static ITag.INamedTag<Item> forge(String name) {
            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }
    }

    public static class Blocks {

        public static final ITag.INamedTag<Block> EARTH_AMULET_BOOSTABLE = forge(ElementalAmulets.MOD_ID + "/earth_amulet_boostable");

        private static ITag.INamedTag<Block> tag(String name) {
            return BlockTags.bind(modPrefix(name).toString());
        }

        private static Tags.IOptionalNamedTag<Block> forge(String name) {
            return BlockTags.createOptional(new ResourceLocation("forge", name));
        }
    }

}
