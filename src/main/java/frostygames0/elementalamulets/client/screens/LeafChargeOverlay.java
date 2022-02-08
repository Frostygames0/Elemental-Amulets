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

package frostygames0.elementalamulets.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.items.amulets.TerraProtectionAmuletItem;
import frostygames0.elementalamulets.util.AmuletUtil;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
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

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void renderLeafOverlay(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        MatrixStack ms = event.getMatrixStack();

        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD && ModConfig.CachedValues.RENDER_LEAF_CHARGE_OVERLAY) {
            RenderSystem.color4f(1, 1, 1, 1);

            mc.getTextureManager().bind(modPrefix("textures/gui/leaf_charge_overlay.png"));

            ClientPlayerEntity player = mc.player;
            if (player != null) {
                AmuletUtil.getAmuletInSlotOrBelt(ModItems.TERRA_PROTECTION_AMULET.get(), player).ifPresent(triple -> {
                    ItemStack stack = triple.getRight();
                    TerraProtectionAmuletItem amulet = (TerraProtectionAmuletItem) stack.getItem();

                    // Offsetted coordinates in 2d GUI space where bar starts
                    MainWindow window = event.getWindow();
                    int offsetX = (window.getGuiScaledWidth() / 2) + 98;
                    int offsetY = window.getGuiScaledHeight() - 12;

                    RenderSystem.enableBlend();

                    int charge = amulet.getCharges(stack);
                    int clampedMax = MathHelper.clamp(amulet.getMaxCharge(stack), 0, 16);

                    drawLeafBar(ms, clampedMax, offsetX, offsetY, 0);
                    drawLeafBar(ms, MathHelper.clamp(charge, 0, clampedMax), offsetX, offsetY, 8);

                    if (charge > clampedMax) {
                        mc.gui.getFont().drawShadow(ms, '+' + String.valueOf(charge - clampedMax), offsetX + 2, offsetY - 10, 0xFF969696);
                    }

                    RenderSystem.disableBlend();

                });
            }
        }
    }

    private static void drawLeafBar(MatrixStack ms, int size, int posX, int posY, int VOffset) {
        for (int j = 1; j <= size; j++) {
            AbstractGui.blit(ms, posX, posY, 0, VOffset, 7, 7, 16, 16);
            posX += 8;
        }
    }

}
