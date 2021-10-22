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

package frostygames0.elementalamulets.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/**
 * @author Frostygames0
 * @date 14.10.2021 20:00
 */
@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID, value = Dist.CLIENT)
public class LeafChargeOverlay {

    @SubscribeEvent
    public static void renderLeafOverlay(RenderGameOverlayEvent event) {
        TextureManager manager = Minecraft.getInstance().getTextureManager();
        MatrixStack ms = event.getMatrixStack();

        int posX = (event.getWindow().getGuiScaledWidth() / 2) + 30;
        int posY = event.getWindow().getGuiScaledHeight() - 10;

        if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            RenderSystem.color4f(1,1,1,1);

            manager.bind(modPrefix("textures/gui/leaf_charge_overlay.png"));

            RenderSystem.enableBlend();
            AbstractGui.blit(ms, posX, posY, 0, 0, 80, 7, 256, 256);
            RenderSystem.disableBlend();

            RenderSystem.enableBlend();
            AbstractGui.drawString(ms, Minecraft.getInstance().font, "TEST",posX+85, posY, 1);
            RenderSystem.disableBlend();
        }
    }

}
