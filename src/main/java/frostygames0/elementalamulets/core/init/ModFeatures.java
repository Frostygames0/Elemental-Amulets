package frostygames0.elementalamulets.core.init;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ModFeatures {
    public static final ConfiguredFeature<?, ?> ELEMENTAL_ORE = Feature.ORE.withConfiguration(
            new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ModBlocks.ELEMENTAL_STONE.get().getDefaultState(), 9)).range(40).square().count(5);

    public static void register() {
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, modPrefix("elemental_ore"), ELEMENTAL_ORE);
    }
}
