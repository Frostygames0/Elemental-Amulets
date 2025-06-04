package frostygames0.elementalamulets.client;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.client.debug.ModDebugRenderers;
import frostygames0.elementalamulets.client.gui.PrimitiveElementalExtractorScreen;
import frostygames0.elementalamulets.client.gui.SimpleStorageScreen;
import frostygames0.elementalamulets.client.gui.tooltip.ClientElementalCompositionTooltip;
import frostygames0.elementalamulets.client.gui.tooltip.ElementalCompositionTooltipHandler;
import frostygames0.elementalamulets.client.item.ElementalCompositionTintSource;
import frostygames0.elementalamulets.client.model.PipeConnectionsModelLoader;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.initialization.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = ElementalAmulets.MOD_ID, dist = Dist.CLIENT)
public class ElementalAmuletsClient {

    public ElementalAmuletsClient(IEventBus modBus) {
        modBus.register(this);
        modBus.addListener(ModKeyMappings::onRegisterKeyMappings);

        var gameBus = NeoForge.EVENT_BUS;
        gameBus.addListener(ElementalCompositionTooltipHandler::onTooltipRenderEvent);
        ModDebugRenderers.register(gameBus);
    }

    @SubscribeEvent
    private void onRegisterItemTintSourcesEvent(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(ElementalAmulets.id("elemental_composition"), ElementalCompositionTintSource.MAP_CODEC);
    }

    @SubscribeEvent
    private void onRegisterClientTooltipComponentFactoriesEvent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(ElementalComposition.class, ClientElementalCompositionTooltip::new);
    }

    @SubscribeEvent
    private void onRegisterMenuScreensEvent(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), PrimitiveElementalExtractorScreen::new);
        event.register(ModMenuTypes.SIMPLE_STORAGE_MENU.get(), SimpleStorageScreen::new);
    }

    @SubscribeEvent
    private void onRegisterBlockModelLoaders(ModelEvent.RegisterLoaders event) {
        event.register(PipeConnectionsModelLoader.ID, new PipeConnectionsModelLoader());
    }
}
