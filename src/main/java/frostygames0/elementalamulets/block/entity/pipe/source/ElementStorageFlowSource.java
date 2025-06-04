package frostygames0.elementalamulets.block.entity.pipe.source;

import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.ElementalHelper;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.initialization.ModCapabilities;
import frostygames0.elementalamulets.util.BlockFace;
import frostygames0.elementalamulets.util.ICapabilityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jetbrains.annotations.Nullable;

public class ElementStorageFlowSource extends FlowSource {
    public static final String TAG_ELEMENT = "Element";
    public static final String TAG_DIRECTION = "Direction";

    private ICapabilityProvider<IElementStorage> capCache;
    private Holder<Element> cachedElement;

    public ElementStorageFlowSource(BlockFace face) {
        super(face);
    }

    @Override
    public void manage(Level level, BlockEntity blockEntity) {
        if (level.isClientSide) {
            return;
        }

        if (capCache != null) {
            return;
        }

        var serverLevel = ((ServerLevel) level);
        var relativeBE = level.getBlockEntity(face.getConnectedPos());
        if (relativeBE == null) {
            return;
        }

        capCache = ICapabilityProvider.of(
                BlockCapabilityCache.create(
                        ModCapabilities.ELEMENT_STORAGE_BLOCK,
                        serverLevel,
                        relativeBE.getBlockPos(),
                        face.getOppositeFace(),
                        () -> !blockEntity.isRemoved(),
                        () -> capCache = null)
        );
    }

    @Override
    public @Nullable ICapabilityProvider<IElementStorage> getElementStorageProvider() {
        return capCache;
    }

    @Override
    public @Nullable Holder<Element> getElement() {
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

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        var sourceTag = new CompoundTag();

        sourceTag.putString(TAG_DIRECTION, face.face().getName());

        if (cachedElement != null) {
            ElementalHelper.serializeToNbt(cachedElement, provider).ifPresent(tag -> sourceTag.put(TAG_ELEMENT, tag));
        }

        return sourceTag;
    }

    public static ElementStorageFlowSource deserializeFromNBT(BlockPos blockPos, CompoundTag tag, HolderLookup.Provider provider) {
        var element = ElementalHelper.deserializeFromNbt(tag.get(TAG_ELEMENT), provider).orElse(null);
        var direction = Direction.byName(tag.getString(TAG_DIRECTION));
        if (direction == null) {
            return null;
        }

        var source = new ElementStorageFlowSource(new BlockFace(blockPos, direction));
        source.cachedElement = element;

        return source;
    }

    @Override
    public boolean isEndpoint() {
        return true;
    }
}
