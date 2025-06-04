package frostygames0.elementalamulets.client.gui;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.inventory.menu.SimpleStorageMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SimpleStorageScreen extends AbstractContainerScreen<SimpleStorageMenu> {
    public static final ResourceLocation BACKGROUND = ElementalAmulets.id("textures/gui/container/simple_storage.png");

    public SimpleStorageScreen(SimpleStorageMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        imageHeight = 133;
        inventoryLabelY = imageHeight - 94;

        addRenderableWidget(new ElementStorageBarWidget(menu.getStorage(), font, true, leftPos + 44, topPos + 20, 88, 16, null));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RenderType::guiTextured, BACKGROUND, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }
}
