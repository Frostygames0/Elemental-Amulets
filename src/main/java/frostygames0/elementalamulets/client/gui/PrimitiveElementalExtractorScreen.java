package frostygames0.elementalamulets.client.gui;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.inventory.menu.extractor.PrimitiveElementalExtractorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PrimitiveElementalExtractorScreen extends AbstractContainerScreen<PrimitiveElementalExtractorMenu> {
    private static final ResourceLocation TEXTURE = ElementalAmulets.id("textures/gui/container/primitive_elemental_extractor.png");

    public PrimitiveElementalExtractorScreen(PrimitiveElementalExtractorMenu menu,
                                             Inventory playerInventory,
                                             Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        var storageScale = new ElementStorageBarWidget(this.menu.getStored(),
                this.font,
                true,
                this.leftPos + 70,
                this.topPos + 53,
                36, 4);

        this.addRenderableWidget(storageScale);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RenderType::guiTextured, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }
}
