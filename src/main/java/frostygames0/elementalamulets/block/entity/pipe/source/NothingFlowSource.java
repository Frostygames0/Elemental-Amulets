package frostygames0.elementalamulets.block.entity.pipe.source;

import frostygames0.elementalamulets.element.Element;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

public class NothingFlowSource extends FlowSource {
    public NothingFlowSource(Direction direction, BlockPos blockPos) {
        super(direction, blockPos);
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
