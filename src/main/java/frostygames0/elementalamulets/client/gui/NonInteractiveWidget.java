package frostygames0.elementalamulets.client.gui;

import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

public abstract class NonInteractiveWidget extends AbstractWidget {
    public NonInteractiveWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_275454_) {
    }

    @Override
    public void playDownSound(SoundManager p_295108_) {
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent p_296129_) {
        return null;
    }
}
