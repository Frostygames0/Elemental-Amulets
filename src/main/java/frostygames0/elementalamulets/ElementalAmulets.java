package frostygames0.elementalamulets;

import frostygames0.elementalamulets.advancements.triggers.ModCriteriaTriggers;
import frostygames0.elementalamulets.client.particles.ModParticles;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.core.init.*;
import frostygames0.elementalamulets.network.ModNetworking;
import frostygames0.elementalamulets.world.LootTableModifiers;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
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
    public static final ItemGroup GROUP = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.FIRE_AMULET.get());
        }
    };

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

        bus.addGenericListener(GlobalLootModifierSerializer.class, LootTableModifiers::registerLootModifierSerializer); // Loot modification register

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, ModConfig.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ModConfig.CLIENT_SPEC);

        bus.addListener(this::enqueueIMC);
        bus.addListener(this::commonSetup);

        forgeBus.addListener(ModCommands::registerCommandsEvent); // Commands don't deserve EventBusSubscriber >:D
    }

    // use this instead, when need ResourceLocation with mod's id
    public static ResourceLocation modPrefix(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("necklace")
                .cosmetic()
                .icon(new ResourceLocation(ElementalAmulets.MOD_ID, "item/necklace_slot"))
                .build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("belt").build());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModNetworking.registerMessages();
        event.enqueueWork(() -> {
            ModFeatures.register();
            ModVillagers.Structures.init();
            ModCriteriaTriggers.register();
        });
    }
}
