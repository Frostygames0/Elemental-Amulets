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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import frostygames0.elementalamulets.blocks.menu.ElementalCombinatorMenu;
import frostygames0.elementalamulets.network.ModNetworkHandler;
import frostygames0.elementalamulets.network.SCombinePacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ElementalCombinatorScreen extends AbstractContainerScreen<ElementalCombinatorMenu> {
    public static final ResourceLocation GUI = modPrefix("textures/gui/elemental_combinator_gui.png");

    private CombinationButton combineButton;

    public ElementalCombinatorScreen(ElementalCombinatorMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.inventoryLabelX += 92;
        this.titleLabelX = this.leftPos + 85;
        this.titleLabelY = this.topPos + 8;
    }

    @Override
    protected void init() {
        super.init();
        this.combineButton = addRenderableWidget(new CombinationButton(this.leftPos + 132, this.topPos + 57,
                button -> ModNetworkHandler.sendToServer(new SCombinePacket(menu.getPos()))));
        this.combineButton.active = this.isCombining();
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.combineButton.active = this.isCombining();
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int l = this.menu.getCombinationTimeScaled();
        this.blit(matrixStack, this.leftPos + 90, this.topPos + 34, 176, 0, l + 1, 17);
    }

    private boolean isCombining() {
        return this.menu.getCombinatorData().get(0) < 1;
    }

    static class CombinationButton extends Button {

        CombinationButton(int pX, int pY, Button.OnPress pOnPress) {
            super(pX, pY, 21, 14, new TranslatableComponent("block.elementalamulets.elemental_combinator.combine_button"), pOnPress);
        }

        @Override
        public void renderButton(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, GUI);
            int yOff = 18;

            if (!this.active) {
                yOff = 32;
            } else if (this.isHovered) {
                yOff = 46;
            }

            this.blit(pMatrixStack, x, y, 178, yOff, width, height);
        }
    }
}
