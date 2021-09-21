package frostygames0.elementalamulets;

import frostygames0.elementalamulets.client.ModKeyBindings;
import frostygames0.elementalamulets.client.particles.ModParticles;
import frostygames0.elementalamulets.client.renderer.ElementalCombinatorRenderer;
import frostygames0.elementalamulets.client.screens.AmuletBeltScreen;
import frostygames0.elementalamulets.client.screens.ElementalCombinatorScreen;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.core.init.ModBlocks;
import frostygames0.elementalamulets.core.init.ModContainers;
import frostygames0.elementalamulets.core.init.ModItems;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    // Client setup was moved from main class for safety, since it can cause crashes on server
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        ElementalCombinatorRenderer.register();
        ModKeyBindings.registerKeyBinds();
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(ModBlocks.CELESTIAL_FOCUS.get(), RenderType.translucent());
            ScreenManager.register(ModContainers.ELEMENTAL_COMBINATOR_CONTAINER.get(), ElementalCombinatorScreen::new);
            ScreenManager.register(ModContainers.AMULET_BELT_CONTAINER.get(), AmuletBeltScreen::new);

            ModItems.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof AmuletItem).forEach(
                    item -> ItemModelsProperties.register(item, new ResourceLocation(AmuletItem.TIER_TAG),
                            (stack, world, entity) -> ModConfig.cached.AMULETS_TIER_DIFFERENCE ? ((AmuletItem)item).getTier(stack) : 0));
        });
    }

    @SubscribeEvent
    public static void textureStitch(final TextureStitchEvent.Pre event) {
        event.addSprite(new ResourceLocation(ElementalAmulets.MOD_ID, "item/necklace_slot"));
    }

    @SubscribeEvent
    public static void particleFactoryRegister(final ParticleFactoryRegisterEvent event) {
        ModParticles.register();
    }
}
