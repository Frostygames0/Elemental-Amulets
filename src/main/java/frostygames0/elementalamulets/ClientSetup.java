/*
 *  Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets;

import frostygames0.elementalamulets.client.ModKeyBindings;
import frostygames0.elementalamulets.client.models.AmuletModel;
import frostygames0.elementalamulets.client.models.LeafShield;
import frostygames0.elementalamulets.client.particles.ModParticles;
import frostygames0.elementalamulets.client.renderer.ElementalCombinatorRenderer;
import frostygames0.elementalamulets.client.renderer.LeafShieldLayer;
import frostygames0.elementalamulets.client.screens.AmuletBeltScreen;
import frostygames0.elementalamulets.client.screens.ElementalCombinatorScreen;
import frostygames0.elementalamulets.client.screens.LeafChargeOverlay;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.ModBEs;
import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.init.ModMenus;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        ModKeyBindings.registerKeyBinds();
        OverlayRegistry.registerOverlayTop("Nature Charges (Terra-Protection amulet)", new LeafChargeOverlay());
        var amulets = AmuletItem.getAmulets();
        amulets.forEach(amulet -> CuriosRendererRegistry.register(amulet, () -> amulet));
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CELESTIAL_FOCUS.get(), RenderType.translucent());
            MenuScreens.register(ModMenus.ELEMENTAL_COMBINATOR_MENU.get(), ElementalCombinatorScreen::new);
            MenuScreens.register(ModMenus.AMULET_BELT_MENU.get(), AmuletBeltScreen::new);

            amulets.forEach(
                    item -> ItemProperties.register(item, new ResourceLocation(AmuletItem.TIER_TAG),
                            (stack, world, entity, ipf) -> ModConfig.CachedValues.AMULETS_TIER_DIFFERENCE ? item.getTier(stack) : 0));
        });
    }

    @SubscribeEvent
    public static void registerRenderer(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBEs.ELEMENTAL_COMBINATOR_BE.get(), ElementalCombinatorRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(final EntityRenderersEvent.AddLayers event) {
        for (String skinType : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skinType);
            if (renderer != null) renderer.addLayer(new LeafShieldLayer<>(renderer, event.getEntityModels()));
        }
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LeafShield.LAYER_LOCATION, LeafShield::createBodyLayer);
        event.registerLayerDefinition(AmuletModel.LAYER_LOCATION, AmuletModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void textureStitch(final TextureStitchEvent.Pre event) {
        event.addSprite(modPrefix("item/necklace_slot"));
    }

    @SubscribeEvent
    public static void particleFactoryRegister(final ParticleFactoryRegisterEvent event) {
        ModParticles.registerFactories();
    }
}
