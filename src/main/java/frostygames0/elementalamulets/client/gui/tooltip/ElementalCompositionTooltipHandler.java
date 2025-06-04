package frostygames0.elementalamulets.client.gui.tooltip;

import com.mojang.datafixers.util.Either;
import frostygames0.elementalamulets.client.ModKeyMappings;
import frostygames0.elementalamulets.element.ElementalHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

public final class ElementalCompositionTooltipHandler {
    private static final Component COMPOSITION_TITLE = Component.translatable("tooltip.elementalamulets.elemental_composition").withStyle(ChatFormatting.GOLD);

    private ElementalCompositionTooltipHandler() {
    }

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
