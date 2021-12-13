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

package frostygames0.elementalamulets.world.structures;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/**
 * @author Frostygames0
 * @date 29.09.2021 15:46
 */
public class StructureFeatures {
    public static StructureFeature<?, ?> CONFIGURED_CULT_TEMPLE = ModStructures.CULT_TEMPLE.get().configured(IFeatureConfig.NONE);

    public static void register() {
        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, modPrefix("cult_temple"), CONFIGURED_CULT_TEMPLE);

        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.CULT_TEMPLE.get(), CONFIGURED_CULT_TEMPLE);
    }
}
