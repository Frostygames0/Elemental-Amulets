/*
 *     Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets;

import frostygames0.elementalamulets.client.ModKeyBindings;
import frostygames0.elementalamulets.client.particles.ModParticles;
import frostygames0.elementalamulets.client.renderer.ElementalCombinatorRenderer;
import frostygames0.elementalamulets.client.renderer.LeafShieldLayer;
import frostygames0.elementalamulets.client.screens.AmuletBeltScreen;
import frostygames0.elementalamulets.client.screens.ElementalCombinatorScreen;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.init.ModContainers;
import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;


import java.util.Map;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        ElementalCombinatorRenderer.register();
        ModKeyBindings.registerKeyBinds();
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(ModBlocks.CELESTIAL_FOCUS.get(), RenderType.translucent());
            ScreenManager.register(ModContainers.ELEMENTAL_COMBINATOR_CONTAINER.get(), ElementalCombinatorScreen::new);
            ScreenManager.register(ModContainers.AMULET_BELT_CONTAINER.get(), AmuletBeltScreen::new);

            ModItems.getAmulets().forEach(
                    item -> ItemModelsProperties.register(item, new ResourceLocation(AmuletItem.TIER_TAG),
                            (stack, world, entity) -> ModConfig.CachedValues.AMULETS_TIER_DIFFERENCE ? item.getTier(stack) : 0));
        });
    }

    @SubscribeEvent
    public static void textureStitch(final TextureStitchEvent.Pre event) {
        event.addSprite(modPrefix("item/necklace_slot"));
    }

    @SubscribeEvent
    public static void particleFactoryRegister(final ParticleFactoryRegisterEvent event) {
        ModParticles.register();
    }

    @SubscribeEvent
    public static void postClientSetup(FMLLoadCompleteEvent event) {
        Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();

        for (PlayerRenderer renderer : skinMap.values()) {
            renderer.addLayer(new LeafShieldLayer<>(renderer));
        }
    }
}
