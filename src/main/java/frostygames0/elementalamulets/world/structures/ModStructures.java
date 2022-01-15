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
package frostygames0.elementalamulets.world.structures;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID)
public class ModStructures {
    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, ElementalAmulets.MOD_ID);

    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> CULT_TEMPLE = STRUCTURES.register("cult_temple", CultTempleStructure::new);

    public static void setupStructures() {
        setup(CULT_TEMPLE.get(), new StructureFeatureConfiguration(50, 12, 234235432));
    }

    private static <T extends StructureFeature<?>> void setup(T structure, StructureFeatureConfiguration settings) {
        StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        StructureSettings.DEFAULTS = ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder().putAll(StructureSettings.DEFAULTS).put(structure, settings).build();

        BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach((spreadSettings) -> {
            Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = spreadSettings.getValue().structureSettings().structureConfig();

            if (structureMap instanceof ImmutableMap) {
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, settings);
                spreadSettings.getValue().structureSettings().structureConfig = tempMap;
            } else {
                structureMap.put(structure, settings);
            }
        });
    }

    private static Method GETCODEC_METHOD;

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel) {
            ServerLevel serverWorld = (ServerLevel) event.getWorld();

            // Skips TerraForged generator
            try {
                if (GETCODEC_METHOD == null)
                    GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().getGenerator()));
                if (cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            } catch (Exception e) {
                ElementalAmulets.LOGGER.error("Was unable to check if " + serverWorld.dimension().location() + " is using Terraforged's ChunkGenerator.");
            }

            if (serverWorld.getChunkSource().getGenerator() instanceof FlatLevelSource &&
                    serverWorld.dimension().equals(Level.OVERWORLD)) {
                return;
            }

            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(serverWorld.getChunkSource().getGenerator().getSettings().structureConfig());
            tempMap.putIfAbsent(ModStructures.CULT_TEMPLE.get(), StructureSettings.DEFAULTS.get(ModStructures.CULT_TEMPLE.get()));
            serverWorld.getChunkSource().getGenerator().getSettings().structureConfig = tempMap;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void addStructuresToWorld(BiomeLoadingEvent event) {
        if (event.getCategory() == Biome.BiomeCategory.JUNGLE && ModConfig.CachedValues.GENERATE_CULT_TEMPLE)
            event.getGeneration()..add(() -> StructureFeatures.CONFIGURED_CULT_TEMPLE);
    }


}
*/