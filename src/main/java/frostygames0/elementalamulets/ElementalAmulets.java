package frostygames0.elementalamulets;

import frostygames0.elementalamulets.initialization.*;
import frostygames0.elementalamulets.network.ModNetworkPayloadHandlers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(ElementalAmulets.MOD_ID)
public final class ElementalAmulets {
    public static final String MOD_ID = "elementalamulets";

    public ElementalAmulets(IEventBus modBus) {
        ModDataComponents.register(modBus);
        ModBlocks.register(modBus);
        ModBlockEntities.register(modBus);
        ModMenuTypes.register(modBus);
        ModItems.register(modBus);
        ModCreativeModeTabs.register(modBus);

        modBus.addListener(ModElements::onRegisterDatapackRegistries);
        modBus.addListener(ModDataMaps::onRegisterDataMaps);
        modBus.addListener(ModCapabilities::onRegisterCapabilities);
        modBus.addListener(ModNetworkPayloadHandlers::onRegisterPayloadHandlers);

        NeoForge.EVENT_BUS.addListener(ModCommands::onRegisterCommands);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
