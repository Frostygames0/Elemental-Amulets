package frostygames0.elementalamulets.initialization.worldgen;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModBiomeModifiers {
    private ModBiomeModifiers() {
    }

    public static final ResourceKey<BiomeModifier> ADD_ELEMENTUM_CRYSTAL_ORE_LOWER = createKey("add_elementum_crystal_ore_lower");
    public static final ResourceKey<BiomeModifier> ADD_ELEMENTUM_CRYSTAL_ORE_MIDDLE = createKey("add_elementum_crystal_ore_middle");

    public static void bootstrap(BootstrapContext<BiomeModifier> bootstrapContext) {
        registerUndergroundOverworldOre(bootstrapContext, ADD_ELEMENTUM_CRYSTAL_ORE_LOWER, ModPlacements.ELEMENTUM_CRYSTAL_ORE_LOWER);
        registerUndergroundOverworldOre(bootstrapContext, ADD_ELEMENTUM_CRYSTAL_ORE_MIDDLE, ModPlacements.ELEMENTUM_CRYSTAL_ORE_MIDDLE);
    }

    private static void registerUndergroundOverworldOre(BootstrapContext<BiomeModifier> bootstrapContext,
                                                        ResourceKey<BiomeModifier> key,
                                                        ResourceKey<PlacedFeature> feature) {
        var placedFeatures = bootstrapContext.lookup(Registries.PLACED_FEATURE);
        var biomes = bootstrapContext.lookup(Registries.BIOME);

        bootstrapContext.register(key,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(Tags.Biomes.IS_OVERWORLD),
                        HolderSet.direct(placedFeatures.getOrThrow(feature)),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                ));
    }

    private static ResourceKey<BiomeModifier> createKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ElementalAmulets.id(name));
    }
}
