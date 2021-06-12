package frostygames0.elementalamulets.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import frostygames0.elementalamulets.blocks.containers.ElementalCombinatorContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ElementalCrafterGUI extends ContainerScreen<ElementalCombinatorContainer> {
    private static final ResourceLocation GUI = modPrefix("textures/gui/elemental_separator_redesign.png");
    private Button craftButton;
    public ElementalCrafterGUI(ElementalCombinatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.playerInventoryTitleX += 92;
        this.titleX = this.guiLeft+85;
        this.titleY = this.guiTop+8;
        this.xSize += 16;
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
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        int l = this.container.getCombinationTimeScaled();
        this.blit(matrixStack, this.guiLeft+90, this.guiTop+34, 176, 0, l+1, 17);
    }

    @Override
    protected void init() {
        super.init();
        /*this.craftButton = this.addButton(new Button(this.guiLeft+82, this.guiTop+32, 45, 20, new StringTextComponent("Combine"),
                button -> ModNetworking.sendToServer(new SCombineElementalPacket(container.getTileEntity().getPos()))));*/
    }

    @Override
    public void tick() {
        super.tick();
        //this.craftButton.active = false;
    }
}
