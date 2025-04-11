package frostygames0.elementalamulets.client.gui.tooltip;

import com.mojang.datafixers.util.Either;
import frostygames0.elementalamulets.element.ElementHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ElementalCompositionTooltipHandler {
    private static final Component HIDDEN = Component.translatable("tooltip.elementalamulets.elemental_composition.hidden").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withItalic(true));
    private static final Component COMPOSITION_TITLE = Component.translatable("tooltip.elementalamulets.elemental_composition").withStyle(ChatFormatting.GOLD);

    @SubscribeEvent
    private static void onTooltipRenderEvent(RenderTooltipEvent.GatherComponents event) {

        var clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer == null || !ElementHelper.canSenseElements(clientPlayer)) {
            return;
        }

        var tooltipElements = event.getTooltipElements();
        var composition = ElementHelper.getItemComposition(event.getItemStack());

        if (composition.isEmpty() || composition.get().isEmpty()) {
            return;
        }

        tooltipElements.add(Either.left(Component.empty()));

        if (!Screen.hasShiftDown()) {
            tooltipElements.add(Either.left(HIDDEN));
            return;
        }

        tooltipElements.add(Either.left(COMPOSITION_TITLE));
        tooltipElements.add(Either.right(composition.get()));
    }
}
