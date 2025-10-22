package frostygames0.elementalamulets.util.capability;

import org.jetbrains.annotations.Nullable;

public interface IContextualCapabilityProvider<T, C> extends ICapabilityProvider<T> {
    @Nullable
    T getCapability(@Nullable C context);

    @Override
    default @Nullable T getCapability() {
        return getCapability(null);
    }
}
