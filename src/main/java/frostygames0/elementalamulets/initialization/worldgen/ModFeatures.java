package frostygames0.elementalamulets.initialization.worldgen;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.initialization.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public final class ModFeatures {
    private ModFeatures() {
    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> ELEMENTUM_CRYSTAL_ORE = createKey("elementum_crystal_ore");

    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ElementalAmulets.id(name));
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> bootstrapContext) {
        var stoneRule = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        var deepslateOre = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        var elementumCrystalOreReplacements = List.of(
                OreConfiguration.target(stoneRule, ModBlocks.ELEMENTUM_CRYSTAL_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateOre, ModBlocks.ELEMENTUM_CRYSTAL_DEEPSLATE_ORE.get().defaultBlockState())
        );

        register(bootstrapContext, ELEMENTUM_CRYSTAL_ORE, Feature.ORE, new OreConfiguration(elementumCrystalOreReplacements, 11));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> bootstrapContext,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> resourceKey, F feature, FC configuration) {
        bootstrapContext.register(resourceKey, new ConfiguredFeature<>(feature, configuration));
    }
}
