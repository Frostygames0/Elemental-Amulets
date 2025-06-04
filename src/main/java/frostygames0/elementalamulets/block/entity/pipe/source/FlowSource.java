package frostygames0.elementalamulets.block.entity.pipe.source;

import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.util.BlockFace;
import frostygames0.elementalamulets.util.ICapabilityProvider;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

public abstract class FlowSource {
    protected final BlockFace face;

    public FlowSource(BlockFace face) {
        this.face = face;
    }

    public void manage(Level level, BlockEntity blockEntity) {
    }

    @Nullable
    public ICapabilityProvider<IElementStorage> getElementStorageProvider() {
        return null;
    }

    @Nullable
    public abstract Holder<Element> getElement();

    public abstract boolean isEndpoint();
}
