package frostygames0.elementalamulets.initialization.worldgen;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public final class ModPlacements {
    private ModPlacements() {
    }

    public static final ResourceKey<PlacedFeature> ELEMENTUM_CRYSTAL_ORE_MIDDLE = createKey("elementum_crystal_ore_middle");
    public static final ResourceKey<PlacedFeature> ELEMENTUM_CRYSTAL_ORE_LOWER = createKey("elementum_crystal_ore_lower");

    private static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ElementalAmulets.id(name));
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> bootstrapContext) {
        var elementumCrystalOre = bootstrapContext.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(ModFeatures.ELEMENTUM_CRYSTAL_ORE);

        register(bootstrapContext,
                ELEMENTUM_CRYSTAL_ORE_MIDDLE,
                elementumCrystalOre,
                commonOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(50))));
        register(bootstrapContext,
                ELEMENTUM_CRYSTAL_ORE_LOWER,
                elementumCrystalOre,
                commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-40), VerticalAnchor.absolute(-10))));
    }

    private static void register(BootstrapContext<PlacedFeature> bootstrapContext, ResourceKey<PlacedFeature> resourceKey,
                                 Holder<ConfiguredFeature<?, ?>> configuredFeatureHolder, List<PlacementModifier> placementModifiers) {
        bootstrapContext.register(resourceKey, new PlacedFeature(configuredFeatureHolder, placementModifiers));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier countPlacement, PlacementModifier heightRange) {
        return List.of(countPlacement, InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(count), heightRange);
    }
}
