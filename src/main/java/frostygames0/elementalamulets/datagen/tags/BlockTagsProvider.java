/*
 *  Copyright (c) 2021-2022
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

package frostygames0.elementalamulets.datagen.tags;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.init.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;


import javax.annotation.Nullable;

public class BlockTagsProvider extends net.minecraft.data.tags.BlockTagsProvider {
    public BlockTagsProvider(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, ElementalAmulets.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ModTags.Blocks.EARTH_AMULET_BOOSTABLE).addTags(BlockTags.FLOWERS, BlockTags.CROPS);
        this.tag(ModTags.Blocks.SHARD_BLOCKS).add(ModBlocks.ELEMENTAL_SHARDS_BLOCK.get(), ModBlocks.AIR_SHARDS_BLOCK.get(), ModBlocks.FIRE_SHARDS_BLOCK.get(), ModBlocks.WATER_SHARDS_BLOCK.get(), ModBlocks.EARTH_SHARDS_BLOCK.get());
        this.tag(ModTags.Blocks.ELEMENTAL_ORE).add(ModBlocks.ELEMENTAL_ORE.get(), ModBlocks.DEEPSLATE_ELEMENTAL_ORE.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.ELEMENTAL_COMBINATOR.get()).addTags(ModTags.Blocks.SHARD_BLOCKS, ModTags.Blocks.ELEMENTAL_ORE);
        this.tag(BlockTags.NEEDS_STONE_TOOL).addTag(ModTags.Blocks.ELEMENTAL_ORE);
    }
}
