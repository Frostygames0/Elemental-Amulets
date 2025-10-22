package frostygames0.elementalamulets.pipes;

import frostygames0.elementalamulets.element.ElementType;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.initialization.ModCapabilities;
import frostygames0.elementalamulets.util.capability.ICapabilityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jetbrains.annotations.Nullable;

abstract class FlowSource {
    public void update(Level level, BlockEntity blockEntity) {}

    @Nullable
    public ICapabilityProvider<IElementStorage> getElementStorage() {
        return null;
    }

    @Nullable
    public abstract Holder<ElementType> getElement();

    public static class BlockCapability extends FlowSource {
        private final BlockPos blockPos;
        private final Direction direction;

        private ICapabilityProvider<IElementStorage> capCache;
        private Holder<ElementType> cachedElement;

        public BlockCapability(BlockPos blockPos, Direction direction) {
            this.blockPos = blockPos;
            this.direction = direction;
        }

        @Override
        public void update(Level level, BlockEntity blockEntity) {
            if (level.isClientSide) {
                return;
            }

            if (capCache != null) {
                return;
            }

            var serverLevel = ((ServerLevel) level);
            var relativeBE = level.getBlockEntity(blockPos);
            if (relativeBE == null) {
                return;
            }

            capCache = ICapabilityProvider.wrap(
                    BlockCapabilityCache.create(
                            ModCapabilities.ELEMENT_STORAGE_BLOCK,
                            serverLevel,
                            relativeBE.getBlockPos(),
                            direction.getOpposite(),
                            () -> !blockEntity.isRemoved(),
                            () -> capCache = null)
            );
        }

        public @Nullable ICapabilityProvider<IElementStorage> getElementStorageProvider() {
            return capCache;
        }

        @Override
        public @Nullable Holder<ElementType> getElement() {
            var storageSupplier = getElementStorageProvider();
            if (storageSupplier == null) {
                return null;
            }

            var storage = storageSupplier.getCapability();
            if (storage == null) {
                return null;
            }

            if (cachedElement == null) {
                cachedElement = storage.getAllStoredElementTypes().stream().findFirst().orElse(null);
            } else {
                if (!storage.containsElement(cachedElement)) {
                    cachedElement = null;
                }
            }

            return cachedElement;
        }
    }
}
