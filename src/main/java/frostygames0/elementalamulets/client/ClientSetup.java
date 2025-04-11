package frostygames0.elementalamulets.client;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.client.gui.PrimitiveElementalExtractorScreen;
import frostygames0.elementalamulets.client.gui.tooltip.ClientElementalCompositionTooltip;
import frostygames0.elementalamulets.client.item.ElementalCompositionTintSource;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.registration.ModMenuTypes;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public final class ClientSetup {
    private ClientSetup() {
    }

    public static void onRegisterItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(ElementalAmulets.id("elemental_composition"), ElementalCompositionTintSource.MAP_CODEC);
    }

    public static void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(ElementalComposition.class, ClientElementalCompositionTooltip::new);
    }

    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), PrimitiveElementalExtractorScreen::new);
    }
}
