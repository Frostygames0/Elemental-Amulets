package frostygames0.elementalamulets.datagen;

import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.initialization.ModDataMaps;
import frostygames0.elementalamulets.initialization.ModElements;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DataMapProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    public ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void gather(@NotNull HolderLookup.Provider provider) {
        builder(ModDataMaps.ELEMENTAL_COMPOSITION)
                .add(Items.WATER_BUCKET.builtInRegistryHolder(), ElementalComposition.builder(provider)
                        .addElement(ModElements.WATER, 3)
                        .addElement(ModElements.AETHER, 2)
                        .build(), false)
                .add(Items.SPONGE.builtInRegistryHolder(), ElementalComposition.builder(provider)
                        .addElement(ModElements.WATER, 3)
                        .build(), false)
                .add(ItemTags.LOGS_THAT_BURN, ElementalComposition.builder(provider)
                        .addElement(ModElements.EARTH, 32)
                        .addElement(ModElements.WATER, 100)
                        .build(), false)
                .add(ItemTags.CRIMSON_STEMS, ElementalComposition.builder(provider)
                        .addElement(ModElements.FIRE, 10)
                        .build(), false);

    }
}
