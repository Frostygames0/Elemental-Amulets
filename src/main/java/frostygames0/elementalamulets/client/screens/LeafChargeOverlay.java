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
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/**
 * @author Frostygames0
 * @date 14.10.2021 20:00
 */
// PROTOTYPE
public class LeafChargeOverlay {

    public static void renderLeafOverlay(RenderGameOverlayEvent event) {
        Minecraft mc = Minecraft.getInstance();
        MatrixStack ms = event.getMatrixStack();
        if(event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
            RenderSystem.color4f(1,1,1,1);
            RenderSystem.enableBlend();
            mc.getTextureManager().bind(modPrefix(""));
            //mc.gui.blit(ms, 10, 10);
            RenderSystem.disableBlend();
        }
    }

}
