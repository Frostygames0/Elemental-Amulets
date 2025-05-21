package frostygames0.elementalamulets.block.entity.pipe.source;

import frostygames0.elementalamulets.element.Element;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

public class BlockedFlowSource extends FlowSource {
    public BlockedFlowSource() {
        super(null, null);
    }

    @Override
    public @Nullable Holder<Element> getElement() {
        return null;
    }

    @Override
    public boolean isEndpoint() {
        return false;
    }
}
