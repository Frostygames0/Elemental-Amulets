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

package frostygames0.elementalamulets.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;


/**
 * @author Frostygames0
 * @date 30.01.2022 1:13
 */
public final class WorldUtil {
    public static ResourceKey<Biome> getBiomeAtPos(Level level, BlockPos pos) {
        return level.getBiome(pos).unwrapKey().orElseThrow(() -> new NullPointerException("Cannot identify biome at: " + level.dimension().getRegistryName() + ", pos: [" + pos.toShortString() + "]!"));
    }

    public static boolean isType(ResourceKey<Biome> biome, BiomeDictionary.Type... types) {
        for (var type : types) {
            if (!BiomeDictionary.hasType(biome, type))
                return false;
        }
        return true;
    }

    public static boolean isNatural(Level level, BlockPos pos) {
        ResourceKey<Biome> biome = getBiomeAtPos(level, pos);
        return (level.dimensionType().natural() || BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD))
                && !isType(biome, BiomeDictionary.Type.DRY, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.DEAD);
    }
}
