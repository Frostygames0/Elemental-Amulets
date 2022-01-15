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
/*
package frostygames0.elementalamulets.world;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID)
public class ModFeatures {
    public static Lazy<ConfiguredFeature<?, ?>> ELEMENTAL_ORE = Lazy.of(() -> Feature.ORE.configured(
            new OreConfiguration(OreFeatures.NATURAL_STONE, ModBlocks.ELEMENTAL_ORE.get().defaultBlockState(), 9)));

    public static void register() {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, modPrefix("elemental_ore"), ELEMENTAL_ORE.get());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void oreGeneration(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();
        if (!event.getCategory().equals(Biome.BiomeCategory.NETHER) && !event.getCategory().equals(Biome.BiomeCategory.THEEND)) {
            if (ModConfig.CachedValues.GENERATE_ORES)
                gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModFeatures.ELEMENTAL_ORE.get().placed());
        }
    }
}
*/