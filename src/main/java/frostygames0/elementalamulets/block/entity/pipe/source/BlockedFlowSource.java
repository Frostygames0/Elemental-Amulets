package frostygames0.elementalamulets.block.entity.pipe.source;

import frostygames0.elementalamulets.element.ElementType;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

public class BlockedFlowSource extends FlowSource {
    public BlockedFlowSource() {
        super(null);
    }

    @Override
    public @Nullable Holder<ElementType> getElement() {
        return null;
    }

    @Override
    public boolean isEndpoint() {
        return false;
    }
}
