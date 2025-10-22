package frostygames0.elementalamulets.client.gui;

import frostygames0.elementalamulets.inventory.menu.extractor.AbstractElementalExtractorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class AbstractElementalExtractorScreen<T extends AbstractElementalExtractorMenu> extends AbstractContainerScreen<T> {
    private final ResourceLocation backgroundTexture;
    private final ElementalExtractorScreenWidgets widgets;

    public AbstractElementalExtractorScreen(T menu, Inventory playerInventory, Component title,
                                            ResourceLocation backgroundTexture, ElementalExtractorScreenWidgets widgets) {
        super(menu, playerInventory, title);
        this.backgroundTexture = backgroundTexture;
        this.widgets = widgets;
    }

    @Override
    protected void init() {
        super.init();

        var elementStorageBarWidget = widgets.elementStorageBar();
        var storageScale = new ElementStorageBarWidget(
                menu.getStorage(),
                font,
                true,
                leftPos + elementStorageBarWidget.x(),
                topPos + elementStorageBarWidget.y(),
                elementStorageBarWidget.width(), elementStorageBarWidget.height(),
                elementStorageBarWidget.sprite()); // 36 4

        addRenderableWidget(storageScale);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RenderType::guiTextured, backgroundTexture, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

        renderLitProgress(guiGraphics);
        renderExtractionProgress(guiGraphics);
    }

    private void renderExtractionProgress(GuiGraphics graphics) {
        var definition = widgets.extractionProgressSprite();

        int j = Mth.ceil(menu.getExtractionProgress() * definition.width());
        graphics.blitSprite(RenderType::guiTextured, definition.sprite(), definition.width(), definition.height(), 0, 0, leftPos + definition.x(), topPos + definition.y(), j, definition.height());
    }

    private void renderLitProgress(GuiGraphics graphics) {
        if (menu.isLit()) {
            var definition = widgets.litProgressSprite();
            var l = Mth.ceil(menu.getLitProgress() * (definition.height() - 1)) + 1;

            graphics.blitSprite(RenderType::guiTextured,
                    definition.sprite(), definition.width(), definition.height(),
                    0, definition.height() - l,
                    leftPos + definition.x(), topPos + definition.y() + definition.height() - l,
                    definition.width(), l);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }
}
