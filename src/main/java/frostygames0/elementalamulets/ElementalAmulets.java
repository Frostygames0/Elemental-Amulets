package frostygames0.elementalamulets;

import frostygames0.elementalamulets.advancements.triggers.ModCriteriaTriggers;
import frostygames0.elementalamulets.client.particles.ModParticles;
import frostygames0.elementalamulets.client.renderer.ElementalCombinatorRenderer;
import frostygames0.elementalamulets.client.screens.ElementalCombinatorScreen;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.core.init.*;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
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
        ModParticles.PARTICLES.register(bus);

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, ModConfig.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ModConfig.CLIENT_SPEC);

        bus.addListener(this::enqueueIMC);
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);

        forgeBus.addListener(ModCommands::registerCommandsEvent); // Commands don't deserve EventBusSubscriber >:D
    }

    // use this instead, when you need ResourceLocation with mod's id
    public static ResourceLocation modPrefix(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("necklace")
                .cosmetic()
                .icon(new ResourceLocation(ElementalAmulets.MOD_ID, "item/necklace_slot"))
                .build());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModFeatures.register();
            ModVillagers.Structures.init();
            ModCriteriaTriggers.register();
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ElementalCombinatorRenderer.register();
        event.enqueueWork(() -> {
            ScreenManager.registerFactory(ModContainers.ELEMENTAL_COMBINATOR_CONTAINER.get(), ElementalCombinatorScreen::new);

            ModItems.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof AmuletItem).forEach(
                    item -> ItemModelsProperties.registerProperty(item, new ResourceLocation(AmuletItem.TIER_TAG),
                            (stack, world, entity) -> ModConfig.cached.AMULETS_TIER_DIFFERENCE ? ((AmuletItem)item).getTier(stack) : 0));
        });
    }
}
