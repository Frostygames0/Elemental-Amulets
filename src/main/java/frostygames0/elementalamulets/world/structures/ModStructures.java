package frostygames0.elementalamulets.world.structures;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Frostygames0
 * @date 29.09.2021 14:57
 */
public class ModStructures {
    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, ElementalAmulets.MOD_ID);

    public static final RegistryObject<Structure<NoFeatureConfig>> CULT_TEMPLE = STRUCTURES.register("cult_temple", CultTempleStructure::new);

    public static void setupStructures() {
        setup(CULT_TEMPLE.get(), new StructureSeparationSettings(10, 5, 234235432));
    }

    private static <T extends Structure<?>> void setup(T structure, StructureSeparationSettings settings) {
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder().addAll(Structure.NOISE_AFFECTING_FEATURES).add(structure).build();
        DimensionStructuresSettings.DEFAULTS = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().putAll(DimensionStructuresSettings.DEFAULTS).put(structure, settings).build();

        WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach((spreadSettings) -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap = spreadSettings.getValue().structureSettings().structureConfig();

            if(structureMap instanceof ImmutableMap){
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, settings);
                spreadSettings.getValue().structureSettings().structureConfig = tempMap;
            }
            else{
                structureMap.put(structure, settings);
            }
        });
    }


}
