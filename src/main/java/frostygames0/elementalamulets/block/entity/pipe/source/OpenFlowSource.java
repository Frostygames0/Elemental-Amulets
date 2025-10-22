package frostygames0.elementalamulets.block.entity.pipe.source;

import frostygames0.elementalamulets.element.ElementType;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

public class OpenFlowSource extends FlowSource {
    public OpenFlowSource() {
        super(null);
    }

    @Override
    public boolean isEndpoint() {
        return true;
    }

    @Override
    public @Nullable Holder<ElementType> getElement() {
        return null;
    }
}
