package frostygames0.elementalamulets.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import frostygames0.elementalamulets.blocks.containers.ElementalCombinatorContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ElementalCombinatorScreen extends ContainerScreen<ElementalCombinatorContainer> {
    private static final ResourceLocation GUI = modPrefix("textures/gui/elemental_separator_redesign.png");
    public ElementalCombinatorScreen(ElementalCombinatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.playerInventoryTitleX += 92;
        this.titleX = this.guiLeft+85;
        this.titleY = this.guiTop+8;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        int l = this.container.getCombinationTimeScaled();
        this.blit(matrixStack, this.guiLeft+90, this.guiTop+34, 176, 0, l+1, 17);
    }
}
