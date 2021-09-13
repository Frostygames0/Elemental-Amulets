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
    public static final ResourceLocation GUI = modPrefix("textures/gui/elemental_combinator_gui.png");
    public ElementalCombinatorScreen(ElementalCombinatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.inventoryLabelX += 92;
        this.titleLabelX = this.leftPos+85;
        this.titleLabelY = this.topPos+8;
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
        this.blit(matrixStack, this.leftPos+90, this.topPos+34, 176, 0, l+1, 17);
    }
}
