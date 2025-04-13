package frostygames0.elementalamulets.client.gui;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.inventory.menu.extractor.PrimitiveElementalExtractorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class PrimitiveElementalExtractorScreen extends AbstractElementalExtractorScreen<PrimitiveElementalExtractorMenu> {
    private static final ResourceLocation BACKGROUND = ElementalAmulets.id("textures/gui/container/primitive_elemental_extractor.png");

    private static final ResourceLocation BAR_OVERLAY = ElementalAmulets.id("element_storage_bar/bar_overlay");
    private static final ResourceLocation PROGRESS_ARROW = ElementalAmulets.id("container/elemental_extractor/progress_arrow");
    private static final ResourceLocation LIT_PROGRESS = ElementalAmulets.id("container/elemental_extractor/lit_progress");

    public PrimitiveElementalExtractorScreen(PrimitiveElementalExtractorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, BACKGROUND, createWidgets());
    }

    private static ElementalExtractorScreenWidgets createWidgets() {
        return ElementalExtractorScreenWidgets.builder()
                .addElementStorageBar(70, 53, 36, 4, true, BAR_OVERLAY)
                .addExtractionProgressSprite(63, 33, 14, 8, PROGRESS_ARROW)
                .addLitProgressSprite(44, 30, 14, 14, LIT_PROGRESS)
                .build();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        int j = Mth.ceil(this.menu.getConversionProgress() * 14);
        guiGraphics.blitSprite(RenderType::guiTextured, PROGRESS_ARROW, 14, 8, 0, 0, this.leftPos + 99, this.topPos + 33, j, 8);
    }
}
