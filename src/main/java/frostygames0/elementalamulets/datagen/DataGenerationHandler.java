package frostygames0.elementalamulets.datagen;

import frostygames0.elementalamulets.datagen.language.EnglishLanguageProvider;
import frostygames0.elementalamulets.datagen.loot.ModLootTableProvider;
import frostygames0.elementalamulets.datagen.model.ModModelProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class DataGenerationHandler {
    @SubscribeEvent
    private static void onGatherDataEvent(GatherDataEvent.Client event) {
        event.createDatapackRegistryObjects(ModDataPackEntriesProvider.BUILDER);

        event.createProvider(ModDataMapProvider::new);
        event.createProvider(EnglishLanguageProvider::new);
        event.createProvider(ModModelProvider::new);
        event.createProvider(ModBlockTagsProvider::new);
        event.createProvider(ModLootTableProvider::new);
    }
}
