package frostygames0.elementalamulets.client.gui;

import frostygames0.elementalamulets.element.storage.IElementStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ElementStorageBarWidget extends AbstractWidget {
    private final ResourceLocation overlay;
    private final Font font;
    private final IElementStorage storage;
    private final boolean horizontal;

    public ElementStorageBarWidget(ResourceLocation overlay, IElementStorage storage, Font font, boolean horizontal, int x, int y, int width, int height) {
        super(x, y, width, height, CommonComponents.EMPTY);

        this.overlay = overlay;
        this.storage = storage;
        this.font = font;
        this.horizontal = horizontal;
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent) {
        return null;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBar(graphics);
        if (this.isHovered()) {
            this.renderTooltip(graphics, mouseX, mouseY);
        }
    }

    // TODO Implement vertical render
    private void renderBar(GuiGraphics graphics) {
        int x = this.getX();
        int y = this.getY();

        for (var entry : this.storage.getStored().elementAmounts().entrySet()) {
            var color = entry.getKey().value().color();
            var amount = entry.getValue();

            var progress = Mth.ceil(((float) amount / (float) this.storage.getMaxCapacity()) * this.getWidth());
            int maxX = Math.clamp(x + progress, 0, this.getX() + this.getWidth());

            graphics.fill(x, y, maxX, y + this.getHeight(), color);
            x += progress;
        }

        graphics.blitSprite(RenderType::guiTextured, this.overlay, this.getX(), this.getY(), getWidth(), getHeight());
    }

    private void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        var title = Component.translatable("tooltip.elementalamulets.element_storage_stored");

        var amountFraction = String.format("%d/%d", this.storage.getTotalAmount(), this.storage.getMaxCapacity());
        var distinctFraction = String.format("%d/%d", this.storage.getDistinctElementsAmount(), this.storage.getMaxDistinctElementsStored());
        var both = String.format(" (%s - %s)", amountFraction, distinctFraction);

        title.append(both).withStyle(ChatFormatting.GOLD);

        var composition = this.storage.getStored();
        graphics.renderTooltip(this.font,
                List.of(title),
                composition.isEmpty() ? Optional.empty() : Optional.of(composition),
                mouseX, mouseY);
    }
}