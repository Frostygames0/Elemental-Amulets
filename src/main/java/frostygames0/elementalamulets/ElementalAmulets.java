package frostygames0.elementalamulets;

import frostygames0.elementalamulets.client.screens.ElementalCrafterGUI;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.core.init.*;
import frostygames0.elementalamulets.network.ModNetworking;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod(ElementalAmulets.MOD_ID)
public class ElementalAmulets {

    public static final String MOD_ID = "elementalamulets";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ItemGroup GROUP = new ElementalAmuletsGroup();

    public ElementalAmulets() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus(); // Only for mod specific events
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        ModItems.ITEMS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModTiles.TILES.register(bus);
        ModContainers.CONTAINERS.register(bus);
        ModRecipes.SERIALIZERS.register(bus);
        ModVillagers.register(bus);

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, ModConfig.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ModConfig.CLIENT_SPEC);

        bus.addListener(this::enqueueIMC);
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("necklace").size(1).cosmetic().build());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModVillagers.Structures.init();
        event.enqueueWork(ModNetworking::registerMessages);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainers.ELEMENTAL_COMBINATOR_CONTAINER.get(), ElementalCrafterGUI::new);
    }
}
