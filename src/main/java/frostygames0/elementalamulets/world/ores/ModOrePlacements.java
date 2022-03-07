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

package frostygames0.elementalamulets.world.ores;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import java.util.List;

/**
 * @author Frostygames0
 * @date 16.01.2022 4:22
 */
@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID)
public class ModOrePlacements {
    public static final Lazy<Holder<PlacedFeature>> ELEMENTAL_ORE_UPPER = Lazy.of(() -> PlacementUtils.register(ElementalAmulets.MOD_ID + ":elemental_ore_upper",
            ModOreFeatures.ELEMENTAL_ORE.get(), orePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384)))));
    public static final Lazy<Holder<PlacedFeature>> ELEMENTAL_ORE_MIDDLE = Lazy.of(() -> PlacementUtils.register(ElementalAmulets.MOD_ID + ":elemental_ore_middle",
            ModOreFeatures.ELEMENTAL_ORE.get(), orePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56)))));
    public static final Lazy<Holder<PlacedFeature>> ELEMENTAL_ORE_SMALL = Lazy.of(() -> PlacementUtils.register(ElementalAmulets.MOD_ID + ":elemental_ore_small",
            ModOreFeatures.ELEMENTAL_ORE_SMALL.get(), orePlacement(9, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72)))));

    private static List<PlacementModifier> orePlacement(int count, PlacementModifier modifier) {
        return List.of(CountPlacement.of(count), InSquarePlacement.spread(), modifier, BiomeFilter.biome());
    }

    public static void register() {
        ELEMENTAL_ORE_UPPER.get();
        ELEMENTAL_ORE_MIDDLE.get();
        ELEMENTAL_ORE_SMALL.get();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void oreGeneration(final BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();
        if (!event.getCategory().equals(Biome.BiomeCategory.NETHER) && !event.getCategory().equals(Biome.BiomeCategory.THEEND)) {
            if (ModConfig.CachedValues.GENERATE_ORES) {
                gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ELEMENTAL_ORE_UPPER.get());
                gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ELEMENTAL_ORE_MIDDLE.get());
                gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ELEMENTAL_ORE_SMALL.get());
            }
        }
    }
}
