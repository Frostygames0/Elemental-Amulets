package frostygames0.elementalamulets.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.blocks.containers.ElementalCombinatorContainer;
import frostygames0.elementalamulets.network.CombineElementalPacket;
import frostygames0.elementalamulets.network.ModNetworking;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ElementalCrafterGUI extends ContainerScreen<ElementalCombinatorContainer> {
    private static final ResourceLocation GUI = new ResourceLocation(ElementalAmulets.MOD_ID, "textures/gui/elemental_separator_redesign.png");
    public ElementalCrafterGUI(ElementalCombinatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.playerInventoryTitleX += 92;
        this.titleX = this.guiLeft+85;
        this.titleY = this.guiTop+8;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
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
        int centerX = (this.width - this.xSize)/2;
        int centerY = (this.height - this.ySize)/2;
        this.blit(matrixStack, centerX, centerY, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void init() {
        super.init();
        this.addButton(new Button(this.guiLeft+82, this.guiTop+32, 45, 20, new StringTextComponent("Combine"),
                (button) -> ModNetworking.sendToServer(new CombineElementalPacket(container.getTileEntity().getPos()))));
    }
}
