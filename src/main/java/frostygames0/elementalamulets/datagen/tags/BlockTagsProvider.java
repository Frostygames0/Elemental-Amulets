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

package frostygames0.elementalamulets.datagen.tags;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.init.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;


import javax.annotation.Nullable;

/**
 * @author Frostygames0
 * @date 05.12.2021 16:49
 */
public class BlockTagsProvider extends net.minecraft.data.BlockTagsProvider {
    public BlockTagsProvider(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, ElementalAmulets.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ModTags.Blocks.EARTH_AMULET_BOOSTABLE).addTags(BlockTags.FLOWERS, BlockTags.CROPS);
    }
}
