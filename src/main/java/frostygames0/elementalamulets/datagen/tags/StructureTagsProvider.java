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
import frostygames0.elementalamulets.init.ModTags;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagManager;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class StructureTagsProvider extends TagsProvider<ConfiguredStructureFeature<?, ?>> {
    public StructureTagsProvider(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ElementalAmulets.MOD_ID, existingFileHelper, TagManager.getTagDir(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.key()).substring(5));
    }

    @Override
    protected void addTags() {
        this.tag(ModTags.Structures.ON_JEWELLER_MAP).add(ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, modPrefix("cult_temple")));
    }

    @Override
    public String getName() {
        return "Configured Structure Feature Tags";
    }
}
