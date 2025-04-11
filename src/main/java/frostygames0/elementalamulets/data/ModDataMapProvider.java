package frostygames0.elementalamulets.data;

import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.registration.Elements;
import frostygames0.elementalamulets.registration.ModDataMaps;
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
                        .addElement(Elements.WATER, 3)
                        .addElement(Elements.AETHER, 2)
                        .build(), false)
                .add(Items.SPONGE.builtInRegistryHolder(), ElementalComposition.builder(provider)
                        .addElement(Elements.WATER, 3)
                        .build(), false)
                .add(ItemTags.LOGS_THAT_BURN, ElementalComposition.builder(provider)
                        .addElement(Elements.EARTH, 32)
                        .addElement(Elements.WATER, 100)
                        .build(), false)
                .add(ItemTags.CRIMSON_STEMS, ElementalComposition.builder(provider)
                        .addElement(Elements.FIRE, 10)
                        .build(), false);

    }
}
