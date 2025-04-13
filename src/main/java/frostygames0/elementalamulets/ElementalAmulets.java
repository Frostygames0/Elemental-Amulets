package frostygames0.elementalamulets;

import frostygames0.elementalamulets.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(ElementalAmulets.MOD_ID)
public final class ElementalAmulets {
    public static final String MOD_ID = "elementalamulets";

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public ElementalAmulets(IEventBus modBus, Dist dist) {
        ModDataComponents.DATA_COMPONENTS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modBus);
        ModMenuTypes.MENU_TYPES.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModCreativeModeTabs.TABS.register(modBus);

        modBus.addListener(Elements::onRegisterDatapackRegistries);
        modBus.addListener(ModDataMaps::onRegisterDataMaps);
        modBus.addListener(ModCapabilities::onRegisterCapabilitiesEvent);
        modBus.addListener(ModNetworkPayloadHandlers::onRegisterPayloadHandlers);

        NeoForge.EVENT_BUS.addListener(ModCommandHandler::onRegisterCommands);
    }
}
