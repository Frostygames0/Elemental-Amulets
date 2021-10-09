/*
 *    This file is part of Elemental Amulets.
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
import frostygames0.elementalamulets.blocks.containers.ElementalCombinatorContainer;
import frostygames0.elementalamulets.network.ModNetworkHandler;
import frostygames0.elementalamulets.network.SCombinePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ElementalCombinatorScreen extends ContainerScreen<ElementalCombinatorContainer> {
    public static final ResourceLocation GUI = modPrefix("textures/gui/elemental_combinator_gui.png");

    private CombinationButton combineButton;

    public ElementalCombinatorScreen(ElementalCombinatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.inventoryLabelX += 92;
        this.titleLabelX = this.leftPos+85;
        this.titleLabelY = this.topPos+8;
    }

    @Override
    protected void init() {
        super.init();
        this.combineButton = addButton(new CombinationButton(this.leftPos+132, this.topPos+57,
                button -> ModNetworkHandler.sendToServer(new SCombinePacket(menu.getPos()))));
        this.combineButton.active = this.isCombining();
    }

    @Override
    public void tick() {
        super.tick();
        this.combineButton.active = this.isCombining();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(GUI);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int l = this.menu.getCombinationTimeScaled();
        this.blit(matrixStack, this.leftPos + 90, this.topPos + 34, 176, 0, l + 1, 17);
    }

    private boolean isCombining() {
        return this.menu.getCombinatorData().get(0) < 1;
    }

    static class CombinationButton extends Button {

        CombinationButton(int pX, int pY, Button.IPressable pOnPress) {
            super(pX, pY, 21, 13, StringTextComponent.EMPTY, pOnPress);
        }

        @Override
        public void renderButton(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            Minecraft.getInstance().getTextureManager().bind(GUI);
            int yOff = 18;

            if(!this.active) {
                yOff = 31;
            } else if(this.isHovered) {
                yOff = 44;
            }

            this.blit(pMatrixStack, x, y, 178, yOff, width, height);
        }
    }
}
