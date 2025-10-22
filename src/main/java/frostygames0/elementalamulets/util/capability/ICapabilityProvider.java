package frostygames0.elementalamulets.util.capability;

import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface ICapabilityProvider<T> {
    @Nullable
    T getCapability();

    static <T, C> ICapabilityProvider<T> wrap(BlockCapabilityCache<T, C> cache) {
        return new BlockCapabilityCacheProviderWrapper<>(cache);
    }

    static <T> ICapabilityProvider<T> wrap(Supplier<T> supplier) {
        return new SupplierProviderWrapper<>(supplier);
    }

    static <T> ICapabilityProvider<T> wrap(T cap) {
        return new SimpleProviderWrapper<>(cap);
    }

    class BlockCapabilityCacheProviderWrapper<T, C> implements ICapabilityProvider<T> {
        private final BlockCapabilityCache<T, C> inner;

        private BlockCapabilityCacheProviderWrapper(BlockCapabilityCache<T, C> inner) {
            this.inner = inner;
        }

        @Override
        public @Nullable T getCapability() {
            return inner == null ? null : inner.getCapability();
        }
    }

    class SupplierProviderWrapper<T> implements ICapabilityProvider<T> {
        private final Supplier<T> inner;

        private SupplierProviderWrapper(Supplier<T> inner) {
            this.inner = inner;
        }

        @Override
        public @Nullable T getCapability() {
            return inner == null ? null : inner.get();
        }
    }

    class SimpleProviderWrapper<T> implements ICapabilityProvider<T> {
        private final T inner;

        private SimpleProviderWrapper(T inner) {
            this.inner = inner;
        }

        @Override
        public @Nullable T getCapability() {
            return inner;
        }
    }
}