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
