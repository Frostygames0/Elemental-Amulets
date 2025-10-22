package frostygames0.elementalamulets.client.gui.tooltip;

import com.mojang.datafixers.util.Either;
import frostygames0.elementalamulets.client.ModKeyMappings;
import frostygames0.elementalamulets.element.ElementType;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.element.ElementalHelper;
import frostygames0.elementalamulets.initialization.ModElements;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import org.jetbrains.annotations.NotNull;

public class ClientElementalCompositionTooltip implements ClientTooltipComponent {
    private static final int ELEMENT_ICON_SIZE = 8;
    private static final int OFFSET_X = 2;
    private static final int DISTANCE_BETWEEN_ICON_AND_TEXT = 4;
    private static final int DISTANCE_BETWEEN_ENTRIES = 1;

    private final ElementalComposition elementalComposition;

    public ClientElementalCompositionTooltip(ElementalComposition elementalComposition) {
        this.elementalComposition = elementalComposition;
    }

    @Override
    public int getHeight(Font font) {
        return (font.lineHeight + DISTANCE_BETWEEN_ENTRIES) * elementalComposition.size();
    }

    @Override
    public int getWidth(@NotNull Font font) {
        int maxTextWidth = 0;
        for (var entry : elementalComposition.getEntries()) {
            int textWidth = font.width(makeComponentForElement(entry.getKey(), entry.getValue()));

            if (textWidth > maxTextWidth) {
                maxTextWidth = textWidth;
            }
        }

        return OFFSET_X + ELEMENT_ICON_SIZE + DISTANCE_BETWEEN_ICON_AND_TEXT + maxTextWidth;
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, int width, int height, @NotNull GuiGraphics guiGraphics) {
        renderComposition(elementalComposition, guiGraphics, font, x + OFFSET_X, y);
    }

    private static void renderComposition(ElementalComposition composition, GuiGraphics guiGraphics, Font font, int x, int y) {
        int i = 0;
        for (var entry : composition.getEntries()) {
            int offsetY = y + i * (font.lineHeight + DISTANCE_BETWEEN_ENTRIES);

            renderElement(entry.getKey(), entry.getValue(), guiGraphics, font, x, offsetY);
            i++;
        }
    }

    private static void renderElement(Holder<ElementType> elementHolder, Integer amount, GuiGraphics guiGraphics, Font font, int x, int y) {
        var spritePath = getSpritePath(elementHolder.getKey().location());
        var elementText = makeComponentForElement(elementHolder, amount);

        guiGraphics.blitSprite(RenderType::guiTextured, spritePath, x, y, ELEMENT_ICON_SIZE, ELEMENT_ICON_SIZE);
        guiGraphics.drawString(font, elementText, x + ELEMENT_ICON_SIZE + DISTANCE_BETWEEN_ICON_AND_TEXT, y, -1);
    }

    private static Component makeComponentForElement(Holder<ElementType> elementHolder, Integer amount) {
        return elementHolder.value().colorizedName()
                .append(Component.literal(" x")
                        .append(amount.toString())
                        .withStyle(ChatFormatting.GRAY));
    }

    private static ResourceLocation getSpritePath(ResourceLocation resourceLocation) {
        return ResourceLocation.fromNamespaceAndPath(resourceLocation.getNamespace(), ModElements.ELEMENTS.location().getPath() + "/" + resourceLocation.getPath());
    }

    private static final Component COMPOSITION_TITLE = Component.translatable("tooltip.elementalamulets.elemental_composition").withStyle(ChatFormatting.GOLD);

    public static void onTooltipRenderEvent(RenderTooltipEvent.GatherComponents event) {
        var clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer == null || !ElementalHelper.canSenseElements(clientPlayer)) {
            return;
        }

        var tooltipElements = event.getTooltipElements();
        var composition = ElementalHelper.getStackElementalComposition(event.getItemStack());

        if (composition.isEmpty() || composition.get().isEmpty()) {
            return;
        }

        tooltipElements.add(Either.left(Component.empty()));

        if (!ModKeyMappings.isKeyDown(ModKeyMappings.SHOW_COMPOSITION)) {
            tooltipElements.add(
                    Either.left(
                            Component.translatable("tooltip.elementalamulets.elemental_composition.hidden",
                                    ModKeyMappings.SHOW_COMPOSITION.get()
                                            .getKey()
                                            .getDisplayName()
                            ).withStyle(Style.EMPTY
                                    .withColor(ChatFormatting.DARK_GRAY)
                                    .withItalic(true)
                            )
                    )
            );
            return;
        }

        tooltipElements.add(Either.left(COMPOSITION_TITLE));
        tooltipElements.add(Either.right(composition.get()));
    }
}
