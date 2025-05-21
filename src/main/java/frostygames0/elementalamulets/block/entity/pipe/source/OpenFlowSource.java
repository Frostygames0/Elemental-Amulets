package frostygames0.elementalamulets.block.entity.pipe.source;

import frostygames0.elementalamulets.element.Element;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

public class OpenFlowSource extends FlowSource {
    public OpenFlowSource() {
        super(null, null);
    }

    @Override
    public boolean isEndpoint() {
        return true;
    }

    @Override
    public @Nullable Holder<Element> getElement() {
        return null;
    }
}
