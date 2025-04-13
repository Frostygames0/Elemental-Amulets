package frostygames0.elementalamulets.client;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.client.gui.PrimitiveElementalExtractorScreen;
import frostygames0.elementalamulets.client.gui.tooltip.ClientElementalCompositionTooltip;
import frostygames0.elementalamulets.client.gui.tooltip.ElementalCompositionTooltipHandler;
import frostygames0.elementalamulets.client.item.ElementalCompositionTintSource;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.registration.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = ElementalAmulets.MOD_ID, dist = Dist.CLIENT)
public class ElementalAmuletsClient {
    public ElementalAmuletsClient(IEventBus modBus) {
        modBus.addListener(ElementalAmuletsClient::onRegisterItemTintSources);
        modBus.addListener(ElementalAmuletsClient::onRegisterClientTooltipComponentFactories);
        modBus.addListener(ElementalAmuletsClient::onRegisterMenuScreens);

        NeoForge.EVENT_BUS.addListener(ElementalCompositionTooltipHandler::onTooltipRenderEvent);
    }

    private static void onRegisterItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(ElementalAmulets.id("elemental_composition"), ElementalCompositionTintSource.MAP_CODEC);
    }

    private static void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(ElementalComposition.class, ClientElementalCompositionTooltip::new);
    }

    private static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), PrimitiveElementalExtractorScreen::new);
    }
}
