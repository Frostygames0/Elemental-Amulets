package frostygames0.elementalamulets.client.gui;

import frostygames0.elementalamulets.element.storage.IElementStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ElementStorageBarWidget extends NonInteractiveWidget {
    private final ResourceLocation overlay;
    private final Font font;
    private final IElementStorage storage;
    private final boolean horizontal;

    public ElementStorageBarWidget(IElementStorage storage, Font font, boolean horizontal, int x, int y, int width, int height, @Nullable ResourceLocation overlay) {
        super(x, y, width, height, CommonComponents.EMPTY);
        this.overlay = overlay;
        this.storage = storage;
        this.font = font;
        this.horizontal = horizontal;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBar(graphics);
        if (isHovered()) {
            renderTooltip(graphics, mouseX, mouseY);
        }
    }

    // TODO Implement vertical render
    private void renderBar(GuiGraphics graphics) {
        int x = getX();
        int y = getY();

        // TODO Do something about this also!
        for (var entry : storage.getStored().getEntries()) {
            var color = entry.getKey().value().color();
            var amount = entry.getValue();

            var progress = Mth.ceil(((float) amount / (float) storage.getMaxCapacity()) * getWidth());
            int maxX = Math.clamp(x + progress, 0, getX() + getWidth());

            graphics.fill(x, y, maxX, y + getHeight(), color);
            x += progress;
        }

        if (overlay != null) {
            graphics.blitSprite(RenderType::guiTextured, overlay, getX(), getY(), getWidth(), getHeight());
        }
    }

    private void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        var title = Component.translatable("tooltip.elementalamulets.element_storage_stored");

        var amountFraction = String.format("%d/%d", storage.getTotalAmount(), storage.getMaxCapacity());
        var distinctFraction = String.format("%d/%s", storage.getDistinctElementsAmount(), storage.getMaxDistinctElementsStored() == Integer.MAX_VALUE ? "∞" : storage.getMaxDistinctElementsStored());
        var both = String.format(" (%s - %s)", amountFraction, distinctFraction);

        title.append(both).withStyle(ChatFormatting.GOLD);

        var composition = storage.getStored();
        graphics.renderTooltip(font,
                List.of(title),
                composition.isEmpty() ? Optional.empty() : Optional.of(composition),
                mouseX, mouseY);
    }
}