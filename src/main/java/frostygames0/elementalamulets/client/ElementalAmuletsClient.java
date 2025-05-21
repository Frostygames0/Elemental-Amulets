package frostygames0.elementalamulets.client;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.client.debug.BaseElementalPipeDebugRenderer;
import frostygames0.elementalamulets.client.gui.PrimitiveElementalExtractorScreen;
import frostygames0.elementalamulets.client.gui.tooltip.ClientElementalCompositionTooltip;
import frostygames0.elementalamulets.client.gui.tooltip.ElementalCompositionTooltipHandler;
import frostygames0.elementalamulets.client.item.ElementalCompositionTintSource;
import frostygames0.elementalamulets.client.model.PipeConnectionsModelLoader;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.initialization.ModBlockEntities;
import frostygames0.elementalamulets.initialization.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = ElementalAmulets.MOD_ID, dist = Dist.CLIENT)
public class ElementalAmuletsClient {

    public ElementalAmuletsClient(IEventBus modBus) {
        modBus.register(this);

        modBus.addListener(ModKeyMappings::onRegisterKeyMappings);
        NeoForge.EVENT_BUS.addListener(ElementalCompositionTooltipHandler::onTooltipRenderEvent);
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
    }

    @SubscribeEvent
    private void onRegisterBlockModelLoaders(ModelEvent.RegisterLoaders event) {
        event.register(PipeConnectionsModelLoader.ID, new PipeConnectionsModelLoader());
    }

    @SubscribeEvent
    private void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.ELEMENTAL_PIPE.get(), ctx -> new BaseElementalPipeDebugRenderer());
        event.registerBlockEntityRenderer(ModBlockEntities.PRESSURIZER_PIPE.get(), ctx -> new BaseElementalPipeDebugRenderer());
    }
}
