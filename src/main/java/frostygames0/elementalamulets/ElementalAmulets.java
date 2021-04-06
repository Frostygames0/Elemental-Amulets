package frostygames0.elementalamulets;

import frostygames0.elementalamulets.core.init.ModBlocks;
import frostygames0.elementalamulets.core.init.ModContainers;
import frostygames0.elementalamulets.core.init.ModItems;
import frostygames0.elementalamulets.core.init.ModTiles;
import frostygames0.elementalamulets.items.JumpAmulet;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod(ElementalAmulets.MOD_ID)
public class ElementalAmulets {
    public static final String MOD_ID = "elementalamulets";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ItemGroup GROUP = new ElementalAmuletsGroup();
    public ElementalAmulets() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModTiles.TILES.register(bus);
        ModContainers.CONTAINERS.register(bus);

        bus.addListener(this::enqueueIMC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("necklace").build());
    }
}
