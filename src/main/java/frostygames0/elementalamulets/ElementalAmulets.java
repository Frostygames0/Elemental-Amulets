package frostygames0.elementalamulets;

import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.core.init.*;
import frostygames0.elementalamulets.items.interfaces.IAmuletItem;
import frostygames0.elementalamulets.network.ModNetworking;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
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
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(ElementalAmulets.MOD_ID)
public class ElementalAmulets {

    public static final String MOD_ID = "elementalamulets";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ItemGroup GROUP = new ElementalAmuletsGroup();

    public ElementalAmulets() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus(); // Only for mod specific events

        ModItems.ITEMS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModTiles.TILES.register(bus);
        ModContainers.CONTAINERS.register(bus);
        ModRecipes.SERIALIZERS.register(bus);
        ModVillagerProfessions.POI_TYPES.register(bus);
        ModVillagerProfessions.PROFESSIONS.register(bus);

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, ModConfig.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ModConfig.CLIENT_SPEC);

        bus.addListener(this::enqueueIMC);
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder(SlotTypePreset.NECKLACE.getIdentifier()).size(1).build());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModNetworking.registerMessages();
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() ->{
            ModItems.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof IAmuletItem).forEach(item -> {
                ItemModelsProperties.registerProperty(item, new ResourceLocation(ElementalAmulets.MOD_ID, "amulet_durability"), (stack, world, living) -> {
                    if (stack.getDamage() <= 200) return 0.0f;
                    else if (stack.getDamage() <= 400) return 0.4f;
                    else if (stack.getDamage() <= 600) return 0.6f;
                    else if (stack.getDamage() <= 800) return 0.8f;
                    else return 1.0f;
                });
            });
        });
    }
}
