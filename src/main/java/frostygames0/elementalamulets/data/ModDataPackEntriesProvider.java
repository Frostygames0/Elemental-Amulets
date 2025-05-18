package frostygames0.elementalamulets.data;

import frostygames0.elementalamulets.initialization.ModElements;
import frostygames0.elementalamulets.initialization.worldgen.ModBiomeModifiers;
import frostygames0.elementalamulets.initialization.worldgen.ModFeatures;
import frostygames0.elementalamulets.initialization.worldgen.ModPlacements;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModDataPackEntriesProvider {
    public static final RegistrySetBuilder BUILDER =
            new RegistrySetBuilder()
                    .add(ModElements.ELEMENTS, ModElements::bootstrap)
                    .add(Registries.CONFIGURED_FEATURE, ModFeatures::bootstrap)
                    .add(Registries.PLACED_FEATURE, ModPlacements::bootstrap)
                    .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);
}
