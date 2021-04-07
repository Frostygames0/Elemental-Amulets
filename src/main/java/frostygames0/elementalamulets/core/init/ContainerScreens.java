package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.client.screens.ElementalCrafterGUI;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerScreens {
    @SubscribeEvent
    public static void CommonSetup(final FMLCommonSetupEvent event) {
        ScreenManager.registerFactory(ModContainers.ELEMENTAL_CRAFTER_CONTAINER.get(), ElementalCrafterGUI::new);
    }
}
