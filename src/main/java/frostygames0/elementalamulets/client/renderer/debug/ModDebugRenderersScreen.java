package frostygames0.elementalamulets.client.renderer.debug;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ModDebugRenderersScreen extends OptionsSubScreen {
    private static final OptionInstance.CaptionBasedToString<Boolean> BOOLEAN_TO_STRING_COLORED = (caption, value) -> value
            ? CommonComponents.OPTION_ON.copy().withStyle(ChatFormatting.GREEN)
            : CommonComponents.OPTION_OFF.copy().withStyle(ChatFormatting.RED);

    public ModDebugRenderersScreen() {
        this(null);
    }

    public ModDebugRenderersScreen(Screen previousScreen) {
        super(previousScreen, Minecraft.getInstance().options, Component.literal("Debug Renderers"));
    }

    @Override
    protected void addOptions() {
        for (var entry : ModDebugRenderers.getRendererEntries()) {
            var optionInstance = OptionInstance.createBoolean(
                    entry.getDisplayName(),
                    OptionInstance.noTooltip(),
                    BOOLEAN_TO_STRING_COLORED,
                    entry.isRendered(),
                    entry::setIsRendered);

            list.addBig(optionInstance);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
