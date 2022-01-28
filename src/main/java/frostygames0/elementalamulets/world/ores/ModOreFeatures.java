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

package frostygames0.elementalamulets.world.ores;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.init.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.common.Mod;


import java.util.List;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ModOreFeatures {
    private static final Lazy<List<OreConfiguration.TargetBlockState>> ELEMENTAL_ORE_TARGETS = Lazy.of(() -> List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.ELEMENTAL_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_ELEMENTAL_ORE.get().defaultBlockState())));
    public static final Lazy<ConfiguredFeature<?, ?>> ELEMENTAL_ORE = Lazy.of(() -> Feature.ORE.configured(
            new OreConfiguration(ELEMENTAL_ORE_TARGETS.get(), 9)));

    public static void register() {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, modPrefix("elemental_ore"), ELEMENTAL_ORE.get());
    }
}