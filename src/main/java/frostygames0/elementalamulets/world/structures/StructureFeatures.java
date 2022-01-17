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

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class StructureFeatures {
    public static final ConfiguredStructureFeature<?, ?> CONFIGURED_CULT_TEMPLE = ModStructures.CULT_TEMPLE.get().configured(new JigsawConfiguration(() -> PlainVillagePools.START, 0));

    public static void register() {
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, modPrefix("cult_temple"), CONFIGURED_CULT_TEMPLE);
    }
}
