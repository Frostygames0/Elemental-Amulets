package frostygames0.elementalamulets.data;

import frostygames0.elementalamulets.registration.Elements;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class DataGenerationHandler {
    @SubscribeEvent
    private static void onGatherDataEvent(GatherDataEvent.Client event) {
        event.createDatapackRegistryObjects(new RegistrySetBuilder().add(Elements.ELEMENTS_REGISTRY_KEY, Elements::bootstrap));

        event.createProvider(ModDataMapProvider::new);
        event.createProvider(EnglishLanguageProvider::new);
        event.createProvider(ModModelProvider::new);
        event.createProvider(ModBlockTagsProvider::new);

        event.createProvider((output, lookupProvider) ->
                new LootTableProvider(output,
                        Set.of(),
                        List.of(new LootTableProvider.SubProviderEntry(ModBlockLootProvider::new, LootContextParamSets.BLOCK)),
                        lookupProvider));
    }
}
