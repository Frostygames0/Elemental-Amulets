package frostygames0.elementalamulets.client.gui.tooltip;

import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.registration.Elements;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ClientElementalCompositionTooltip implements ClientTooltipComponent {
    private static final int ELEMENT_ICON_SIZE = 8;
    private static final int OFFSET_X = 2;
    private static final int DISTANCE_BETWEEN_ICON_AND_TEXT = 4;
    private static final int DISTANCE_BETWEEN_ENTRIES = 1;

    private final Map<Holder<Element>, Integer> elementalComposition;

    public ClientElementalCompositionTooltip(ElementalComposition elementalComposition) {
        this.elementalComposition = elementalComposition.elementAmounts();
    }

    @Override
    public int getHeight(Font font) {
        return (font.lineHeight + DISTANCE_BETWEEN_ENTRIES) * elementalComposition.size();
    }

    @Override
    public int getWidth(@NotNull Font font) {
        int maxTextWidth = 0;
        for (var entry : elementalComposition.entrySet()) {
            int textWidth = font.width(makeComponentForElement(entry.getKey(), entry.getValue()));

            if (textWidth > maxTextWidth) {
                maxTextWidth = textWidth;
            }
        }

        return OFFSET_X + ELEMENT_ICON_SIZE + DISTANCE_BETWEEN_ICON_AND_TEXT + maxTextWidth;
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, int width, int height, @NotNull GuiGraphics guiGraphics) {
        renderComposition(this.elementalComposition, guiGraphics, font, x + OFFSET_X, y);
    }

    private static void renderComposition(Map<Holder<Element>, Integer> composition, GuiGraphics guiGraphics, Font font, int x, int y) {
        int i = 0;
        for (var entry : composition.entrySet()) {
            int offsetY = y + i * (font.lineHeight + DISTANCE_BETWEEN_ENTRIES);

            renderElement(entry.getKey(), entry.getValue(), guiGraphics, font, x, offsetY);
            i++;
        }
    }

    private static void renderElement(Holder<Element> elementHolder, Integer amount, GuiGraphics guiGraphics, Font font, int x, int y) {
        var spritePath = getSpritePath(elementHolder.getKey().location());
        var elementText = makeComponentForElement(elementHolder, amount);

        guiGraphics.blitSprite(RenderType::guiTextured, spritePath, x, y, ELEMENT_ICON_SIZE, ELEMENT_ICON_SIZE);
        guiGraphics.drawString(font, elementText, x + ELEMENT_ICON_SIZE + DISTANCE_BETWEEN_ICON_AND_TEXT, y, -1);
    }

    private static Component makeComponentForElement(Holder<Element> elementHolder, Integer amount) {
        return elementHolder.value().colorizeNameMutable()
                .append(Component.literal(" x")
                        .append(amount.toString())
                        .withStyle(ChatFormatting.GRAY));
    }

    private static ResourceLocation getSpritePath(ResourceLocation resourceLocation) {
        return ResourceLocation.fromNamespaceAndPath(resourceLocation.getNamespace(), Elements.ELEMENTS_REGISTRY_KEY.location().getPath() + "/" + resourceLocation.getPath());
    }
}
